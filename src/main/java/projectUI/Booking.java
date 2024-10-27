package projectUI;

import businessModels.Bookings;
import businessModels.ChicServices;
import businessServices.BookingManager;
import businessServices.ServicesSelection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Booking {
	
	private ServicesSelection serviceSelection;
    private BookingManager bookingManager;

    public Booking(ServicesSelection serviceSelection, BookingManager bookingManager) {
        this.serviceSelection = serviceSelection;
        this.bookingManager = bookingManager;
    }

    public void startBooking() {
    	Scanner scanner = new Scanner(System.in);

        // Display available services
        serviceSelection.displayServices();

        // Select a service with a stylist
        ChicServices selectedService = serviceSelection.selectServiceWithStylist(scanner);
        if (selectedService == null) {
            System.out.println("Booking process cancelled.");
            return;
        }

        // Input customer details
        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter your contact information: ");
        String customerContact = scanner.nextLine();

        // Select a date
        System.out.print("Enter the booking date (yyyy-mm-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        // Display available times
        serviceSelection.displayTimesAvailable();

        // Select a time
        System.out.print("Enter the booking time (HH:MM): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());

        if (!serviceSelection.isTimesAvailable(time)) {
            System.out.println("Selected time is not available.");
            return;
        }
        
        String status = "Completed"; // Set the initial status

        // Confirm and create the booking
        Bookings newBooking = bookingManager.createBooking(customerName, customerContact, selectedService, date, time, status);
        
        if (newBooking == null) {
            System.out.println("Failed to create booking. Please try again.");
            return;
        }

        serviceSelection.removeTimeSlot(time);

        System.out.println("Booking confirmed! Your booking ID is " + newBooking.getBookingId() + 
                ". You have booked " + selectedService.getName() + " with " + selectedService.getStylists().getName() + 
                " on " + date + " at " + time);
    }
}


