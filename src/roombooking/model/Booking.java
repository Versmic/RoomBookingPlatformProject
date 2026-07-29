package roombooking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import roombooking.enums.BookingStatus;

public class Booking {

    private String bookingId;
    private Account account;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double depositAmount;
    private double finalCost;
    private BookingStatus status;
    private ArrayList<Payment> payments;

    public Booking(String bookingId, Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, double depositAmount, double finalCost, BookingStatus status) {
        this.bookingId = bookingId;
        this.account = account;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.depositAmount = depositAmount;
        this.finalCost = finalCost;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Account getAccount() {
        return account;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}