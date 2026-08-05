package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import roombooking.enums.BookingStatus;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;

public class BookingAITester {

    private Booking booking;
    private Room room;
    private Account account;

    private LocalDateTime startTime;
    private LocalDateTime endTime;


    @Before
    public void setUp() {

        room = new Room(
                "R001",
                "Science Building",
                101,
                50,
                roombooking.enums.RoomStatus.AVAILABLE
        );


        account = null; // Account is not needed for Booking tests


        startTime = LocalDateTime.of(2026, 8, 10, 9, 0);

        endTime = LocalDateTime.of(2026, 8, 10, 11, 0);


        booking = new Booking(
                "B001",
                account,
                room,
                startTime,
                endTime,
                50.00,
                200.00,
                BookingStatus.ACTIVE
        );
    }



    // ==========================
    // Constructor Tests
    // ==========================

    @Test
    public void testBookingConstructor() {

        assertEquals("B001", booking.getBookingId());
        assertEquals(account, booking.getAccount());
        assertEquals(room, booking.getRoom());
        assertEquals(startTime, booking.getStartTime());
        assertEquals(endTime, booking.getEndTime());
        assertEquals(50.00, booking.getDepositAmount(), 0.0);
        assertEquals(200.00, booking.getFinalCost(), 0.0);
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }



    // ==========================
    // Getter Tests
    // ==========================

    @Test
    public void testGetBookingId() {

        assertEquals("B001", booking.getBookingId());
    }



    @Test
    public void testGetAccount() {

        assertEquals(account, booking.getAccount());
    }



    @Test
    public void testGetRoom() {

        assertEquals(room, booking.getRoom());
    }



    @Test
    public void testGetStartTime() {

        assertEquals(startTime, booking.getStartTime());
    }



    @Test
    public void testGetEndTime() {

        assertEquals(endTime, booking.getEndTime());
    }



    @Test
    public void testGetDepositAmount() {

        assertEquals(50.00, booking.getDepositAmount(), 0.0);
    }



    @Test
    public void testGetFinalCost() {

        assertEquals(200.00, booking.getFinalCost(), 0.0);
    }



    @Test
    public void testGetStatus() {

        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }



    // ==========================
    // Setter Tests
    // ==========================

    @Test
    public void testSetEndTime() {

        LocalDateTime newEndTime =
                LocalDateTime.of(2026, 8, 10, 13, 0);


        booking.setEndTime(newEndTime);


        assertEquals(newEndTime, booking.getEndTime());
    }



    @Test
    public void testSetFinalCost() {

        booking.setFinalCost(350.00);


        assertEquals(350.00, booking.getFinalCost(), 0.0);
    }



    @Test
    public void testSetStatusCancelled() {

        booking.setStatus(BookingStatus.CANCELLED);


        assertEquals(
                BookingStatus.CANCELLED,
                booking.getStatus()
        );
    }



    @Test
    public void testSetStatusCompleted() {

        booking.setStatus(BookingStatus.COMPLETED);


        assertEquals(
                BookingStatus.COMPLETED,
                booking.getStatus()
        );
    }



    @Test
    public void testSetStatusNoShow() {

        booking.setStatus(BookingStatus.NOSHOW);


        assertEquals(
                BookingStatus.NOSHOW,
                booking.getStatus()
        );
    }



    // ==========================
    // Edge Case Tests
    // ==========================

    @Test
    public void testSetEndTimeNull() {

        booking.setEndTime(null);


        assertNull(booking.getEndTime());
    }



    @Test
    public void testSetFinalCostZero() {

        booking.setFinalCost(0);


        assertEquals(0, booking.getFinalCost(), 0.0);
    }



    @Test
    public void testSetStatusNull() {

        booking.setStatus(null);


        assertNull(booking.getStatus());
    }



    // ==========================
    // Enum Tests
    // ==========================

    @Test
    public void testBookingStatusValuesExist() {

        assertNotNull(BookingStatus.ACTIVE);
        assertNotNull(BookingStatus.CANCELLED);
        assertNotNull(BookingStatus.NOSHOW);
        assertNotNull(BookingStatus.COMPLETED);
    }
}