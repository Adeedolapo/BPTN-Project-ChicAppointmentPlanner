package com.bptn.bptnproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class BookingCancellationUIController {

    @FXML
    private AnchorPane serviceManager;

    @FXML
    private Label preferredService;
    
    @FXML
    private Label messages;

    @FXML
    private Button backToLoginButton;

    // This method initializes any necessary logic for the scene
    @FXML
    public void initialize() {
        messages.setText("Booking Cancelled Successfully!!!");
        
    }

    // Method to handle navigating back to login page
    @FXML
    private void backToLoginPage() {
        try {
            // Load the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginUI.fxml")); 
            Parent loginUIRoot = loader.load();
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) serviceManager.getScene().getWindow();
            Scene scene = new Scene(loginUIRoot);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading login page: " + e.getMessage());
        }
    }
}