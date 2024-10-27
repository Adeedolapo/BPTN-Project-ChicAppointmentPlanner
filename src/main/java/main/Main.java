package main;

import businessModels.Bookings;
import businessServices.BookingManager;
import businessServices.ServicesSelection;
import businessServices.UserAuthentication;
import projectUI.Login;
import projectUI.Booking;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        UserAuthentication userAuthentication = new UserAuthentication();
        ServicesSelection servicesSelection = new ServicesSelection(); // Create ServicesSelection instance
        BookingManager bookingManager = new BookingManager(servicesSelection); // Pass ServicesSelection to BookingManager for access to available services
        
        Login login = new Login(userAuthentication, bookingManager, scanner);
        login.startLogin();

      
        System.out.println("Welcome to Chic Appointment Planner");
        
     // Start the booking process
        Booking booking = new Booking(servicesSelection, bookingManager);
        
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Create a new booking");
            System.out.println("2. Cancel a booking");
            System.out.println("3. Generate booking report");
            System.out.println("4. Exit");

            System.out.print("Choose an option: ");
            try {
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (option) {
                    case 1:
                        // Call the booking method
                        booking.startBooking();
                        break;

                    case 2:
                        // Cancel a booking
                        System.out.print("Enter the booking ID to cancel: ");
                        String bookingId = scanner.nextLine();
                        boolean isCancelled = bookingManager.cancelBooking(bookingId);
                        if (isCancelled) {
                            System.out.println("Booking cancelled successfully.");
                        } else {
                            System.out.println("Booking ID not found.");
                        }
                        break;

                    case 3:
                        generateBookingReport(scanner, bookingManager);
                        break;

                    case 4:
                        // Exit the program
                        System.out.println("Thank you for using Chic Appointment Planner. Goodbye!");
                        exit = true; // Set exit to true to break the loop
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        scanner.close();
    }

    private static void generateBookingReport(Scanner scanner, BookingManager bookingManager) {
        System.out.println("Generate Report By:");
        System.out.println("1. All bookings");
        System.out.println("2. By username");
        System.out.println("3. By date range");
        System.out.println("4. By status");
        
        int reportOption;
        try {
            reportOption = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (reportOption) {
                case 1:
                    bookingManager.generateBookingReport();
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    List<Bookings> userBookings = bookingManager.generateReportByUser(username);
                    if (userBookings.isEmpty()) {
                        System.out.println("No bookings found for user: " + username);
                    } else {
                        System.out.println(bookingManager.generateReport(userBookings));
                    }
                    break;
                case 3:
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    LocalDate startDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter end date (YYYY-MM-DD): ");
                    LocalDate endDate = LocalDate.parse(scanner.nextLine());
                    List<Bookings> dateRangeBookings = bookingManager.generateReportByDateRange(startDate, endDate);
                    System.out.println(bookingManager.generateReport(dateRangeBookings));
                    break;
                case 4:
                    System.out.print("Enter booking status: ");
                    String status = scanner.nextLine();
                    List<Bookings> statusFilteredBookings = bookingManager.generateReportByStatus(status);
                    System.out.println(bookingManager.generateReport(statusFilteredBookings));
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (InputMismatchException | DateTimeParseException e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear the invalid input
        }
    }
}
