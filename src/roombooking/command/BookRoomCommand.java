package roombooking.command;

import users.Booking;
import users.RegisteredUser;
import users.BookingStatus;
import roombooking.service.BookingService;
import roombooking.observer.NotificationService;

import java.time.LocalDateTime;
import java.time.Duration;

public class BookRoomCommand implements Command {
    private int userId;
    private int roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingService bookingService;
    private Booking createdBooking;
    private double depositAmount;
    private double totalCost;
    private boolean executed = false;
    
    public BookRoomCommand(int userId, int roomId, 
                          LocalDateTime startTime, LocalDateTime endTime,
                          BookingService bookingService) {
        this.userId = userId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingService = bookingService;
    }
    
    @Override
    public boolean execute() {
        try {
            RegisteredUser user = bookingService.getUser(userId);
            if (user == null) {
                return false;
            }
            
            if (!bookingService.isRoomAvailable(roomId, startTime, endTime)) {
                return false;
            }
            
            double rate = user.getRate();
            long hours = Duration.between(startTime, endTime).toHours();
            if (hours <= 0) hours = 1;
            totalCost = rate * hours;
            depositAmount = rate * 1;
            
            boolean paymentSuccess = bookingService.processPayment(userId, depositAmount);
            if (!paymentSuccess) {
                return false;
            }
            
            createdBooking = bookingService.bookRoom(userId, roomId, startTime, endTime, depositAmount);
            createdBooking.setFinalCost(totalCost);
            bookingService.updateBooking(createdBooking);
            executed = true;
            
            NotificationService.getInstance().sendNotification(
                "Booking confirmed: " + createdBooking.getBookingID()
            );
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean undo() {
        if (createdBooking == null) {
            return false;
        }
        
        boolean success = bookingService.cancelBooking(createdBooking.getBookingID());
        if (success) {
            bookingService.processRefund(userId, depositAmount);
            NotificationService.getInstance().sendNotification(
                "Booking undone: " + createdBooking.getBookingID()
            );
        }
        return success;
    }
    
    @Override
    public String getCommandType() {
        return "BOOK_ROOM";
    }
    
    public boolean isExecuted() {
        return executed;
    }
    
    public Booking getCreatedBooking() {
        return createdBooking;
    }
    
    public double getDepositAmount() {
        return depositAmount;
    }
}
