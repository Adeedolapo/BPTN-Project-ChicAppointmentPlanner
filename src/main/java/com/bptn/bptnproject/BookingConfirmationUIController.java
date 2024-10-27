package com.bptn.bptnproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import businessModels.ChicServices;

public class BookingConfirmationUIController {

    @FXML
    private TextField bookingID;
    
    @FXML
    private TextField customerName;
    
    @FXML
    private TextField customerContact;

    @FXML
    private TextField serviceName;

    @FXML
    private TextField stylistName;

    @FXML
    private TextField price;

    @FXML
    private TextField bookingDate;

    @FXML
    private TextField bookingTime;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Label preferredService;
    
    @FXML
    private Label msg;
    
 // Method to populate the fields with booking details
    public void setBookingDetails(String bookingIDValue, String serviceNameValue, String stylistNameValue, String priceValue, String bookingDateValue, String bookingTimeValue, String customerNameValue, String customerContactValue) {
        bookingID.setText(bookingIDValue);
        customerName.setText(customerNameValue); // Set Customer Name
        customerContact.setText(customerContactValue); 
        serviceName.setText(serviceNameValue);
        stylistName.setText(stylistNameValue);
        price.setText(priceValue);
        bookingDate.setText(bookingDateValue);
        bookingTime.setText(bookingTimeValue);
    }

    @FXML
    public void initialize() {
        // Initialize any details here if needed (can be done dynamically from previous scene)
        msg.setText("Booking Confirmed!!!");
    }

    // Method to switch back to the login page when the button is clicked
    @FXML
    public void goBackToLogin(ActionEvent event) throws IOException {
        // Load the FXML file for the login page
        Parent loginUIroot = FXMLLoader.load(getClass().getResource("loginUI.fxml"));

        // Get the current stage (window)
        Stage window = (Stage) backToLoginButton.getScene().getWindow();

        // Set the new scene for the login page
        window.setScene(new Scene(loginUIroot));
        window.show();
    }

}

	
