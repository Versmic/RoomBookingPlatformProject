package roombooking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roombooking.enums.AccountType;

public class Account {

    private String userName;
    private String password;
    private String email;
    private AccountType accountType;
    private RegisteredUser registeredUser;

    private final List<Booking> bookings = new ArrayList<>();

    public Account(String userName, String password, String email, AccountType accountType, RegisteredUser registeredUser) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.accountType = accountType;
        this.registeredUser = registeredUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
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
}