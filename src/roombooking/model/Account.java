package roombooking.model;

import java.util.ArrayList;
import java.util.List;

import roombooking.enums.AccountType;

public class Account {
	
    private String userName;
    private String password;
    private String email;
    private AccountType accountType;
    private RegisteredUser registeredUser;
    
    private List<Booking> bookings = new ArrayList<>();
    
    public Account(String userName, String password, String email, AccountType accountType, RegisteredUser registeredUser) {
    	this.userName = userName;
    	this.password = password;
    	this.email = email;
    	this.accountType = accountType;
    	this.registeredUser = registeredUser;
    }
 // getter and setter methods 

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

}
