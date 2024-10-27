/*Manage the booking process for javafx */

package com.bptn.bptnproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime; // Import LocalTime
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import businessModels.ChicServices;
import businessModels.Stylist;
import businessServices.ServicesSelection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookingUIController {

    @FXML
    private TextField customerName; // Name Field

    @FXML
    private TextField customerContact; // Contact Field

    @FXML
    private ComboBox<ChicServices> selectService; // ComboBox for Services

    @FXML
    private ComboBox<Stylist> selectStylist; // ComboBox for Stylists

    @FXML
    private DatePicker bookingDate; // Date Picker for Booking

    @FXML
    private TextField priceField; // Price Field

    @FXML
    private Label message; // Message Label

    @FXML
    private ComboBox<LocalTime> selectTime; // Booking Time Field (Updated to LocalTime)

    @FXML
    private ComboBox<String> generateReport; // Generate report Field

    @FXML
    private Button confirmBooking; // Confirm Booking Button

    @FXML
    private Button confirmReportGeneration; // Confirm Report Generation Button

    @FXML
    private TextField bookingID; // Booking ID Field

    @FXML
    private Button confirmCancellation; // Confirm Cancellation Button

    // Path to store the booking information
    private final String bookingsFilePath = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\bookings.txt";
    
    private ServicesSelection servicesSelection; // Added instance variable for ServicesSelection

    // Initialize ComboBox with services and stylists
    @FXML
    public void initialize() {
        // Initialize ServicesSelection
        servicesSelection = new ServicesSelection();
        
        // Fetch services and stylists
        List<ChicServices> services = servicesSelection.getServices(); 

        for (ChicServices service : services) {
            selectService.getItems().add(service); // Populate service ComboBox
        }

        // Populate available time slots
        populateAvailableTimes();

        // Populate report options
        generateReport.getItems().addAll("All bookings", "By username", "By date range", "By status");

        // When a service is selected, update the price and stylist list
        selectService.setOnAction(event -> updatePriceAndStylist());
    }
    
    private void populateAvailableTimes() {
        // Clear any existing items in the time ComboBox
        selectTime.getItems().clear();

        // Fetch available times from the ServicesSelection instance
        List<LocalTime> availableTimes = servicesSelection.getTimesAvailable();
        
        // Add available times to the ComboBox
        selectTime.getItems().addAll(availableTimes);
    }

    // Update price field and stylist based on selected service
    private void updatePriceAndStylist() {
        ChicServices selectedService = selectService.getValue();
        if (selectedService != null) {
        	priceField.setText(String.valueOf(selectedService.getPrice())); // Set price
            selectStylist.getItems().clear(); // Clear existing stylists
            selectStylist.setValue(selectedService.getStylists()); // Set stylist
          
            }

            // Re-populate available times based on the selected service and date
            populateAvailableTimes();
        }
    

    // Confirm booking action
    @FXML
    public void confirm(ActionEvent event) {
        ChicServices service = selectService.getValue();
        Stylist stylist = selectStylist.getValue();
        String date = bookingDate.getValue() != null ? bookingDate.getValue().toString() : null;
        LocalTime time = selectTime.getValue(); 

        // Validate inputs
        if (service != null && stylist != null && date != null && time != null && 
            !customerName.getText().isEmpty() && !customerContact.getText().isEmpty()) {
            
            String generatedBookingID = "B-" + UUID.randomUUID().toString(); // Generate Booking ID
            bookingID.setText(generatedBookingID); // Set Booking ID
            message.setText("Booking confirmed!");

            try {
                saveBookingToFile(service, stylist, date, time, generatedBookingID); // Save to file
                loadConfirmationPage(service, stylist, date, time, generatedBookingID); // Show confirmation
            } catch (IOException e) {
                e.printStackTrace();
                message.setText("Error saving booking.");
            }
        } else {
            message.setText("Select again!"); // Show error message
        }
    }

    // Save booking details to file
    private void saveBookingToFile(ChicServices service, Stylist stylist, String date, LocalTime time, String bookingID) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookingsFilePath, true))) {
            writer.write(bookingID + "," + service.getName() + "," + stylist.getName() + "," + 
                         service.getPrice() + "," + date + "," + time + "," + 
                         customerName.getText() + "," + customerContact.getText());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load confirmation page
    private void loadConfirmationPage(ChicServices service, Stylist stylist, String date, LocalTime time, String bookingID) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BookingConfirmationUI.fxml"));
        Parent bookingConfirmationUIRoot = loader.load();

        BookingConfirmationUIController confirmationController = loader.getController();
        confirmationController.setBookingDetails(bookingID, service.getName(), stylist.getName(), 
                                                  String.valueOf(service.getPrice()), date, 
                                                  time.toString(), customerName.getText(), 
                                                  customerContact.getText());

        Stage window = (Stage) confirmBooking.getScene().getWindow();
        window.setScene(new Scene(bookingConfirmationUIRoot));
        window.show();
    }

    // Confirm cancellation action
    @FXML
    public void cancel(ActionEvent event) {
        String idToCancel = bookingID.getText();
        if (idToCancel != null && !idToCancel.isEmpty()) {
            try {
                if (cancelBookingInFile(idToCancel)) {
                    message.setText("Booking cancelled!");
                    loadCancellationPage();
                } else {
                    message.setText("Booking ID not found!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                message.setText("Error cancelling booking.");
            }
        } else {
            message.setText("Please enter a valid Booking ID.");
        }
    }

    // Cancel the booking from the file
    private boolean cancelBookingInFile(String bookingID) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(bookingsFilePath));
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookingsFilePath))) {
            for (String line : lines) {
                if (line.startsWith(bookingID + ",")) {
                    found = true;
                    continue; // Skip the line to remove it
                }
                writer.write(line);
                writer.newLine();
            }
        }

        return found;
    }

    // Load cancellation confirmation page
    private void loadCancellationPage() throws IOException {
        Parent bookingCancellationUIRoot = FXMLLoader.load(getClass().getResource("BookingCancellationUI.fxml"));
        Stage window = (Stage) confirmCancellation.getScene().getWindow();
        window.setScene(new Scene(bookingCancellationUIRoot));
        window.show();
    }
    
    //file path to save reports
    String reportFilePath = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\report.txt"; // Save reports with a unique name
    
    // Generate report action
    @FXML
    private String generateAllBookingsReport() throws IOException {
        List<String> allBookings = Files.readAllLines(Paths.get(bookingsFilePath));
        return formatBookingsReport("All Bookings Report", allBookings);
    }

    private String generateReportByUsername(String username) throws IOException {
        List<String> bookings = Files.readAllLines(Paths.get(bookingsFilePath)).stream()
                .filter(line -> line.contains("," + username + ","))
                .collect(Collectors.toList());
        return formatBookingsReport("Bookings by Username: " + username, bookings);
    }

    private String generateReportByDateRange(LocalDate startDate, LocalDate endDate) throws IOException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<String> bookings = Files.readAllLines(Paths.get(bookingsFilePath)).stream()
                .filter(line -> {
                    String[] fields = line.split(",");
                    LocalDate bookingDate = LocalDate.parse(fields[4], dateFormatter);
                    return (bookingDate.isEqual(startDate) || bookingDate.isAfter(startDate)) &&
                           (bookingDate.isEqual(endDate) || bookingDate.isBefore(endDate));
                })
                .collect(Collectors.toList());

        return formatBookingsReport("Bookings from " + startDate + " to " + endDate, bookings);
    }

    private String generateReportByStatus(String status) throws IOException {
        
        List<String> bookings = Files.readAllLines(Paths.get(bookingsFilePath)).stream()
                .filter(line -> line.endsWith("," + status))
                .collect(Collectors.toList());
        return formatBookingsReport("Bookings with Status: " + status, bookings);
    }

    private String formatBookingsReport(String reportTitle, List<String> bookings) {
        StringBuilder reportContent = new StringBuilder(reportTitle + "\n\n");
        if (bookings.isEmpty()) {
            reportContent.append("No bookings found for this criterion.\n");
        } else {
            for (String booking : bookings) {
                String[] fields = booking.split(",");
                reportContent.append("Booking ID: ").append(fields[0])
                             .append("\nService: ").append(fields[1])
                             .append("\nStylist: ").append(fields[2])
                             .append("\nPrice: ").append(fields[3])
                             .append("\nDate: ").append(fields[4])
                             .append("\nTime: ").append(fields[5])
                             .append("\nCustomer: ").append(fields[6])
                             .append("\nContact: ").append(fields[7])
                             .append("\n\n");
            }
        }
        return reportContent.toString();
    }

    @FXML
    public void generateReport(ActionEvent event) {
        String reportOption = generateReport.getValue();
        String reportContent = "";
        try {
            switch (reportOption) {
                case "All bookings":
                    reportContent = generateAllBookingsReport();
                    break;
                case "By username":
                    String username = customerName.getText();
                    if (username.isEmpty()) {
                        message.setText("Please enter a username.");
                        return;
                    }
                    reportContent = generateReportByUsername(username);
                    break;
                case "By date range":
                    LocalDate startDate = bookingDate.getValue();  
                    LocalDate endDate = bookingDate.getValue();    
                    if (startDate == null || endDate == null) {
                        message.setText("Please select a valid date range.");
                        return;
                    }
                    reportContent = generateReportByDateRange(startDate, endDate);
                    break;
                case "By status":
                    String status = "COMPLETED"; // 
                    reportContent = generateReportByStatus(status);
                    break;
                default:
                    message.setText("Select a report option!");
                    return;
            }

            String reportFileName = reportFilePath.replace("report.txt", "report_" + System.currentTimeMillis() + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFileName))) {
                writer.write(reportContent);
            }
            message.setText("Report generated: " + reportFileName);
        } catch (IOException e) {
            e.printStackTrace();
            message.setText("Error generating report.");
        }
    }

     // Method to switch back to the report page when the button is clicked
    @FXML
    public void confirmReport(ActionEvent event) throws IOException {
        // Load the FXML file for the report page
        Parent reportGenerationUIRoot = FXMLLoader.load(getClass().getResource("ReportGenerationUI.fxml"));

        // Get the current stage (window) from the confirmReport button
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        

        // Set the new scene for the report generation page
        window.setScene(new Scene(reportGenerationUIRoot));
        window.show();
    }

}