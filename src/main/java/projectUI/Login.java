package projectUI;

import businessServices.UserAuthentication;
import businessServices.BookingManager;
import businessModels.User;
import java.util.Scanner;

public class Login {
    private UserAuthentication userAuthentication;//user authentication
    private BookingManager bookingManager; // Booking manager for reservation management
    private Scanner scanner;

    public Login(UserAuthentication userAuthentication, BookingManager bookingManager, Scanner scanner) {
        this.userAuthentication = userAuthentication;
        this.bookingManager = bookingManager;
        this.scanner = scanner; //pass scanner from main method to parent class
    }

    public void startLogin() {
        System.out.println("Welcome to Chic Appointment Planner!");
        //get username and password
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        //Authenticate user
        User user = userAuthentication.login(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUsername() + "!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
        
        //Method to handle cancellation of reservation
        public void cancelReservation() {
            System.out.print("Enter your booking ID to cancel: ");
            String bookingId = scanner.nextLine();

            if (bookingManager.cancelBooking(bookingId)) {
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("Booking ID not found.");
          }
     }
}