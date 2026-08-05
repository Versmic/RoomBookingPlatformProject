package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;

import roombooking.controller.BookingController;
import roombooking.enums.AccountType;
import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;
import roombooking.model.Student;


public class BookingControllerTest {


    // ==========================
    // Setup Helpers
    // ==========================

    private Account createStudentAccount() {

        Student student = new Student("TEST_STUDENT_ID_001");

        return new Account(
                "testStudentUser001",
                "Password1!",
                "student001@test.com",
                AccountType.STUDENT,
                student
        );
    }


    private Room createRoom() {

        return new Room(
                "TEST_ROOM_001",
                "Testing Building",
                999,
                50,
                RoomStatus.AVAILABLE
        );
    }



    // ==========================
    // Cost Calculation Tests
    // ==========================

    @Test
    public void testCalculateTotalCost() {

        BookingController controller = new BookingController();

        Account account = createStudentAccount();


        double result =
                controller.calculateTotalCost(
                        account,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,30)
                );


        // Student rate = $20/hour
        // 2.5 hours * 20 = 50

        assertEquals(
                50.0,
                result,
                0.01
        );
    }



    @Test
    public void testCalculateInitialDeposit() {

        BookingController controller = new BookingController();

        Account account = createStudentAccount();


        double deposit =
                controller.calculateInitialDeposit(account);


        assertEquals(
                20.0,
                deposit,
                0.01
        );
    }



    // ==========================
    // Store Booking Tests
    // ==========================


    @Test
    public void testStoreBookingCreatesBooking() {

        BookingController controller =
                new BookingController();


        Account account =
                createStudentAccount();


        Room room =
                createRoom();



        Booking booking =
                controller.storeBooking(
                        account,
                        room,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0),
                        BookingStatus.ACTIVE
                );


        assertNotNull(booking);

        assertEquals(
                account,
                booking.getAccount()
        );


        assertEquals(
                room,
                booking.getRoom()
        );


        assertEquals(
                BookingStatus.ACTIVE,
                booking.getStatus()
        );


        assertEquals(
                40.0,
                booking.getFinalCost(),
                0.01
        );
    }




    // ==========================
    // Room Availability Tests
    // ==========================


    @Test
    public void testRoomAvailableValidTime() {

        BookingController controller =
                new BookingController();


        Room room =
                createRoom();



        boolean result =
                controller.isRoomAvailable(
                        room,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0)
                );


        assertTrue(result);
    }



    @Test
    public void testRoomAvailableInvalidTime() {

        BookingController controller =
                new BookingController();


        Room room =
                createRoom();



        boolean result =
                controller.isRoomAvailable(
                        room,
                        LocalDateTime.of(2035,1,1,12,0),
                        LocalDateTime.of(2035,1,1,10,0)
                );


        assertFalse(result);
    }




    @Test
    public void testRoomAvailableNullRoom() {

        BookingController controller =
                new BookingController();



        boolean result =
                controller.isRoomAvailable(
                        null,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0)
                );


        assertFalse(result);
    }



    // ==========================
    // Extension Tests
    // ==========================


    @Test
    public void testExtendBookingSuccessfully() {

        BookingController controller =
                new BookingController();


        Account account =
                createStudentAccount();


        Room room =
                createRoom();



        Booking booking =
                controller.storeBooking(
                        account,
                        room,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0),
                        BookingStatus.ACTIVE
                );


        double oldCost =
                booking.getFinalCost();



        boolean result =
                controller.extendBooking(
                        booking,
                        2
                );



        assertTrue(result);


        assertEquals(
                oldCost + 40,
                booking.getFinalCost(),
                0.01
        );


        assertEquals(
                LocalDateTime.of(2035,1,1,14,0),
                booking.getEndTime()
        );
    }




    @Test
    public void testExtendBookingInvalidHours() {

        BookingController controller =
                new BookingController();


        boolean result =
                controller.extendBooking(
                        null,
                        2
                );


        assertFalse(result);
    }




    @Test
    public void testExtendBookingNegativeHours() {

        BookingController controller =
                new BookingController();


        Account account =
                createStudentAccount();


        Room room =
                createRoom();



        Booking booking =
                controller.storeBooking(
                        account,
                        room,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0),
                        BookingStatus.ACTIVE
                );



        boolean result =
                controller.extendBooking(
                        booking,
                        -1
                );


        assertFalse(result);
    }



    // ==========================
    // Cancel Booking Tests
    // ==========================


    @Test
    public void testCancelFutureBooking() {

        BookingController controller =
                new BookingController();


        Account account =
                createStudentAccount();


        Room room =
                createRoom();



        Booking booking =
                controller.storeBooking(
                        account,
                        room,
                        LocalDateTime.now().plusDays(5),
                        LocalDateTime.now().plusDays(5).plusHours(2),
                        BookingStatus.ACTIVE
                );



        boolean result =
                controller.cancelBooking(booking);



        assertTrue(result);


        assertEquals(
                BookingStatus.CANCELLED,
                booking.getStatus()
        );
    }




    @Test
    public void testCancelNullBooking() {

        BookingController controller =
                new BookingController();


        assertFalse(
                controller.cancelBooking(null)
        );
    }



    // ==========================
    // Available Room List Tests
    // ==========================


    @Test
    public void testGetAvailableRoomsReturnsList() {

        BookingController controller =
                new BookingController();


        List<Room> rooms =
                controller.getAvailableRooms();



        assertNotNull(rooms);
    }



    @Test
    public void testGetAvailableRoomsByTimeReturnsList() {

        BookingController controller =
                new BookingController();


        List<Room> rooms =
                controller.getAvailableRooms(
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0)
                );



        assertNotNull(rooms);
    }


}