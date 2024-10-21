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

}
