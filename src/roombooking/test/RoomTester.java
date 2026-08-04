package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;
import roombooking.model.Student;

public class RoomTester {
	
	
	/*
	 * this test checks that the Room constructor
	 * correctly stores the roomId
	 */
	@Test
	public void testRoomId() {
		Room room = new Room("testRoom", null, 0, 0, null);
		
		assertEquals("testRoom", room.getRoomId());
	}
	
	
	/*
	 * this test checks that the Room constructor
	 * correctly stores the building name
	 */
	@Test
	public void testBuildingName() {
		Room room = new Room(null, "testRoomName", 0, 0, null);
		
		assertEquals("testRoomName", room.getBuildingName());
	}
	
	
	/*
	 * this test checks that the Room constructor
	 * correctly stores the room number
	 */
	@Test
	public void testRoomNumber() {
		Room room = new Room(null, null, 21, 0, null);
		
		assertEquals(21, room.getRoomNumber());
	}
	
	
	/*
	 * this test checks that the Room constructor
	 * correctly stores the capacity
	 */
	@Test
	public void testCapacity() {
		Room room = new Room(null, null, 0, 19, null);
		
		assertEquals(19, room.getCapacity());
	}
	
	/*
	 * this test checks that the Room constructor
	 * correctly stores the room status
	 */
	@Test
	public void testRoomStatus() {
		Room room = new Room(null, null, 0, 0, RoomStatus.MAINTENANCE);
		
		assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
	}
	
	
	
	
	/*
	 * this test checks that the Room class
	 * initially holds a empty bookings list
	 */
	@Test
	public void testEmptyInitialBooking() {
		Room room = new Room(null, null, 0, 0, null);
		
		assertEquals(room.getBookings().size(), 0);
	}
	
	
	
	
	
	
	/*
	 * this test ensures that calling addBooking() adds a
	 * booking to the rooms booking list. Note* the method itself does not do 
	 * validation checking (besides null and duplicate checking)
	 * because the gui guides the user to valid inputs only
	 */
	@Test
	public void testAddBooking() {
		Room room = new Room("testRoom", "testRoomName", 0, 0, null);
		
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 25, 11, 0);
		
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 20.00, 40.00, BookingStatus.ACTIVE);
		
		
		room.addBooking(booking);
		
		assertEquals(room.getBookings().size(), 1);
		assertEquals(room.getBookings().get(0), booking);
	}
	
	/*
	 * this test ensures that calling addBooking() on a null
	 * booking to the rooms booking list does not add it
	 */
	@Test
	public void testAddNullBooking() {
		Room room = new Room("testRoom", "testRoomName", 0, 0, null);
		
		Booking booking = null;
		
		
		room.addBooking(booking);
		
		assertEquals(room.getBookings().size(), 0);
	}
	
	
	
	/*
	 * this test ensures that calling addBooking() on a duplicate
	 * booking to the rooms booking list does not add it
	 */
	@Test
	public void testAddDuplicateBooking() {
		Room room = new Room("testRoom", "testRoomName", 0, 0, null);
		
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 25, 11, 0);
		
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 20.00, 40.00, BookingStatus.ACTIVE);
		
		
		room.addBooking(booking);
		room.addBooking(booking);
		
		assertEquals(room.getBookings().size(), 1);
		assertEquals(room.getBookings().get(0), booking);
	}
	
	
	
	
	
	/*
	 * this test ensures that calling removeBooking() on a 
	 * booking to the rooms booking list removes it
	 */
	@Test
	public void testRemoveBooking() {
		Room room = new Room("testRoom", "testRoomName", 0, 0, null);
		
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 25, 11, 0);
		
		
		Booking booking = new Booking("testBooking", account, room, startTime, endTime, 20.00, 40.00, BookingStatus.ACTIVE);
		
		
		room.addBooking(booking);
		room.removeBooking(booking);
		
		assertEquals(room.getBookings().size(), 0);
	}
	
	
	
	
	


}
