package roombooking.command;

import roombooking.model.Booking;
import roombooking.model.User;
import roombooking.service.BookingService;
import roombooking.observer.NotificationService;

import java.time.LocalDateTime;
import java.time.Duration;

public class ExtendCommand implements Command {
    private int bookingId;
    private LocalDateTime newEndTime;
    private BookingService bookingService;
    private Booking booking;
    private LocalDateTime oldEndTime;
    private double extensionCost;
    private boolean executed = false;
    
    public ExtendCommand(int bookingId, LocalDateTime newEndTime, BookingService bookingService) {
        this.bookingId = bookingId;
        this.newEndTime = newEndTime;
        this.bookingService = bookingService;
    }
    
    @Override
    public boolean execute() {
        booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(booking.getEndTime())) {
            return false;
        }
        
        if (newEndTime.isBefore(booking.getEndTime()) || newEndTime.equals(booking.getEndTime())) {
            return false;
        }
        
        if (!bookingService.isRoomAvailable(booking.getRoomId(), booking.getStartTime(), newEndTime)) {
            return false;
        }
        
        User user = bookingService.getUser(booking.getUserId());
        long extraHours = Duration.between(booking.getEndTime(), newEndTime).toHours();
        if (extraHours <= 0) extraHours = 1;
        extensionCost = user.getRate() * extraHours;
        
        if (!bookingService.processPayment(booking.getUserId(), extensionCost)) {
            return false;
        }
        
        oldEndTime = booking.getEndTime();
        booking.setEndTime(newEndTime);
        booking.setTotalCost(booking.getTotalCost() + extensionCost);
        bookingService.updateBooking(booking);
        executed = true;
        
        NotificationService.getInstance().sendNotification(
            "Booking extended: " + bookingId
        );
        
        return true;
    }
    
    @Override
    public boolean undo() {
        if (booking == null) {
            return false;
        }
        
        booking.setEndTime(oldEndTime);
        booking.setTotalCost(booking.getTotalCost() - extensionCost);
        bookingService.updateBooking(booking);
        bookingService.processRefund(booking.getUserId(), extensionCost);
        
        return true;
    }
    
    @Override
    public String getCommandType() {
        return "EXTEND_BOOKING";
    }
    
    public boolean isExecuted() {
        return executed;
    }
}
