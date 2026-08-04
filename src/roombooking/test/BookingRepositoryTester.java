package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

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

public class BookingRepositoryTester {
	
	/*
	 * this test ensures that saveBooking() saves a booking
	 * to our database and saves the correct information
	 */
	@Test
	public void saveBooking() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		
		
		
		int initialBookingsAmount = bookingRepo.getAllBookings().size();
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(booking);
			List<Booking> currentBookings = bookingRepo.getAllBookings();
			assertEquals(initialBookingsAmount + 1, currentBookings.size());
			assertEquals("testBooking", currentBookings.get(currentBookings.size() - 1).getBookingId());
			assertEquals("testuser", currentBookings.get(currentBookings.size() - 1).getAccount().getUserName());
			assertEquals("testRoom", currentBookings.get(currentBookings.size() - 1).getRoom().getRoomId());
			assertEquals(startTime, currentBookings.get(currentBookings.size() - 1).getStartTime());
			assertEquals(endTime, currentBookings.get(currentBookings.size() - 1).getEndTime());
		}
		
		finally {
			bookingRepo.deleteBooking(booking.getBookingId());	
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
		
		
	}
	
	/*
	 * this test ensures that saving a null booking
	 * does not add anything to the booking database
	 */
	@Test
	public void saveNullBooking() {
		BookingRepository bookingRepo = new BookingRepository();
		Booking booking = null;
		int initialBookingAmount = bookingRepo.getAllBookings().size();
		bookingRepo.saveBooking(booking);
		assertEquals(initialBookingAmount, bookingRepo.getAllBookings().size());
	
	}
	
	
	/*
	 * this test ensures that saving a duplicate booking
	 * throws an IllegalArgumentException
	 */
	@Test(expected = IllegalArgumentException.class)
	public void saveDuplicateBooking() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(booking);
			bookingRepo.saveBooking(booking);
		}
		
		finally {
			bookingRepo.deleteBooking(booking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}
	
	
	
	/*
	 * this test ensures that updating a booking
	 * properly updates it in the database
	 */
	@Test
	public void updateBooking() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(booking);
			
			LocalDateTime updatedStartTime = LocalDateTime.of(2027, 1, 5, 10, 0);
			LocalDateTime updatedEndTime = LocalDateTime.of(2027, 1, 5, 12, 0);
		
			booking = new Booking("testBooking", account, room, updatedStartTime, updatedEndTime, 20, 40, BookingStatus.COMPLETED);
			
			bookingRepo.updateBooking(booking);
			
			Booking updatedBooking = bookingRepo.findBookingById(booking.getBookingId());
			
			assertEquals("testBooking", updatedBooking.getBookingId());
			assertEquals(account.getUserName(), updatedBooking.getAccount().getUserName());
			assertEquals(room.getRoomId(), updatedBooking.getRoom().getRoomId());
			assertEquals(updatedStartTime, updatedBooking.getStartTime());
			assertEquals(updatedEndTime, updatedBooking.getEndTime());
			assertEquals(BookingStatus.COMPLETED, updatedBooking.getStatus());
		}
		
		finally {
			bookingRepo.deleteBooking(booking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}
	
	
	
	
	/*
	 * this test ensures that deleteBooking() correctly
	 * deletes the proper booking from our database
	 */
	@Test
	public void deleteBooking() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		
		int initialBookingsAmount = bookingRepo.getAllBookings().size();
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(booking);
			bookingRepo.deleteBooking(booking.getBookingId());
			
			List<Booking> currentBookings = bookingRepo.getAllBookings();
			
			assertEquals(initialBookingsAmount, currentBookings.size());
			assertEquals(null, bookingRepo.findBookingById(booking.getBookingId()));
		}
		
		finally {
			bookingRepo.deleteBooking(booking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}
	
	
	
	/*
	 * this test ensures that findBookingById() correctly
	 * finds the proper booking from our database
	 */
	@Test
	public void findBooking() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(booking);
			
			Booking foundBooking = bookingRepo.findBookingById(booking.getBookingId());
			
			assertEquals(booking.getBookingId(), foundBooking.getBookingId());
			assertEquals(booking.getAccount().getUserName(), foundBooking.getAccount().getUserName());
			assertEquals(booking.getRoom().getRoomId(), foundBooking.getRoom().getRoomId());
			assertEquals(booking.getStartTime(), foundBooking.getStartTime());
			assertEquals(booking.getEndTime(), foundBooking.getEndTime());
			assertEquals(booking.getStatus(), foundBooking.getStatus());
		}
		
		finally {
			bookingRepo.deleteBooking(booking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}
	
	
	
	/*
	 * this test ensures that findBookingById() correctly
	 * returns null when we pass a null id
	 */
	@Test
	public void findNullBooking() {
		BookingRepository bookingRepo = new BookingRepository();
		assertEquals(null, bookingRepo.findBookingById(null));
	}
	
	
	/*
	 * this test ensures that findBookingById() correctly
	 * returns null when we pass an id that DNE
	 */
	@Test
	public void findDNEBooking() {
		BookingRepository bookingRepo = new BookingRepository();
		assertEquals(null, bookingRepo.findBookingById("!mkjfso0292@#!"));
	}
	
	
	/*
	 * this test ensures that getAllBookings() correctly
	 * returns a list of all bookings from our database
	 */
	@Test
	public void getAllBookings() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		
		int initialBookingsAmount = bookingRepo.getAllBookings().size();
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(booking);
			
			assertEquals(initialBookingsAmount + 1, bookingRepo.getAllBookings().size());
		}
		
		finally {
			bookingRepo.deleteBooking(booking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}
	
	
	
	/*
	 * this test ensures that findBookingsByRoomId()
	 * returns all bookings belonging to the correct room
	 */
	@Test
	public void findBookingsByRoomId() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime firstStartTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime firstEndTime = LocalDateTime.of(2026, 12, 25, 11, 0);
		
		LocalDateTime secondStartTime = LocalDateTime.of(2026, 12, 26, 9, 0);
		LocalDateTime secondEndTime = LocalDateTime.of(2026, 12, 26, 11, 0);
		
		Booking firstBooking = new Booking("testBookingOne", account, room, firstStartTime, firstEndTime, 0, 0, BookingStatus.ACTIVE);
		
		Booking secondBooking = new Booking("testBookingTwo", account, room, secondStartTime, secondEndTime, 0, 0, BookingStatus.ACTIVE);
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(firstBooking);
			bookingRepo.saveBooking(secondBooking);
			
			List<Booking> foundBookings = bookingRepo.findBookingsByRoomId(room.getRoomId());
			
			assertEquals(2, foundBookings.size());
			assertEquals("testRoom", foundBookings.get(0).getRoom().getRoomId());
			assertEquals("testRoom", foundBookings.get(1).getRoom().getRoomId());
		}
		
		finally {
			bookingRepo.deleteBooking(firstBooking.getBookingId());
			bookingRepo.deleteBooking(secondBooking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}
	
	
	
	/*
	 * this test ensures that findBookingsByRoomId()
	 * returns an empty list
	 */
	@Test
	public void findNullBookingsByRoomId() {
		
		BookingRepository bookingRepo = new BookingRepository();

		assertEquals(0, bookingRepo.findBookingsByRoomId(null).size());
	}
	
	
	
	/*
	 * this test ensures that deleteNullBooking()
	 * does not delete anything when passed null
	 */
	@Test
	public void deleteNullBooking() {
		
		BookingRepository bookingRepo = new BookingRepository();
		int initialSize = bookingRepo.getAllBookings().size();
		bookingRepo.deleteBooking(null);
		assertEquals(bookingRepo.getAllBookings().size(), initialSize);
	}
	
	
	
	/*
	 * this test ensures that findBookingsByUsername()
	 * returns all bookings belonging to the correct account
	 */
	@Test
	public void findBookingsByUsername() {
		
		BookingRepository bookingRepo = new BookingRepository();
		AccountRepository accountRepo = new AccountRepository();
		RoomRepository roomRepo = new RoomRepository();
		
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		LocalDateTime firstStartTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime firstEndTime = LocalDateTime.of(2026, 12, 25, 11, 0);
		
		LocalDateTime secondStartTime = LocalDateTime.of(2026, 12, 26, 9, 0);
		LocalDateTime secondEndTime = LocalDateTime.of(2026, 12, 26, 11, 0);
		
		Booking firstBooking = new Booking("testBookingOne", account, room, firstStartTime, firstEndTime, 0, 0, BookingStatus.ACTIVE);
		
		Booking secondBooking = new Booking("testBookingTwo", account, room, secondStartTime, secondEndTime,0, 0, BookingStatus.ACTIVE);
		
		try {
			accountRepo.saveAccount(account);
			roomRepo.saveRoom(room);
			bookingRepo.saveBooking(firstBooking);
			bookingRepo.saveBooking(secondBooking);
			
			List<Booking> foundBookings = bookingRepo.findBookingsByUsername(account.getUserName());
			
			assertEquals(2, foundBookings.size());
			assertEquals("testuser", foundBookings.get(0).getAccount().getUserName());
			assertEquals("testuser", foundBookings.get(1).getAccount().getUserName());
		}
		
		finally {
			bookingRepo.deleteBooking(firstBooking.getBookingId());
			bookingRepo.deleteBooking(secondBooking.getBookingId());
			accountRepo.deleteUser(account.getUserName());
			roomRepo.deleteRoom(room.getRoomId());
		}
	}

	
}
