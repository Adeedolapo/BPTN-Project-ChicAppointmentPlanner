package bptnproject_junit;

import businessModels.Bookings;
import businessModels.ChicServices;
import businessServices.BookingManager;
import businessServices.ServicesSelection;

import org.junit.jupiter.api.BeforeEach; // Correct annotation for setup method
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingManagerCancelTest {

    private BookingManager bookingManager;
    private ChicServices service;
    private String validBookingId;
    private ServicesSelection servicesSelection;

    @BeforeEach //Initializes the testing environment before each test case runs
    public void setUp() {
        servicesSelection = new ServicesSelection();
        service = new ChicServices("Haircut", 50.0, null); // Use null 
        bookingManager = new BookingManager(servicesSelection);

        // Create a booking to cancel later
        Bookings booking = bookingManager.createBooking("Jane Doe", "987-654-3210", service, LocalDate.now().plusDays(1), LocalTime.of(14, 0), "Cancelled");
        validBookingId = booking.getBookingId();
    }

    @Test
    public void testCancelBooking_ValidId() {
        // Cancel the booking with the valid ID
        boolean isCancelled = bookingManager.cancelBooking(validBookingId);

        // Check if cancellation is successful
        assertTrue(isCancelled, "Booking should be canceled successfully");
    }

    @Test
    public void testCancelBooking_InvalidId() {
        // Attempt to cancel with an invalid ID
        boolean isCancelled = bookingManager.cancelBooking("invalid-id");

        // Check if cancellation fails
        assertFalse(isCancelled, "Booking cancellation should fail for invalid ID");
    }
}
