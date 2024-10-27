/* The User class is used to manage user authentication */
package businessModels;

/*
 * The User class represents a user in the system with a username and password.
 * It utilizes encapsulation to keep the fields private and accessible through public methods.
 */
public class User {
    private String username;
    private String password;

    // Constructor to create a User object with a username and password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

}