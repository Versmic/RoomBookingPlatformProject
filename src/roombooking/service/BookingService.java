package roombooking.service;

import users.Booking;
import users.RegisteredUser;
import users.Room;
import users.BookingStatus;
import users.RoomStatus;
import users.BookingRepository;
import users.RoomRepository;
import users.UserRepository;
import roombooking.observer.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {
    private BookingRepository bookingRepo;
    private RoomRepository roomRepo;
    private UserRepository userRepo;
    private PaymentService paymentService;
    
    public BookingService() {
        this.bookingRepo = new BookingRepository();
        this.roomRepo = new RoomRepository();
        this.userRepo = new UserRepository();
        this.paymentService = new PaymentService();
    }
    
    public RegisteredUser getUser(int userId) {
        return userRepo.findUserByID(userId);
    }
    
    public double getUserRate(int userId) {
        RegisteredUser user = getUser(userId);
        if (user == null) return 0.0;
        return user.getRate();
    }
    
    public boolean isRoomAvailable(int roomId, LocalDateTime start, LocalDateTime end) {
        Room room = roomRepo.findRoomByID(roomId);
        if (room == null) return false;
        return room.getStatus() == RoomStatus.AVAILABLE;
    }
    
    public Booking bookRoom(int userId, int roomId, LocalDateTime start, LocalDateTime end, double deposit) {
        Booking booking = new Booking(0, userId, roomId, start, end, deposit, 0.0, null, BookingStatus.CONFIRMED);
        bookingRepo.saveBooking(booking);
        return booking;
    }
    
    public Booking getBooking(int bookingId) {
        return bookingRepo.findBookingByID(bookingId);
    }
    
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingRepo.findBookingByID(bookingId);
        if (booking == null) return false;
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepo.updateBooking(booking);
        return true;
    }
    
    public boolean processPayment(int userId, double amount) {
        return paymentService.processPayment(userId, amount);
    }
    
    public boolean processRefund(int userId, double amount) {
        return paymentService.processRefund(userId, amount);
    }
    
    public void updateBooking(Booking booking) {
        bookingRepo.updateBooking(booking);
    }
    
    public boolean restoreBooking(Booking booking) {
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepo.updateBooking(booking);
        return true;
    }
    
    public boolean validateBadge(String badgeId, Booking booking) {
        return true;
    }
    
    public double applyDepositToFinalCost(Booking booking) {
        double remaining = booking.getFinalCost() - booking.getDepositAmount();
        booking.setFinalCost(booking.getFinalCost() - booking.getDepositAmount());
        bookingRepo.updateBooking(booking);
        return remaining;
    }
    
    public void markDepositLost(Booking booking) {
        // Deposit is lost - no refund
    }
    
    public void undoDepositApplication(Booking booking) {
        booking.setFinalCost(booking.getFinalCost() + booking.getDepositAmount());
        bookingRepo.updateBooking(booking);
    }
    
    public void undoNoShowMark(Booking booking) {
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepo.updateBooking(booking);
    }
    
    public List<Booking> getFutureBookingsForRoom(int roomId) {
        List<Booking> allBookings = bookingRepo.getAllBookings();
        LocalDateTime now = LocalDateTime.now();
        allBookings.removeIf(b -> b.getRoomID() != roomId || b.getEndTime().isBefore(now));
        return allBookings;
    }
}
