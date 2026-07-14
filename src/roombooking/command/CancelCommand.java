package roombooking.command;

import roombooking.model.Booking;
import roombooking.model.enums.BookingStatus;
import roombooking.service.BookingService;
import roombooking.observer.NotificationService;

import java.time.LocalDateTime;

public class CancelCommand implements Command {
    private int bookingId;
    private BookingService bookingService;
    private Booking cancelledBooking;
    private boolean executed = false;
    
    public CancelCommand(int bookingId, BookingService bookingService) {
        this.bookingId = bookingId;
        this.bookingService = bookingService;
    }
    
    @Override
    public boolean execute() {
        cancelledBooking = bookingService.getBooking(bookingId);
        if (cancelledBooking == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(cancelledBooking.getStartTime())) {
            return false;
        }
        
        if (cancelledBooking.getStatus() == BookingStatus.CANCELLED) {
            return false;
        }
        
        executed = bookingService.cancelBooking(bookingId);
        if (executed) {
            double refundAmount = cancelledBooking.getDepositPaid();
            bookingService.processRefund(cancelledBooking.getUserId(), refundAmount);
            
            NotificationService.getInstance().sendNotification(
                "Booking cancelled: " + bookingId
            );
        }
        return executed;
    }
    
    @Override
    public boolean undo() {
        if (cancelledBooking == null) {
            return false;
        }
        return bookingService.restoreBooking(cancelledBooking);
    }
    
    @Override
    public String getCommandType() {
        return "CANCEL_BOOKING";
    }
    
    public boolean isExecuted() {
        return executed;
    }
    
    public Booking getCancelledBooking() {
        return cancelledBooking;
    }
}
