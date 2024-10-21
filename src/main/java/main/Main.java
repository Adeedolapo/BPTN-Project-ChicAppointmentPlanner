package main;

import businessModels.Bookings;
import businessModels.ChicServices;
import businessServices.BookingManager;
import businessServices.ServicesSelection;
import businessServices.UserAuthentication;
import projectUI.Login;
import projectUI.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        UserAuthentication userAuthentication = new UserAuthentication();
        ServicesSelection servicesSelection = new ServicesSelection(); // Create ServicesSelection instance
        BookingManager bookingManager = new BookingManager(servicesSelection); // Pass ServicesSelection to BookingManager
        
        Login login = new Login(userAuthentication, bookingManager, scanner);
        login.startLogin();

        List<String> stylists = List.of("stylist1", "stylist2", "stylist3", "stylist4");


        System.out.println("Welcome to Chic Salon Booking System");
        
     // Start the booking process
        Booking booking = new Booking(servicesSelection, bookingManager);

        while (true) {
            System.out.println("1. Create a new booking");
            System.out.println("2. Cancel a booking");
            System.out.println("3. Generate booking report");
            System.out.println("4. Exit");

            System.out.print("Choose an option: ");
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
                    // Generate booking report
                    System.out.println("Generate Report By:");
                    System.out.println("1. All bookings");
                    System.out.println("2. By username");
                    System.out.println("3. By date range");
                    System.out.println("4. By status");
                    int reportOption = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    switch (reportOption) {
                        case 1:
                            bookingManager.generateBookingReport();
                            break;
                        case 2:
                            System.out.print("Enter username: ");
                            String username = scanner.nextLine();
                            List<Bookings> userBookings = bookingManager.generateReportByUser(username);
                            System.out.println(bookingManager.generateReport(userBookings));
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
                    break;

                case 4:
                    // Exit the program
                    System.out.println("Thank you for using Chic Salon Booking System. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
