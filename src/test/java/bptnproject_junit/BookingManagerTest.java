package bptnproject_junit;

import businessModels.Bookings;
import businessModels.ChicServices;
import businessModels.Stylist; // Import Stylist if needed
import businessServices.BookingManager;
import businessServices.ServicesSelection;

import org.junit.jupiter.api.BeforeEach; // Correct annotation for setup method
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingManagerTest {

    private BookingManager bookingManager;
    private ChicServices service;
    private ServicesSelection servicesSelection;

    @BeforeEach // Initializes the testing environment before each test case runs
    public void setUp() {
        servicesSelection = new ServicesSelection();
        
        Stylist stylist = new Stylist("Jane Doe"); // Create a Stylist for testing
        service = new ChicServices("Haircut", 50.0, stylist); // Pass the Stylist object to ChicServices
        bookingManager = new BookingManager(servicesSelection);
    }

    @Test
    public void testCreateBooking_Successful() {
        String customerName = "Ife";
        String customerContact = "123-456-7890";
        LocalDate date = LocalDate.now().plusDays(1); // Tomorrow's date
        LocalTime time = LocalTime.of(10, 0); // 10:00 AM
        String status = "Completed";

        // Create booking
        Bookings booking = bookingManager.createBooking(customerName, customerContact, service, date, time, status);

        // Check if booking is successful
        assertNotNull(booking, "Booking should not be null");
        assertEquals(customerName, booking.getCustomerName(), "Customer name should match");
        assertEquals("Haircut", booking.getService().getName(), "Service name should match");
        assertEquals(date, booking.getDate(), "Booking date should match");
        assertEquals(time, booking.getTime(), "Booking time should match");
    }
}
