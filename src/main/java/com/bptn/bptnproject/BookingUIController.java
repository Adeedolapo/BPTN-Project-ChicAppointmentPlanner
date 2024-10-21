package com.bptn.bptnproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import businessModels.ChicServices;
import businessModels.Stylist;
import businessServices.ServicesSelection;

import java.io.BufferedWriter;
import java.io.FileWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private ComboBox<ChicServices> selectService; // Use ChicServices type
    
    @FXML
    private ComboBox<Stylist> selectStylist; // Use Stylist type

    @FXML
    private DatePicker bookingDate;
    
    @FXML
    private TextField priceField;
    
    @FXML
    private Label message;

    @FXML
    private TextField bookingTime;

    @FXML
    private Button confirmBooking;

    @FXML
    private TextField bookingID;

    @FXML
    private Button confirmCancellation;
    
    // File to save bookings
    private final String bookingsFilePath = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\bookings.txt";

    // Initialize ComboBox with services and stylists
    @FXML
    public void initialize() {
        // Populate services and stylists
        ServicesSelection servicesSelection = new ServicesSelection();
        List<ChicServices> services = servicesSelection.getServices(); // Get the list of services

        for (ChicServices service : services) {
            selectService.getItems().add(service);
            // Check if stylist already exists to avoid duplicates
            if (!selectStylist.getItems().contains(service.getStylists())) {
                selectStylist.getItems().add(service.getStylists());
            }
        }

        // Set a listener to update price when a service is selected
        selectService.setOnAction(event -> updatePriceField());
    }

    private void updatePriceField() {
        ChicServices selectedService = selectService.getValue();
        if (selectedService != null) {
            priceField.setText(String.valueOf(selectedService.getPrice()));
            selectStylist.setValue(selectedService.getStylists()); // Set stylist based on selected service
        }
    }

    // Method triggered by the Confirm Booking button action
    @FXML
    public void confirm(ActionEvent event) {
        ChicServices service = selectService.getValue();
        Stylist stylist = selectStylist.getValue();
        String date = bookingDate.getValue() != null ? bookingDate.getValue().toString() : null;
        String time = bookingTime.getText();
        
        if (service != null && stylist != null && date != null && !time.isEmpty()) {
        	// Generate a unique booking ID
            String generatedBookingID = "B-" + UUID.randomUUID().toString();
            bookingID.setText(generatedBookingID); // Auto-populate the booking ID field
        	
        	message.setText("Booking confirmed!");
            // Save booking to file
            try {
                saveBookingToFile(service, stylist, date, time, generatedBookingID);
                loadConfirmationPage(service, stylist, date, time, generatedBookingID); // Pass details to the confirmation page
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            message.setText("Select again!");
        }
    }

    // Save the booking details to a file
    private void saveBookingToFile(ChicServices service,Stylist stylist, String date, String time, String bookingID) throws IOException {

        // Append the booking details to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookingsFilePath, true))) {
            writer.write(bookingID + "," + service.getName() + "," + stylist.getName()  + "," + service.getPrice() + "," + date + "," + time);
            writer.newLine();
            System.out.println("Booking saved: " + bookingID);
        }
    }

    // Method to switch to the confirmation page after booking confirmation
    private void loadConfirmationPage(ChicServices service, Stylist stylist, String date, String time, String bookingID) throws IOException {
        // Load the FXML file for the confirmation page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bptn/bptnproject/BookingConfirmationUI.fxml"));
        Parent bookingConfirmationUIRoot = loader.load();

        // Get the controller for the booking confirmation UI
        BookingConfirmationUIController confirmationController = loader.getController();
        
        // Pass the booking details to the confirmation controller
        confirmationController.setBookingDetails(
            bookingID,
            service.getName(),
            stylist.getName(),
            String.valueOf(service.getPrice()),
            date,
            time
        );


        // Get the current stage (window)
        Stage window = (Stage) confirmBooking.getScene().getWindow();

        // Set the new scene for the confirmation page
        window.setScene(new Scene(bookingConfirmationUIRoot));
        window.show();
    }

    // Method triggered by the Confirm Cancellation button action
    @FXML
    public void cancel(ActionEvent event) {
        // Add the logic for booking cancellation based on the provided BookingID
        String idToCancel = bookingID.getText();
        if (idToCancel != null && !idToCancel.isEmpty()) {
            // Attempt to cancel the booking by removing it from the file
            try {
                if (cancelBookingInFile(idToCancel)) {
                    message.setText("Booking cancelled!");
                    System.out.println("Booking cancelled successfully.");
                    loadCancellationPage(); // Switch to cancellation confirmation page
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

    // Locate the booking in a file or database and delete it
    private boolean cancelBookingInFile(String bookingID) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(bookingsFilePath));
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookingsFilePath))) {
            for (String line : lines) {
                // Check if the line contains the booking ID
                if (line.startsWith(bookingID + ",")) {
                    found = true; // Booking ID found, skip this line (i.e., remove it)
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        }

        return found;
    }

    // Load the cancellation confirmation page
    private void loadCancellationPage() throws IOException {
        Parent bookingCancellationUIRoot = FXMLLoader.load(getClass().getResource("BookingCancellationUI.fxml"));
        Stage window = (Stage) confirmCancellation.getScene().getWindow();
        window.setScene(new Scene(bookingCancellationUIRoot));
        window.show();
    }
}

