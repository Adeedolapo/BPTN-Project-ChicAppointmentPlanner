/* Creates and sets status of bookings for customers */

package businessModels;

import java.time.LocalDate;
import java.time.LocalTime;

public class Bookings {
	private String bookingId;
    private String customerName;
    private String customerContact;
    private ChicServices service;
    private LocalDate date;
    private LocalTime time;
	private String status;

    //Initialize constructor for service bookings
    public Bookings(String bookingId, String customerName, String customerContact, ChicServices service, LocalDate date, LocalTime time,String status) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.service = service;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    //provide a string representation of booking object
    @Override
    public String toString() {
        return "Booking{" +
        		"bookingId='" + bookingId + '\'' +
                "customerName='" + customerName + '\'' +
                ", customerContact='" + customerContact + '\'' +
                ", service=" + service.getName() +
                ", date=" + date +
                ", time=" + time +
                ", status=" + status +
                '}';
    }


    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }
    
    public String getCustomerContact() {
        return customerContact;
    }

    public ChicServices getService() {
        return service;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public LocalTime getTime() {
        return time;
    }

    public String getStatus() {
        return this.status; // Return current status
    }

    public void setStatus(String status) {
        this.status = status; // Allow status to be updated
    }

    // Additional methods to handle status changes
    public void cancelBooking() {
        setStatus("Cancelled"); // Update status to cancelled
    }

    public void completeBooking() {
        setStatus("Completed"); // Update status to completed
    }
}