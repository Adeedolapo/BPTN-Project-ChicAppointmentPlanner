/* Manage booking functionalities, creating,cancellation and report management*/
package businessServices;

import businessModels.Bookings;
import businessModels.ChicServices;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class BookingManager {
	
	private String filePath = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\bookings.txt";
	String reportFilePath = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\report.txt";
	
	private List<Bookings> bookings = new ArrayList<>();
    private ServicesSelection servicesSelection;

	    public BookingManager(ServicesSelection servicesSelection) {
	        this.servicesSelection = servicesSelection;
	    }


	// Method to create booking
    public Bookings createBooking(String customerName, String customerContact, ChicServices service, LocalDate date, LocalTime time, String status) {
        String bookingId = generateBookingId();
        Bookings booking = new Bookings(bookingId, customerName, customerContact, service, date, time, status);
        bookings.add(booking); // Add booking to list
        //System.out.println("Booking details: " + booking);//debugging
        saveBookingToFile(booking); // Save booking details to file
        return booking; // Return the created booking
    }

    // Method to select a service
    public ChicServices selectService(Scanner inputScanner) {
        return servicesSelection.selectServiceWithStylist(inputScanner);
    }
    
    // Method to select a date
    public LocalDate selectDate(Scanner inputScanner) {
        System.out.print("Enter booking date (YYYY-MM-DD): ");
        String dateInput = inputScanner.nextLine();
        return LocalDate.parse(dateInput); // Parse and return date
    }

    public LocalTime selectTime(Scanner inputScanner) {
        servicesSelection.displayTimesAvailable(); // Display available times
        System.out.print("Select a time by number: ");
        int choice = inputScanner.nextInt();
        inputScanner.nextLine(); 
        
     // Make sure to check if the choice is valid
        if (choice < 1 || choice > servicesSelection.getTimesAvailable().size()) {
            System.out.println("Invalid choice. Please select a valid time.");
            return null; // Return null if the choice is invalid
        }
        return servicesSelection.getTimesAvailable().get(choice - 1); // Return selected time
    }   

    // Method to cancel a booking by ID
    public boolean cancelBooking(String bookingId) {
        Iterator<Bookings> iterator = bookings.iterator(); // Create an iterator for the bookings list
        while (iterator.hasNext()) {
            Bookings booking = iterator.next();
            if (booking.getBookingId().equals(bookingId)) {
                iterator.remove(); // Remove the booking from the list
                saveCancellationToFile(booking); // Save cancellation details to file
                return true; // Return true if cancellation was successful
            }
        }
        return false; // Return false if booking ID was not found
    }

    // Save cancellation to file 
    private void saveCancellationToFile(Bookings booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Cancellation ID: " + booking.getBookingId() + "\n");
            writer.write("Customer: " + booking.getCustomerName() + "\n");
            writer.write("Service: " + booking.getService().getName() + "\n");
            writer.write("Date: " + booking.getDate() + "\n");
            writer.write("Time: " + booking.getTime() + "\n");
            writer.write("----------------------\n");
        } catch (IOException e) {
            System.out.println("Error saving cancellation: " + e.getMessage());
        }
    }

    // Save booking to file 
    private void saveBookingToFile(Bookings booking) {
    	
    	// Print file path for debugging purposes
       // System.out.println("Saving booking to file: " + filePath);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
        	 // Write the booking details to the file
            writer.write(booking.getBookingId() + "," + 
                         booking.getCustomerName() + "," + 
                         booking.getCustomerContact() + "," + 
                         booking.getService().getName() + "," + 
                         booking.getDate() + "," + 
                         booking.getTime() + "," +
                         booking.getStatus());
            writer.newLine(); // Move to the next line
            writer.flush();   // Ensure data is written to the file before closing
            //System.out.println("Booking saved successfully to file.");
        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
            e.printStackTrace(); // print stack trace for debugging
        }
    }

    // Method to generate booking report and save to file
    public void generateBookingReport() {
        StringBuilder reportBuilder = new StringBuilder("Booking Report:\n\n");
        for (Bookings booking : bookings) {
            reportBuilder.append("Booking ID: ").append(booking.getBookingId()).append("\n")
                         .append("Customer: ").append(booking.getCustomerName()).append("\n")
                         .append("Service: ").append(booking.getService().getName()).append("\n")
                         .append("Date: ").append(booking.getDate()).append("\n")
                         .append("Time: ").append(booking.getTime()).append("\n")
                         .append("Status: ").append(booking.getStatus()).append("\n")
                         .append("----------------------\n");
        }

        // Print to console for verification
        System.out.println(reportBuilder.toString());

        // Save report to file
        
        saveReportToFile(reportBuilder.toString(), reportFilePath); 
    }

     // Filtering methods

    // Method to generate report filtered by username
    public List<Bookings> generateReportByUser(String username) {
        List<Bookings> userBookings = new ArrayList<>();
        for (Bookings booking : bookings) {
        	System.out.println("Checking booking for user: " + booking.getCustomerName()); // Debugging line
            if (booking.getCustomerName().equalsIgnoreCase(username)) {
                userBookings.add(booking);
            }
        }
        if (userBookings.isEmpty()) {
            System.out.println("No bookings found for user: " + username);
    }
        return userBookings;
    }    

    // Method to generate report filtered by date range
    public List<Bookings> generateReportByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Bookings> dateFilteredBookings = new ArrayList<>();
        for (Bookings booking : bookings) {
            if ((booking.getDate().isEqual(startDate) || booking.getDate().isAfter(startDate)) &&
                (booking.getDate().isEqual(endDate) || booking.getDate().isBefore(endDate))) {
                dateFilteredBookings.add(booking);
            }
        }
        if (dateFilteredBookings.isEmpty()) {
            System.out.println("No bookings found in the specified date range.");
    }
        return dateFilteredBookings;
    }

    // Method to generate report filtered by booking status
    public List<Bookings> generateReportByStatus(String status) {
        List<Bookings> statusFilteredBookings = new ArrayList<>();
        for (Bookings booking : bookings) {
            if (booking.getStatus().equalsIgnoreCase(status)) {
                statusFilteredBookings.add(booking);
            }
        }
        if (statusFilteredBookings.isEmpty()) {
            System.out.println("No bookings found with status: " + status);
    }
        return statusFilteredBookings;
    }
    // Method to generate a formatted report string for bookings
    public String generateReport(List<Bookings> filteredBookings) {
        StringBuilder reportBuilder = new StringBuilder();
        for (Bookings booking : filteredBookings) {
            reportBuilder.append("Booking ID: ").append(booking.getBookingId()).append("\n")
                         .append("Customer: ").append(booking.getCustomerName()).append("\n")
                         .append("Service: ").append(booking.getService().getName()).append("\n")
                         .append("Price: $").append(booking.getService().getPrice()).append("\n")
                         .append("Stylist: ").append(booking.getService().getStylists()).append("\n")
                         .append("Date: ").append(booking.getDate()).append("\n")
                         .append("Time: ").append(booking.getTime()).append("\n")
                         .append("Status: ").append(booking.getStatus()).append("\n")
                         .append("----------------------\n");
        }
        
        saveReportToFile(reportBuilder.toString(), reportFilePath); 
        return reportBuilder.toString();
    }

    // Method to save the report to a file
    public void saveReportToFile(String reportData, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(reportData);
            System.out.println("Report saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing report to file: " + e.getMessage());
        }
    }

    // Generate booking ID
    private String generateBookingId() {
        return UUID.randomUUID().toString(); // Generates a unique ID
    }

	public String selectStylistForService(Scanner scanner, ChicServices selectedService) {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalTime selectTimeForService(Scanner scanner, ChicServices selectedService) {
		// TODO Auto-generated method stub
		return null;
	}
}



