/* Stylist class was designed to hold and manage stylist attributes */

package businessModels;

public class Stylist {
	
	private String name;
    

	public Stylist(String name) {
	        this.name = name;
	        
	    }

	public String getName() {
        return name;
    }

	// Override toString() to display stylist name in Javafx
    @Override
    public String toString() {
        return name; // This ensures that only the stylist name is displayed in ComboBoxes
    }
}

