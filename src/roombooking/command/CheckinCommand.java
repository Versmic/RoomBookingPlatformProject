package roombooking.command;

import roombooking.model.Booking;
import roombooking.model.enums.BookingStatus;
import roombooking.service.BookingService;
import roombooking.observer.NotificationService;

import java.time.LocalDateTime;
import java.time.Duration;

public class CheckinCommand implements Command {
    private int bookingId;
    private String badgeId;
    private BookingService bookingService;
    private Booking booking;
    private boolean executed = false;
    private boolean wasWithinWindow;
    
    public CheckinCommand(int bookingId, String badgeId, BookingService bookingService) {
        this.bookingId = bookingId;
        this.badgeId = badgeId;
        this.bookingService = bookingService;
    }
    
    @Override
    public boolean execute() {
        booking = bookingService.getBooking(bookingId);
        if (booking == null) {
            return false;
        }
        
        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            return false;
        }
        
        if (booking.getStatus() == BookingStatus.COMPLETED || 
            booking.getStatus() == BookingStatus.CANCELLED) {
            return false;
        }
        
        if (!bookingService.validateBadge(badgeId, booking)) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = booking.getStartTime();
        Duration duration = Duration.between(startTime, now);
        long minutesLate = duration.toMinutes();
        
        if (minutesLate >= -5 && minutesLate <= 30) {
            wasWithinWindow = true;
            booking.setStatus(BookingStatus.CHECKED_IN);
            bookingService.updateBooking(booking);
            executed = true;
            
            double remainingBalance = bookingService.applyDepositToFinalCost(booking);
            if (remainingBalance > 0) {
                bookingService.processPayment(booking.getUserId(), remainingBalance);
            }
            
            NotificationService.getInstance().sendNotification(
                "Check-in successful: " + bookingId
            );
            
        } else if (minutesLate > 30) {
            wasWithinWindow = false;
            booking.setStatus(BookingStatus.NO_SHOW);
            bookingService.updateBooking(booking);
            executed = true;
            bookingService.markDepositLost(booking);
            
            NotificationService.getInstance().sendNotification(
                "No-show: " + bookingId
            );
            
        } else {
            executed = false;
        }
        
        return executed;
    }
    
    @Override
    public boolean undo() {
        if (booking == null) {
            return false;
        }
        
        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingService.undoDepositApplication(booking);
            bookingService.updateBooking(booking);
            return true;
        } else if (booking.getStatus() == BookingStatus.NO_SHOW) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingService.undoNoShowMark(booking);
            bookingService.updateBooking(booking);
            return true;
        }
        return false;
    }
    
    @Override
    public String getCommandType() {
        return "CHECKIN";
    }
    
    public boolean isExecuted() {
        return executed;
    }
    
    public boolean wasWithinWindow() {
        return wasWithinWindow;
    }
}
