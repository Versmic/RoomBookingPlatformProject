package users;

import java.util.ArrayList;
import java.util.List;

// superclass for authenticated users in the system (Student, Faculty, Partner, Staff, Administrator)
public abstract class RegisteredUser extends User {

    private int userID;
    private String fullName;
    private String phoneNumber;
    private Account account;

    private List<Booking> bookings = new ArrayList<>();

    protected RegisteredUser(int userID, String fullName, String phoneNumber, Account account) {
        this.userID = userID;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.account = account;
    }
    
    // getter and setter methods 
    public int getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Account getAccount() {
        return account;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }


    // update any profile changes (fullName, phoneNumber, ...) 
    public void updateProfile() {
        // TODO: persist changes via the Account / a repository once available.
    }

    
    // hourly rate to calculate costs, each role has its own rate
    // admins usually override with fixed rate (0.0) 
    public abstract double getHRate();

    
    @Override
    public List<Room> searchAvailableRooms() {
        throw new UnsupportedOperationException("Wire RegisteredUser to a BookingController");
    }
}