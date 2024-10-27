package com.bptn.bptnproject;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ReportGenerationUIController {

	 @FXML
	    private AnchorPane serviceManager;

	    @FXML
	    private Label report;
	    
	    @FXML
	    private Label messages;

	    @FXML
	    private Button backToLoginButton;

	    // This method initializes any necessary logic for the scene
	    @FXML
	    public void initialize() {
	        messages.setText("Report Generated Successfully!!!");
	        
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