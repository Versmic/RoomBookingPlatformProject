package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.Booking;
import roombooking.model.Room;

public class RoomTest {

    private Room room;
    private Booking booking1;
    private Booking booking2;


    @Before
    public void setUp() {

        room = new Room(
                "R001",
                "Science Building",
                101,
                50,
                RoomStatus.AVAILABLE
        );


        booking1 = new Booking(
                "B001",
                null,
                room,
                LocalDateTime.of(2026, 8, 5, 10, 0),
                LocalDateTime.of(2026, 8, 5, 12, 0),
                50.00,
                200.00,
                BookingStatus.ACTIVE
        );


        booking2 = new Booking(
                "B002",
                null,
                room,
                LocalDateTime.of(2026, 8, 6, 14, 0),
                LocalDateTime.of(2026, 8, 6, 16, 0),
                75.00,
                250.00,
                BookingStatus.ACTIVE
        );
    }


    // ===============================
    // Constructor Tests
    // ===============================

    @Test
    public void testRoomConstructor() {

        assertEquals("R001", room.getRoomId());
        assertEquals("Science Building", room.getBuildingName());
        assertEquals(101, room.getRoomNumber());
        assertEquals(50, room.getCapacity());
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
    }


    // ===============================
    // Getter Tests
    // ===============================

    @Test
    public void testGetRoomId() {

        assertEquals("R001", room.getRoomId());
    }


    @Test
    public void testGetBuildingName() {

        assertEquals("Science Building", room.getBuildingName());
    }


    @Test
    public void testGetCapacity() {

        assertEquals(50, room.getCapacity());
    }


    @Test
    public void testGetStatus() {

        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
    }


    // ===============================
    // Add Booking Tests
    // ===============================

    @Test
    public void testAddBooking() {

        room.addBooking(booking1);

        assertEquals(1, room.getBookings().size());
        assertTrue(room.getBookings().contains(booking1));
    }


    @Test
    public void testAddMultipleBookings() {

        room.addBooking(booking1);
        room.addBooking(booking2);

        assertEquals(2, room.getBookings().size());
    }


    @Test
    public void testAddDuplicateBooking() {

        room.addBooking(booking1);
        room.addBooking(booking1);

        assertEquals(1, room.getBookings().size());
    }


    @Test
    public void testAddNullBooking() {

        room.addBooking(null);

        assertTrue(room.getBookings().isEmpty());
    }


    // ===============================
    // Remove Booking Tests
    // ===============================

    @Test
    public void testRemoveBooking() {

        room.addBooking(booking1);

        room.removeBooking(booking1);

        assertFalse(room.getBookings().contains(booking1));
        assertEquals(0, room.getBookings().size());
    }


    @Test
    public void testRemoveBookingNotPresent() {

        room.removeBooking(booking1);

        assertTrue(room.getBookings().isEmpty());
    }


    @Test
    public void testRemoveNullBooking() {

        room.addBooking(booking1);

        room.removeBooking(null);

        assertEquals(1, room.getBookings().size());
    }


    // ===============================
    // Room Status Tests
    // ===============================

    @Test
    public void testSetRoomStatusMaintenance() {

        room.setStatus(RoomStatus.MAINTENANCE);

        assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
    }


    @Test
    public void testSetRoomStatusDisabled() {

        room.setStatus(RoomStatus.DISABLED);

        assertEquals(RoomStatus.DISABLED, room.getStatus());
    }


    @Test
    public void testSetRoomStatusNull() {

        room.setStatus(null);

        assertNull(room.getStatus());
    }


    // ===============================
    // Collection Protection Test
    // ===============================

    @Test
    public void testBookingListIsUnmodifiable() {

        room.addBooking(booking1);

        List<Booking> bookings = room.getBookings();

        try {
            bookings.add(booking2);
            fail("Expected UnsupportedOperationException");
        } 
        catch (UnsupportedOperationException e) {
            // Test passes
        }
    }
}