package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import roombooking.controller.BookingController;
import roombooking.enums.*;
import roombooking.model.*;
import roombooking.repository.*;

public class BookingControllerTester {
	// this test makes sure final price calculation is valid
	@Test 
	public void testFinalPriceCalculation() {
		BookingController bookingController = new BookingController();
		
		// test account for cost calculation
		Student student = new Student("0");
		Account account = new Account("test", "test", "test@gmail.com", AccountType.STUDENT, student);
		// test start and end datetimes
		// start -> December 25 2026 at 9am
		// end -> December 31 2026 at 5:30pm
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 31, 17, 30);

		// store the actual total cost
		double actualTotalCost = 152.5 * 20;
		// compare actual vs calculateTotalCost()
		assertEquals(actualTotalCost, bookingController.calculateTotalCost(account, startTime, endTime), 0.001);
	
	}
	
	// this test makes sure the room booking 
	// controller gets all currently available rooms
	// stored in the database
	@Test
	public void testGetAvailableRooms() {
		BookingController bookingController = new BookingController();
		
		// roomRepo so we can access our room info
		RoomRepository roomRepo = new RoomRepository();
		
		// variable to hold the original room info, second for setting all room to disabled
		List<Room> originalRooms = roomRepo.getAllRooms();
		
		try {
			// loop through rooms and delete
			for(Room rooms : originalRooms) {
				roomRepo.deleteRoom(rooms.getRoomId());	
			}
			
			// add a new available room to the database
			Room roomAvailable = new Room("test0", "test", 0, 0, RoomStatus.AVAILABLE);	
			roomRepo.saveRoom(roomAvailable);
			
			// add a new disabled room to the database
			Room roomDisabled = new Room("test1", "test2", 0, 0, RoomStatus.DISABLED);
			roomRepo.saveRoom(roomDisabled);
			
			// get current available rooms
			List<Room> allRooms = bookingController.getAvailableRooms();
			
			// create variable that holds actual available rooms
			List<Room> actualRoomList = new ArrayList<>();
			actualRoomList.add(roomAvailable);
			
			// test if room arrays are equal
			assertEquals(actualRoomList.size(), allRooms.size());
			assertEquals(actualRoomList.get(0).getRoomId(), allRooms.get(0).getRoomId());
			assertEquals(actualRoomList.get(0).getBuildingName(), allRooms.get(0).getBuildingName());
			
		} 
		
		finally {
			// delete tester rooms from database
			roomRepo.deleteRoom("test0");
			roomRepo.deleteRoom("test1");
			
			// add back original rooms into database
			for(Room rooms : originalRooms) {
				roomRepo.saveRoom(rooms);
			}
		}
	}
	
	// test to see if our booking controller correctly assigns
	// deposit amounts depending on the account passed
	@Test
	public void testInitialDepositAmount() {
		BookingController bookingController = new BookingController();
		// actual deposit amounts
		double actualStudentDeposit = 20.00;
		double actualFacultyDeposit = 30.00;
		double actualStaffDeposit = 40.00;
		double actualPartnerDeposit = 50.00;
		
		// test student deposit amount
		Student student = new Student("0");
		Account account = new Account("test", "test", "test@gmail.com", AccountType.STUDENT, student);
		assertEquals(actualStudentDeposit, bookingController.calculateInitialDeposit(account), 0.001);
		
		
		// test faculty deposit amount
		Faculty faculty = new Faculty("0");
		account = new Account("test", "test", "test@gmail.com", AccountType.FACULTY, faculty);
		assertEquals(actualFacultyDeposit, bookingController.calculateInitialDeposit(account), 0.001);
		
		// test staff deposit amount
		Staff staff = new Staff("0");
		account = new Account("test", "test", "test@gmail.com", AccountType.STAFF, staff);
		assertEquals(actualStaffDeposit, bookingController.calculateInitialDeposit(account), 0.001);
		
		// test partner deposit amount
		Partner partner = new Partner("0");
		account = new Account("test", "test", "test@gmail.com", AccountType.PARTNER, partner);
		assertEquals(actualPartnerDeposit, bookingController.calculateInitialDeposit(account), 0.001);
		
		
	}
	
	// test to see if booking controller store booking 
	// method properly stores a booking
	// and holds the right information
	@Test
	public void testStoreBooking() {
		BookingController bookingController = new BookingController();
		
		// repo to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		
		// store a copy of all the original bookings
		List<Booking> originalBookings = bookingRepo.getAllBookings();
		
		// store the booking Id that our new booking will take
		String actualBookingId = bookingRepo.generateBookingId();
		
		// store actual info
		String actualUserName = "testusername";
		String actualRoomId = "roomTest0";
		
		try {
			// test account for the store booking
			Student student = new Student("0");
			Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
			
			// store the test account
			accountRepo.saveAccount(account);
			
			// test room for the store booking
			Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
			
			// store the test room
			roomRepo.saveRoom(room);
			
			// test local start and end datetimes
			LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
			LocalDateTime endTime = LocalDateTime.of(2026, 12, 31, 17, 30);
			
			// use bookingController store booking method to store our test booking
			bookingController.storeBooking(account, room, startTime, endTime, BookingStatus.ACTIVE);
			
			// get all new bookings from our repository
			List<Booking> newBookings = bookingRepo.getAllBookings();
			
			// check if the booking got added
			assertEquals(originalBookings.size() + 1, newBookings.size());
			
			// get the last booking in newBookings
			Booking storedBooking = newBookings.get(newBookings.size() - 1);
			
			// check if the last booking in newBookings is equal to the one we just attempted to store
			assertEquals(actualBookingId, storedBooking.getBookingId());
			
			// check if actualUserName and actualRoomName were stored
			assertEquals(actualUserName, storedBooking.getAccount().getUserName().toLowerCase());
			assertEquals(actualRoomId, storedBooking.getRoom().getRoomId());
			
		} 
		finally {
			// delete the test booking, account, and room
			bookingRepo.deleteBooking(actualBookingId);
			roomRepo.deleteRoom(actualRoomId);
			accountRepo.deleteUser(actualUserName);
		}
	}
	
	
	// this test ensures that bookingControllers storeBooking method
	// properly creates and returns the booking object we expect
	@Test
	public void testStoreBookingReturn() {
		BookingController bookingController = new BookingController();
		
		// repo to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		
		//test start end datetime
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 1, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 10, 9, 0);
		
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);	
		
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		
		// generate the expected booking id before bookingController activates...
		// if we did this after we called storeBooking then generateBookingId would be 1 higher
		String expectedBookingId = bookingRepo.generateBookingId();
		
		// generate the rest of our expected return values
		String expectedUsername = account.getUserName();
		String expectedRoomId = room.getRoomId();
		
		Booking booking = null;
		
		try {
			// use booking controller to call storeBooking and save its return to booking
			booking = bookingController.storeBooking(account, room, startTime, endTime, BookingStatus.ACTIVE);
			// check if storeBooking() return is what we expected
			assertEquals(expectedBookingId, booking.getBookingId());
			assertEquals(expectedUsername, booking.getAccount().getUserName());
			assertEquals(expectedRoomId, booking.getRoom().getRoomId());
			
		} 
		
		finally {
			// delete the test booking if it was successfully created
			bookingRepo.deleteBooking(booking.getBookingId());
		}
	}
	
	// test to make sure getCurrentBookings in
	// booking controller properly returns all the current users
	// bookings no matter the status
	@Test
	public void testGetCurrentBookings() {
		BookingController bookingController = new BookingController();
		
		// repo to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		
		//test start end datetimes
		LocalDateTime startTime0 = LocalDateTime.of(2026, 10, 1, 9, 0);
		LocalDateTime endTime0 = LocalDateTime.of(2026, 10, 2, 9, 0);
		
		LocalDateTime startTime1 = LocalDateTime.of(2026, 11, 1, 9, 0);
		LocalDateTime endTime1 = LocalDateTime.of(2026, 11, 7, 9, 0);
		
		LocalDateTime startTime2 = LocalDateTime.of(2026, 12, 1, 9, 0);
		LocalDateTime endTime2 = LocalDateTime.of(2026, 12, 10, 9, 0);
		
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		
		// test rooms
		Room room0 = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		Room room1 = new Room("roomTest1", "test", 0, 0, RoomStatus.AVAILABLE);
		Room room2 = new Room("roomTest2", "test", 0, 0, RoomStatus.AVAILABLE);
		
		Booking booking0 = null;
		Booking booking1 = null;
		Booking booking2 = null;
		
		try {
			// store account and room temporarily
			accountRepo.saveAccount(account);
			
			roomRepo.saveRoom(room0);
			roomRepo.saveRoom(room1);
			roomRepo.saveRoom(room2);
			
			// test bookings
			booking0 = bookingController.storeBooking(account, room0, startTime0, endTime0, BookingStatus.ACTIVE);
			booking1 = bookingController.storeBooking(account, room1, startTime1, endTime1, BookingStatus.ACTIVE);
			booking2 = bookingController.storeBooking(account, room2, startTime2, endTime2, BookingStatus.CANCELLED);
			
			// store all the actual bookings
			ArrayList<Booking> actualTestusersBookings = new ArrayList<Booking>();
			actualTestusersBookings.add(booking0);
			actualTestusersBookings.add(booking1);
			actualTestusersBookings.add(booking2);
			
			// get all bookings from our test user using booking controller getCurrentBookings method
			List<Booking> testusersBookings = bookingController.getCurrentBookings(account);
			
			// check if actualTestusersBookings and testusersBookings are holding the same bookings
			assertEquals(actualTestusersBookings.size(), testusersBookings.size());
			
			// check if bookingIds, usernames, and roomId are as expected
			for(int i = 0; i < testusersBookings.size(); i++) {
				assertEquals(actualTestusersBookings.get(i).getBookingId(), testusersBookings.get(i).getBookingId());
				assertEquals(actualTestusersBookings.get(i).getAccount().getUserName(), testusersBookings.get(i).getAccount().getUserName());
				assertEquals(actualTestusersBookings.get(i).getRoom().getRoomId(), testusersBookings.get(i).getRoom().getRoomId());
			}
			
		} 
		
		finally {
			// delete test room, account, booking
			bookingRepo.deleteBooking(booking0.getBookingId());
			bookingRepo.deleteBooking(booking1.getBookingId());
			bookingRepo.deleteBooking(booking2.getBookingId());

			roomRepo.deleteRoom("roomTest0");
			roomRepo.deleteRoom("roomTest1");
			roomRepo.deleteRoom("roomTest2");
			
			accountRepo.deleteUser("testusername");
		}
	}
	
	// test to make sure booking controller isRoomAvailable() method
	// properly returns true when there is no overlap between two bookings
	// of the same room
	@Test
	public void testIsRoomAvailableWithNoOverlap() {
		BookingController bookingController = new BookingController();
		
		// repo to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);

		Booking booking = null;

		try {
			// save account and room temporarily
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);

			// test start and end datetimes
			LocalDateTime bookingStart = LocalDateTime.of(2026, 12, 1, 9, 0);
			LocalDateTime bookingEnd = LocalDateTime.of(2026, 12, 1, 10, 0);
			
			// store booking into database temporarily
			booking = bookingController.storeBooking(account, room, bookingStart, bookingEnd, BookingStatus.ACTIVE);
			// test start and end datetimes to pass into the isRoomAvailable
			LocalDateTime requestedStart = LocalDateTime.of(2026, 12, 1, 11, 0);
			LocalDateTime requestedEnd = LocalDateTime.of(2026, 12, 1, 12, 0);
			// expect the method to return true as there is no active booking blocking that room between
			// the requested time
			assertEquals(true, bookingController.isRoomAvailable(room, requestedStart, requestedEnd));

		} 
		finally {
			// delete test booking, account, room
			bookingRepo.deleteBooking(booking.getBookingId());
			roomRepo.deleteRoom("roomTest0");
			accountRepo.deleteUser("testusername");
		}
	}
	
	/*
	 * test to make sure booking controller isRoomAvailable() method
	 * properly returns false when there is overlap between two bookings
	 * of the same room
	 */
	@Test
	public void testIsRoomAvailableWithOverlap() {
		BookingController bookingController = new BookingController();
		
		// repo to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);  
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);

		Booking booking = null;

		try {
			// store room and account temporarily
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			
			// test start and end datetimes
			LocalDateTime bookingStart = LocalDateTime.of(2026, 12, 1, 9, 0);
			LocalDateTime bookingEnd = LocalDateTime.of(2026, 12, 1, 10, 0);
			
			// store booking into database temporarily
			booking = bookingController.storeBooking(account, room, bookingStart, bookingEnd, BookingStatus.ACTIVE);    
			
			// test start and end datetimes to pass into the isRoomAvailable
			LocalDateTime requestedStart = LocalDateTime.of(2026, 12, 1, 9, 30);
			LocalDateTime requestedEnd = LocalDateTime.of(2026, 12, 1, 12, 0);
			// expect the method to return false as there is an active booking blocking that room between
			// the requested time
			assertEquals(false, bookingController.isRoomAvailable(room, requestedStart, requestedEnd));

		} 
		finally {
			// delete test booking, account, room
			bookingRepo.deleteBooking(booking.getBookingId());
			roomRepo.deleteRoom("roomTest0");
			accountRepo.deleteUser("testusername");
		}
	}
	
	/*
	 * test to make sure booking controller isRoomAvailable() method
	 * properly returns true when there is overlap between two bookings
	 * of the same room, but the overlapping booking is cancelled
	 */
	@Test
	public void testCancelledBookingDoesNotBlockRoom() {
		BookingController bookingController = new BookingController();
		
		// repo to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);  
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);

		Booking booking = null;
		try {
			// store room and account temporarily
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			
			// test start and end datetimes
			LocalDateTime bookingStart = LocalDateTime.of(2026, 12, 1, 9, 0);
			LocalDateTime bookingEnd = LocalDateTime.of(2026, 12, 1, 10, 0);
			
			// store booking into database temporarily with a cancelled status
			booking = bookingController.storeBooking(account, room, bookingStart, bookingEnd, BookingStatus.CANCELLED);    
			
			// test start and end datetimes to pass into the isRoomAvailable
			LocalDateTime requestedStart = LocalDateTime.of(2026, 12, 1, 9, 30);
			LocalDateTime requestedEnd = LocalDateTime.of(2026, 12, 1, 12, 0);
			
			// both actual and requested times over lap but it shouldnt matter because the booking
			// status is cancelled
			
			// expect the method to return true as there is no active booking blocking that room between
			// the requested time
			assertEquals(true, bookingController.isRoomAvailable(room, requestedStart, requestedEnd));

		} 
		finally {
			// delete test booking, account, room
			bookingRepo.deleteBooking(booking.getBookingId());
			roomRepo.deleteRoom("roomTest0");
			accountRepo.deleteUser("testusername");
		}
	}
	
	/*
	 * test extendBooking() method in booking controller.
	 * check to see if it returns true, updates the booking end time,
	 * and updates the final cost of the booking
	 */
	@Test
	public void testExtendBookingSuccessfully() {
		BookingController bookingController = new BookingController();
		
		// repos to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);

		Booking booking = null;

		try {
			// store account and room temporarily
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);

			// start and end datetimes for booking
			LocalDateTime startTime = LocalDateTime.of(2026, 12, 1, 9, 0);
			LocalDateTime endTime = LocalDateTime.of(2026, 12, 1, 10, 0);
			// store booking
			booking = bookingController.storeBooking(account, room, startTime, endTime, BookingStatus.ACTIVE);

			double originalCost = booking.getFinalCost();

			boolean result = bookingController.extendBooking(booking, 2);

			// check extendBooking result to make sure it returns true
			assertEquals(true, result);
			// check that the booking end time got extended
			assertEquals(endTime.plusHours(2), booking.getEndTime());
			// check that the original cost got updated
			assertEquals(originalCost + 40.00, booking.getFinalCost(), 0.001);

		} 
		finally {
			// delete test booking, room, account
			bookingRepo.deleteBooking(booking.getBookingId());
			roomRepo.deleteRoom("roomTest0");
			accountRepo.deleteUser("testusername");
		}
	}
	
	/*
	 * test cancelBooking() method in booking controller.
	 * check to see if it returns true and updates the booking 
	 * status to canceled
	 */
	@Test
	public void testCancelActiveFutureBooking() {
		BookingController bookingController = new BookingController();
		
		// repos to access database and helper methods
		BookingRepository bookingRepo = new BookingRepository();
		RoomRepository roomRepo = new RoomRepository();
		AccountRepository accountRepo = new AccountRepository();
		// test account
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
	
		Booking booking = null;
		
		try {
			// store account and room temporarily
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			// test start and end times relative to todays date
			LocalDateTime startTime = LocalDateTime.now().plusDays(10);
			LocalDateTime endTime = startTime.plusHours(2);
			// store test booking
			booking = bookingController.storeBooking(account, room, startTime, endTime, BookingStatus.ACTIVE);

			boolean result = bookingController.cancelBooking(booking);

			// test to see is cancelBooking() returns true
			assertEquals(true, result);
			// check that the booking status got updated to cancelled
			assertEquals(BookingStatus.CANCELLED, booking.getStatus());

		}
		finally {
			// delete test booking, room, account
			bookingRepo.deleteBooking(booking.getBookingId());
			roomRepo.deleteRoom("roomTest0");
			accountRepo.deleteUser("testusername");
		}
	}
	
	
	/*
	 * test isRoomAvailable() with invalid arguments...
	 * null in 3 different places
	 * startTime being before endTime
	 * two start times
	 */
	@Test
	public void testIsRoomAvailableWithInvalidArguments() {
		BookingController bookingController = new BookingController();
		// test room
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		// invalid test start and end datetimes
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 1, 10, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 1, 9, 0);
		// check that isRoomAvailable() returns false on all invalid arguments
		assertEquals(false, bookingController.isRoomAvailable(null, startTime, endTime));
		assertEquals(false, bookingController.isRoomAvailable(room, null, endTime));
		assertEquals(false, bookingController.isRoomAvailable(room, startTime, null));
		assertEquals(false, bookingController.isRoomAvailable(room, startTime, endTime));
		assertEquals(false, bookingController.isRoomAvailable(room, startTime, startTime));
	}
	
	
}
