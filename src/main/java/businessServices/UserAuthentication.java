/* Handle login functionality*/

package businessServices;

import businessModels.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserAuthentication {
	
	private static final String USER_FILE = "C:\\Users\\Adedolapo\\OneDrive\\Desktop\\Eclipse\\BPTNproject-ChicAppointmentPlanner\\bptnproject\\src\\main\\java\\users.txt"; // Path to the text file

    public User login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {//Initialize BufferedReader to read from User_File
            String line;
            //loop to read the file line by line
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(","); //split the current line into an array of elements
                if (userData[0].equals(username) && userData[1].equals(password)) {
                    return new User(userData[0], userData[1]); // Return the user object on successful login
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return null; // Return null if login fails
    }

}


