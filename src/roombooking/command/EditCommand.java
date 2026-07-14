package roombooking.command;

import roombooking.model.Booking;
import roombooking.model.User;
import roombooking.service.BookingService;
import roombooking.observer.NotificationService;

import java.time.LocalDateTime;
import java.time.Duration;

public class EditCommand implements Command {
    private int bookingId;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;
    private BookingService bookingService;
    private Booking booking;
    private LocalDateTime oldStartTime;
    private LocalDateTime oldEndTime;
    private double oldTotalCost;
    private double priceDifference;
    private boolean executed = false;
    
    public EditCommand(int bookingId, LocalDateTime newStartTime, 
                      LocalDateTime newEndTime, BookingService bookingService) {
        this.bookingId = bookingId;
        this.newStartTime = newStartTime;
        this.newEndTime = newEndTime;
        this.bookingService = bookingService;
    }
    
    @Override
    public boolean execute() {
        booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(booking.getStartTime())) {
            return false;
        }
        
        oldStartTime = booking.getStartTime();
        oldEndTime = booking.getEndTime();
        oldTotalCost = booking.getTotalCost();
        
        if (!bookingService.isRoomAvailable(booking.getRoomId(), newStartTime, newEndTime)) {
            return false;
        }
        
        User user = bookingService.getUser(booking.getUserId());
        long hours = Duration.between(newStartTime, newEndTime).toHours();
        if (hours <= 0) hours = 1;
        double newTotalCost = user.getRate() * hours;
        priceDifference = newTotalCost - oldTotalCost;
        
        if (priceDifference > 0) {
            if (!bookingService.processPayment(booking.getUserId(), priceDifference)) {
                return false;
            }
        } else if (priceDifference < 0) {
            bookingService.processRefund(booking.getUserId(), Math.abs(priceDifference));
        }
        
        booking.setStartTime(newStartTime);
        booking.setEndTime(newEndTime);
        booking.setTotalCost(newTotalCost);
        bookingService.updateBooking(booking);
        executed = true;
        
        NotificationService.getInstance().sendNotification(
            "Booking edited: " + bookingId
        );
        
        return true;
    }
    
    @Override
    public boolean undo() {
        if (booking == null) {
            return false;
        }
        
        booking.setStartTime(oldStartTime);
        booking.setEndTime(oldEndTime);
        booking.setTotalCost(oldTotalCost);
        bookingService.updateBooking(booking);
        
        if (priceDifference > 0) {
            bookingService.processRefund(booking.getUserId(), priceDifference);
        } else if (priceDifference < 0) {
            bookingService.processPayment(booking.getUserId(), Math.abs(priceDifference));
        }
        
        return true;
    }
    
    @Override
    public String getCommandType() {
        return "EDIT_BOOKING";
    }
    
    public boolean isExecuted() {
        return executed;
    }
}
