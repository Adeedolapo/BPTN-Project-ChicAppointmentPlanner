package com.bptn.bptnproject;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import businessServices.UserAuthentication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class LoginUIController {
	
	@FXML
	private Button login;
	@FXML
	private Label wrongLogin;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private ImageView imageView;
	
	private UserAuthentication userAuthentication;
	

    // Initialize method to link authentication services
    public LoginUIController() {
        userAuthentication = new UserAuthentication();
    }
	
	// Path to the file that stores user credentials
    private final String credentialsFilePath = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\users.txt";
	
 // Method triggered by login button action
	@FXML
    public void userLogin(ActionEvent event) throws IOException{
		login();
	}
	@FXML
    private void login() {
		 // Get input from the UI fields
		String inputUsername = username.getText();
        String inputPassword = password.getText();

        // Validate credentials using the method
        if (validateCredentials(inputUsername, inputPassword)) {
            wrongLogin.setText("Login successful!");
            // Redirect to the next scene
            try {
            	loadBookingUIScene(); //Switch to next screen
            } catch (IOException e) {
            	e.printStackTrace();
            }
        } else {
            wrongLogin.setText("Wrong username or password!");
		
        }
	}
     // This method checks if the username and password match any record in the file
        private boolean validateCredentials(String username, String password) {
            try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Each line in the file contains a username and password separated by a comma
                    String[] credentials = line.split(",");
                    String storedUsername = credentials[0].trim();
                    String storedPassword = credentials[1].trim();

                    // Compare input credentials with the stored credentials
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        return true; // Valid credentials
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading credentials file: " + e.getMessage());
            }
            return false; // Invalid credentials
        }
     // Method to load and switch to the secondary scene after successful login
        private void loadBookingUIScene() throws IOException {
            // Load the new FXML for the secondary screen
            Parent bookingUIRoot = FXMLLoader.load(getClass().getResource("BookingUI.fxml"));

            // Get the current stage (window) to set the new scene
            Stage window = (Stage) login.getScene().getWindow();

            // Set the new scene on the current stage
            window.setScene(new Scene(bookingUIRoot));
            window.show();
        }
    

	private void Secondary() throws IOException {
        App.setRoot("BookingUI");
    }
}


