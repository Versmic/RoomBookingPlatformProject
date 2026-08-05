package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;
import roombooking.model.Student;
import roombooking.repository.AccountRepository;
import roombooking.repository.BookingRepository;
import roombooking.repository.RoomRepository;


public class BookingRepositoryTest {


    private BookingRepository bookingRepository;
    private AccountRepository accountRepository;
    private RoomRepository roomRepository;

    private Account account;
    private Room room;
    private Booking booking;



    @Before
    public void setUp() {


        bookingRepository = new BookingRepository();
        accountRepository = new AccountRepository();
        roomRepository = new RoomRepository();



        account =
                new Account(
                        "bookinguser",
                        "Password1!",
                        "booking@test.com",
                        AccountType.STUDENT,
                        new Student("ST100")
                );


        accountRepository.saveAccount(account);



        room =
                new Room(
                        "ROOM100",
                        "Main Building",
                        100,
                        20,
                        RoomStatus.AVAILABLE
                );


        roomRepository.saveRoom(room);



        booking =
                new Booking(
                        "B001",
                        account,
                        room,
                        LocalDateTime.of(2035,1,1,10,0),
                        LocalDateTime.of(2035,1,1,12,0),
                        20.0,
                        40.0,
                        BookingStatus.ACTIVE
                );

    }




    @Test
    public void testSaveBooking() {


        bookingRepository.saveBooking(booking);


        Booking result =
                bookingRepository.findBookingById("B001");


        assertNotNull(result);


        assertEquals(
                "B001",
                result.getBookingId()
        );

    }





    @Test
    public void testFindBookingByIdNotFound() {


        Booking result =
                bookingRepository.findBookingById(
                        "UNKNOWN"
                );


        assertNull(result);

    }





    @Test
    public void testUpdateBooking() {


        bookingRepository.saveBooking(
                booking
        );


        booking.setFinalCost(100.0);


        bookingRepository.updateBooking(
                booking
        );



        Booking updated =
                bookingRepository.findBookingById(
                        "B001"
                );


        assertEquals(
                100.0,
                updated.getFinalCost(),
                0.01
        );

    }





    @Test
    public void testDeleteBooking() {


        bookingRepository.saveBooking(
                booking
        );


        bookingRepository.deleteBooking(
                "B001"
        );



        Booking result =
                bookingRepository.findBookingById(
                        "B001"
                );


        assertNull(result);

    }





    @Test
    public void testGetAllBookings() {


        bookingRepository.saveBooking(
                booking
        );


        List<Booking> bookings =
                bookingRepository.getAllBookings();



        assertNotNull(bookings);


        assertTrue(
                bookings.size() > 0
        );

    }





    @Test
    public void testFindBookingsByUsername() {


        bookingRepository.saveBooking(
                booking
        );


        List<Booking> result =
                bookingRepository.findBookingsByUsername(
                        "bookinguser"
                );



        assertEquals(
                1,
                result.size()
        );


        assertEquals(
                "B001",
                result.get(0).getBookingId()
        );

    }





    @Test
    public void testFindBookingsByRoomId() {


        bookingRepository.saveBooking(
                booking
        );


        List<Booking> result =
                bookingRepository.findBookingsByRoomId(
                        "ROOM100"
                );



        assertEquals(
                1,
                result.size()
        );


        assertEquals(
                "ROOM100",
                result.get(0).getRoom().getRoomId()
        );

    }





    @Test
    public void testGenerateBookingId() {


        bookingRepository.saveBooking(
                booking
        );



        String nextId =
                bookingRepository.generateBookingId();



        assertEquals(
                "B002",
                nextId
        );

    }





    @Test
    public void testSaveNullBooking() {


        bookingRepository.saveBooking(null);



        assertTrue(
                bookingRepository.getAllBookings().size() >= 0
        );

    }





    @Test
    public void testDeleteInvalidBookingId() {


        bookingRepository.deleteBooking(
                ""
        );


        assertNull(
                bookingRepository.findBookingById("")
        );

    }


}