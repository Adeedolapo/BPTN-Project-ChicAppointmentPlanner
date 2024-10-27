/* The ChicServices class is designed to encapsulate the details of a service. 
 This class plays a crucial role in managing service information. */

package businessModels;

public class ChicServices {
	
	private String name;
    private double price;
    private Stylist stylists;
    
    //Initialize constructors for service variables
    public ChicServices(String name, double price, Stylist stylists) {
        this.name = name;
        this.price = price;
        this.stylists = stylists;
    }

	public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Stylist getStylists() {
        return stylists;
    }

  //Override toString() to display service name in Javafx
    @Override
    public String toString() {
        return name; // This ensures that only the service name is displayed in ComboBoxes
    }
}
