package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.*;

public class BookingTester {
	
	
	/*
	 * this test ensures that when creating a 
	 * new booking object, our bookingId 
	 * is correctly set
	 */
	@Test
	public void testBookingId() {
		Booking booking = new Booking("test", null, null, null, null, 0, 0, BookingStatus.ACTIVE);
		
		assertEquals(booking.getBookingId(), "test");
	}
	
	/*
	 * this test ensures that when creating a 
	 * new booking object, our account object
	 * is correctly set
	 */
	@Test
	public void testBookingAccount() {
		Student student = new Student("0");
		Account account = new Account("testusername", "test", "test@gmail.com", AccountType.STUDENT, student);
		
		Booking booking = new Booking("test", account, null, null, null, 0, 0, BookingStatus.ACTIVE);
		
		assertEquals(booking.getAccount(), account);
	}
	
	
	/*
	 * this test ensures that when creating a
	 * new booking object, our room object
	 * is correctly set
	 */
	@Test
	public void testBookingRoom() {
		Room room = new Room("roomTest", "test", 0, 0, RoomStatus.AVAILABLE);
		
		Booking booking = new Booking("test", null, room, null, null, 0, 0, BookingStatus.ACTIVE);
		
		assertEquals(room, booking.getRoom());
	}
	
	
	
	/*
	 * this test ensures that when creating a
	 * new booking object, our start time
	 * is correctly set
	 */
	@Test
	public void testBookingStartTime() {
		LocalDateTime startTime = LocalDateTime.of(2026, 12, 25, 9, 0);
		
		Booking booking = new Booking("test", null, null, startTime, null, 0, 0, BookingStatus.ACTIVE);
		
		assertEquals(startTime, booking.getStartTime());
		
	}
	
	/*
	 * this test ensures that when creating a
	 * new booking object, our end time
	 * is correctly set
	 */
	@Test
	public void testBookingEndTime() {
		LocalDateTime endTime = LocalDateTime.of(2026, 12, 30, 11, 0);
		
		Booking booking = new Booking("test", null, null, null, endTime, 0, 0, BookingStatus.ACTIVE);
		
		assertEquals(endTime, booking.getEndTime());
	}
	
	
	
	/*
	 * this test ensures that when creating a
	 * new booking object, our deposit amount
	 * is correctly set
	 */
	@Test
	public void testBookingDepositAmount() {
		Booking booking = new Booking("test", null, null, null, null, 20.00, 0, BookingStatus.ACTIVE);
		
		assertEquals(20.00, booking.getDepositAmount(), 0.001);
	}
	
	
	/*
	 * this test ensures that when creating a
	 * new booking object, our final cost
	 * is correctly set
	 */
	@Test
	public void testBookingFinalCost() {
		Booking booking = new Booking("test", null, null, null, null, 0, 40.00, BookingStatus.ACTIVE);
		
		assertEquals(40.00, booking.getFinalCost(), 0.001);
	}
	
	
	
	
	/*
	 * this test ensures that setEndTime
	 * correctly updates the booking end time
	 */
	@Test
	public void testSetEndTime() {
		LocalDateTime originalEndTime = LocalDateTime.of(2026, 12, 1, 11, 0);
		LocalDateTime newEndTime = LocalDateTime.of(2026, 12, 1, 13, 30);
		
		Booking booking = new Booking("test", null, null, null, originalEndTime, 0, 0, BookingStatus.ACTIVE);
		
		booking.setEndTime(newEndTime);
		
		assertEquals(newEndTime, booking.getEndTime());
	}
	
	
	
	/*
	 * this test ensures that setFinalCost
	 * correctly updates the booking final cost
	 */
	@Test
	public void testSetFinalCost() {
		Booking booking = new Booking("test", null, null, null, null, 0, 40.00, BookingStatus.ACTIVE);
		
		booking.setFinalCost(75.50);
		
		assertEquals(75.50, booking.getFinalCost(), 0.001);
	}
	
	
	
	
	/*
	 * this test ensures that setStatus
	 * correctly updates the booking status
	 */
	@Test
	public void testSetStatus() {
		Booking booking = new Booking("test", null, null, null, null, 0, 0, BookingStatus.ACTIVE);
		
		booking.setStatus(BookingStatus.CANCELLED);
		
		assertEquals(BookingStatus.CANCELLED, booking.getStatus());
	}
	
	
	
}
