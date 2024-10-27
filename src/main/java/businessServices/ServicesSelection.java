/*manage services and available timeslots*/

package businessServices;

import businessModels.ChicServices;
import businessModels.Stylist;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServicesSelection {
    private List<ChicServices> services;//holds list of services
    private List<LocalTime> timesAvailable;// stores available time for booking

    public ServicesSelection() {
        services = new ArrayList<>();
        timesAvailable = new ArrayList<>();

        // Create Stylists
        Stylist stylist1 = new Stylist("Nathan");
        Stylist stylist2 = new Stylist("Laura");
        Stylist stylist3 = new Stylist("Victor");
        Stylist stylist4 = new Stylist("Angel");

        // Adding services with their corresponding stylists
        services.add(new ChicServices("Haircut", 50.0, stylist1));
        services.add(new ChicServices("Hair Coloring", 80.0, stylist2));
        services.add(new ChicServices("Manicure", 20.0, stylist3));
        services.add(new ChicServices("Pedicure", 30.0, stylist3));
        services.add(new ChicServices("Braids", 100.0, stylist4));
        
     // Initialize available times
        for (int i = 9; i <= 17; i++) {
			timesAvailable.add(LocalTime.of(i, 0)); // One-hour slots from 9 AM to 5 PM
    }
    }
    
    // New method to get the list of services
    public List<ChicServices> getServices() {
        return services; // Method to return the list of services
    }
    
    public List<LocalTime> getTimesAvailable() {
        return timesAvailable; // Method to return the available times
    }

    public void displayServices() {
        System.out.println("Available Services:");
        for (ChicServices service : services) {
            System.out.println(service.getName() + " - $" + service.getPrice() + " with " + service.getStylists().getName());
        }
    }

    public ChicServices selectServiceWithStylist(Scanner scanner) {
        while (true) {
        	displayServices();        

        System.out.print("Enter the name of the service you want to book (or type 'exit' to cancel): ");
        String serviceName = scanner.nextLine();

        if (serviceName.equalsIgnoreCase("exit")) {
            return null; // Cancel the selection
        }

        for (ChicServices service : services) {
            if (service.getName().equalsIgnoreCase(serviceName)) {
                return service; // Return the selected service
            }
        }

        System.out.println("Invalid service name.");
        return null; // Return null if no valid service is found
    }
   }

    
	public void displayTimesAvailable() {
        System.out.println("Available Time Slots:");
		for (LocalTime time : timesAvailable) {
            System.out.println(time);
        }
    }

    public boolean isTimesAvailable(LocalTime time) {
        return timesAvailable.contains(time);
    }

    public void removeTimeSlot(LocalTime time) {
    	timesAvailable.remove(time);
    }
}
