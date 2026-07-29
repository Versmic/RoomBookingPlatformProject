package roombooking.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import roombooking.enums.AccountType;
import roombooking.enums.BookingStatus;
import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;
import roombooking.repository.BookingRepository;
import roombooking.repository.RoomRepository;

public class BookingController {

    private final RoomRepository roomRepository = new RoomRepository();
    private final BookingRepository bookingRepository = new BookingRepository();

    // returns rooms available during the selected period
    public List<Room> getAvailableRooms() { 
    	List<Room> availableRooms = new ArrayList<>(); 
    	for (Room room : roomRepository.getAllRooms()) { 
    		if (room.getStatus() == RoomStatus.AVAILABLE) { 
    			availableRooms.add(room); 
    		} 
    	} 
    	return availableRooms; 
    }
    
    public double calculateFinalCost(Account account, LocalDateTime startTime, LocalDateTime endTime) {
    	double hourlyRate = account.getRegisteredUser().getHRate();
    	long hours = Duration.between(startTime, endTime).toHours();
    	return hours * hourlyRate; 
    	
    }
    
    public double calculateInitialDeposit(Account account) {
    	return account.getRegisteredUser().getHRate();
    }
 
    // creates and saves a new booking
    public Booking storeBooking(Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, BookingStatus status) {

        double hourlyRate = getHourlyRate(account.getAccountType());
        double bookingHours = calculateBookingHours(startTime, endTime);
        double depositAmount = hourlyRate;
        double finalCost = hourlyRate * bookingHours;

        Booking booking = new Booking(
                bookingRepository.generateBookingId(), account,
                room, startTime, endTime, depositAmount, finalCost, status);

        bookingRepository.saveBooking(booking);
        account.addBooking(booking);
        room.addBooking(booking);

        return booking;
    }

    // calculates the rate for the account type
    private double getHourlyRate(AccountType accountType) {
        return switch (accountType) {
            case STUDENT -> 20.00;
            case FACULTY -> 30.00;
            case STAFF -> 40.00;
            case PARTNER -> 50.00;
            case CHEIF -> 0;
            case ADMIN -> 0;
        };
    }
    

    // calculates booking length in hours
    private double calculateBookingHours(LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = Duration.between(startTime, endTime).toMinutes();
        return minutes / 60.0;
    }
    
	 // returns the users bookings
	 public List<Booking> getCurrentBookings(Account account) {
	     List<Booking> currentBookings = new ArrayList<>();
	     for (Booking booking : bookingRepository.getAllBookings()) {
	         boolean belongsToUser = booking.getAccount().getUserName().equalsIgnoreCase(account.getUserName());
	         if (belongsToUser) currentBookings.add(booking);
	      }
	     return currentBookings;
	 }
	// checks if a room has no overlapping bookings during the selected time
	 public boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime) {
	     if (room == null || startTime == null || endTime == null || !endTime.isAfter(startTime)) {
	         return false;
	     }

	     for (Booking booking : bookingRepository.getAllBookings()) {
	         boolean sameRoom = booking.getRoom().getRoomId().equalsIgnoreCase(room.getRoomId());
	         boolean overlaps = booking.getStartTime().isBefore(endTime) && booking.getEndTime().isAfter(startTime);

	         if (sameRoom && overlaps) {
	             return false;
	         }
	     }

	     return true;
	 }
	 
	 public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
		    List<Room> availableRooms = new ArrayList<>();

		    for (Room room : roomRepository.getAllRooms()) {
		        if (isRoomAvailable(room, startTime, endTime)) {
		            availableRooms.add(room);
		        }
		    }

		    return availableRooms;
	}
	 
	 public boolean extendBooking(Booking booking, int additionalHours) {
		    if (booking == null || additionalHours <= 0) {
		        return false;
		    }

		    LocalDateTime currentEndTime = booking.getEndTime();
		    LocalDateTime newEndTime = currentEndTime.plusHours(additionalHours);

		    for (Booking existingBooking : bookingRepository.getAllBookings()) {
		        boolean sameRoom = existingBooking.getRoom().getRoomId().equalsIgnoreCase(booking.getRoom().getRoomId());
		        boolean differentBooking = !existingBooking.getBookingId().equalsIgnoreCase(booking.getBookingId());
		        boolean overlaps = existingBooking.getStartTime().isBefore(newEndTime) && existingBooking.getEndTime().isAfter(currentEndTime);

		        if (sameRoom && differentBooking && overlaps) {
		            return false;
		        }
		    }

		    double extensionCost = additionalHours * booking.getAccount().getRegisteredUser().getHRate();

		    booking.setEndTime(newEndTime);
		    booking.setFinalCost(booking.getFinalCost() + extensionCost);
		    bookingRepository.updateBooking(booking);

		    return true;
		}

}