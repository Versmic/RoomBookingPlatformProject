package roombooking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roombooking.enums.RoomStatus;

public class Room {

    private String roomId;
    private String buildingName;
    private int roomNumber;
    private int capacity;
    private RoomStatus status;

    private final List<Booking> bookings = new ArrayList<>();

    public Room(String roomId, String buildingName, int roomNumber, int capacity, RoomStatus status) {
        this.roomId = roomId;
        this.buildingName = buildingName;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }
    
    public void addBooking(Booking booking) {
        if (booking != null && !bookings.contains(booking)) {
            bookings.add(booking);
        }
    }
    
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
    
    public void setStatus(RoomStatus status) {
        this.status = status;
    }
    
    public String getBuildingName() {
    	return this.buildingName;
    }
}