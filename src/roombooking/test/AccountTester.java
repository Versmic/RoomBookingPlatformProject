package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.*;


public class AccountTester {
	
	/*
	 * this test ensures that the Account constructor
	 * stores all account information correctly
	 */
	@Test
	public void testAccountConstructor() {
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);

		assertEquals("testuser", account.getUserName());
		assertEquals("password", account.getPassword());
		assertEquals("test@test.com", account.getEmail());
		assertEquals(AccountType.STUDENT, account.getAccountType());
		assertEquals(student, account.getRegisteredUser());
	}
	
	
	/*
	 * this test ensures that usernames are converted
	 * to lowercase when an account is created
	 */
	@Test
	public void testConstructorConvertsUsernameToLowercase() {
		Student student = new Student("test");
		Account account = new Account("TestUser", "password", "test@test.com", AccountType.STUDENT, student);

		assertEquals("testuser", account.getUserName());
	}
	
	
	
	/*
	 * this test ensures that setUsername
	 * properly takes a username, puts it to lowercase
	 * and updates
	 */
	@Test
	public void testSetUserName() {
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);

		account.setUserName("updatedUser");

		assertEquals("updateduser", account.getUserName());
	}
	
	
	
	/*
	 * this test ensures that setEmail
	 * properly updates the email
	 */
	@Test
	public void testSetEmail() {
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);

		account.setEmail("updatedEmail@test.com");

		assertEquals("updatedEmail@test.com", account.getEmail());
	}
	

	/*
	 * this test ensures that a new account
	 * starts with an empty booking list
	 */
	@Test
	public void testNewAccountHasNoBookings() {
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);

		assertEquals(0, account.getBookings().size());
	}
	
	
	
	/*
	 * this test ensures that addBooking
	 * properly adds a booking to the account
	 */
	@Test
	public void testAddBooking() {
		// create tester account, room, and dates
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 31, 17, 30);
		// create test booking
		Booking booking = new Booking("testbooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		// add booking to the account
		account.addBooking(booking);

		assertEquals(1, account.getBookings().size());
		assertEquals(booking, account.getBookings().get(0));
	}
	
	
	
	/*
	 * this test ensures that addBooking
	 * does not add the same booking twice
	 */
	@Test
	public void testAddDuplicateBooking() {
		// create tester account, room, and dates
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 31, 17, 30);
		// create test booking
		Booking booking = new Booking("testbooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		// add booking to the account
		account.addBooking(booking);
		account.addBooking(booking);

		assertEquals(1, account.getBookings().size());
	}
	
	
	
	/*
	 * this test ensures that addBooking
	 * does not add a null booking
	 */
	@Test
	public void testAddNullBooking() {
		// create tester account, room, and dates
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);

		account.addBooking(null);

		assertEquals(0, account.getBookings().size());
	}
	
	
	
	/*
	 * this test ensures that removeBooking
	 * properly removes an existing booking
	 */
	@Test
	public void testRemoveBooking() {
		// create tester account, room, and dates
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 31, 17, 30);
		// create test booking
		Booking booking = new Booking("testbooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		// add booking to the account
		account.addBooking(booking);
		// remove booking from account
		account.removeBooking(booking);

		assertEquals(0, account.getBookings().size());
	}
	
	
	/*
	 * this test ensures that removeBooking
	 * does not affect the booking list when
	 * the booking does not exist
	 */
	@Test
	public void testRemoveBookingThatDoesNotExist() {
		// create tester account, room, and dates
		Student student = new Student("test");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		Room room = new Room("roomTest0", "test", 0, 0, RoomStatus.AVAILABLE);
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 31, 17, 30);
		
		LocalDateTime startTime2 = LocalDateTime.of(2027, 12, 25, 9, 0);
		LocalDateTime endTime2 = LocalDateTime.of(2027, 12, 31, 17, 30);
		// create test bookings
		Booking booking = new Booking("testbooking", account, room, startTime, endTime, 0, 0, BookingStatus.ACTIVE);
		Booking booking2 = new Booking("testbooking2", account, room, startTime2, endTime2, 0, 0, BookingStatus.ACTIVE);
		// add booking to the account
		account.addBooking(booking);
		
		// booking1 was never added
		account.removeBooking(booking2);
		
		
		assertEquals(1, account.getBookings().size());
		assertEquals(booking, account.getBookings().get(0));
	}
	
	
	
	
	
	
	
	

}
