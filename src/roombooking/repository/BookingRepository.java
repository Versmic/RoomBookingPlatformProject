package roombooking.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import roombooking.enums.BookingStatus;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;
import java.time.LocalDateTime;
import roombooking.model.Booking;
import roombooking.model.Room;

public class BookingRepository {

    private static final String FILE_NAME = "src/roombooking/database/bookings.csv";

    private static final int BOOKING_ID_COLUMN = 0;
    private static final int USERNAME_COLUMN = 1;
    private static final int ROOM_ID_COLUMN = 2;
    private static final int START_TIME_COLUMN = 3;
    private static final int END_TIME_COLUMN = 4;
    private static final int DEPOSIT_COLUMN = 5;
    private static final int FINAL_COST_COLUMN = 6;
    private static final int STATUS_COLUMN = 7;
    private static final int REQUIRED_COLUMNS = 8;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();
    private final RoomRepository roomRepository = new RoomRepository();

    // saves a new booking
    public void saveBooking(Booking booking) {
        if (booking == null) {
            return;
        }

        if (findBookingById(booking.getBookingId()) != null) {
            throw new IllegalArgumentException("booking id already exists");
        }

        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(booking));
        db.writeCSV(FILE_NAME, rows);
    }

    // finds a booking using its id
    public Booking findBookingById(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            return null;
        }

        for (String[] row : db.readCSV(FILE_NAME)) {

            if (row[BOOKING_ID_COLUMN].trim().equalsIgnoreCase(bookingId.trim())) {
                return toBooking(row);
            }
        }

        return null;
    }

    // updates a booking using its id
    public void updateBooking(Booking booking) {
        if (booking == null) {
            return;
        }

        db.updateRow(FILE_NAME, BOOKING_ID_COLUMN, booking.getBookingId(), toRow(booking));
    }

    // deletes a booking using its id
    public void deleteBooking(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            return;
        }

        db.deleteRow(FILE_NAME, BOOKING_ID_COLUMN, bookingId.trim());
    }

    // returns every valid booking
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();

        for (String[] row : db.readCSV(FILE_NAME)) {
                bookings.add(toBooking(row));
        }

        return bookings;
    }

    // returns bookings belonging to one account
    public List<Booking> findBookingsByUsername(String username) {
        List<Booking> bookings = new ArrayList<>();

        if (username == null || username.isBlank()) {
            return bookings;
        }

        for (Booking booking : getAllBookings()) {
            if (booking.getAccount().getUserName().equalsIgnoreCase(username.trim())) {
                bookings.add(booking);
            }
        }

        return bookings;
    }

    // returns bookings belonging to one room
    public List<Booking> findBookingsByRoomId(String roomId) {
        List<Booking> bookings = new ArrayList<>();

        if (roomId == null || roomId.isBlank()) {
            return bookings;
        }

        for (Booking booking : getAllBookings()) {
            if (booking.getRoom().getRoomId().equalsIgnoreCase(roomId.trim())) {
                bookings.add(booking);
            }
        }

        return bookings;
    }

    // creates the next booking id
    public String generateBookingId() {
        int highestNumber = 0;

        for (Booking booking : getAllBookings()) {
            String bookingId = booking.getBookingId();

            if (bookingId != null && bookingId.matches("B\\d+")) {
                int number = Integer.parseInt(bookingId.substring(1));
                highestNumber = Math.max(highestNumber, number);
            }
        }

        return String.format("B%03d", highestNumber + 1);
    }

    // converts a booking into a csv row
    private String[] toRow(Booking booking) {
        return new String[] {
                booking.getBookingId(),
                booking.getAccount().getUserName(),
                booking.getRoom().getRoomId(),
                booking.getStartTime().toString(),
                booking.getEndTime().toString(),
                String.valueOf(booking.getDepositAmount()),
                String.valueOf(booking.getFinalCost()),
                booking.getStatus().name()
        };
    }

    // converts a csv row into a booking
    private Booking toBooking(String[] row) {
        String bookingId = row[BOOKING_ID_COLUMN].trim();
        String username = row[USERNAME_COLUMN].trim();
        String roomId = row[ROOM_ID_COLUMN].trim();
        
        AccountRepository accountRepo = new AccountRepository();
        Account account = accountRepo.findAccountByUserName(username);
        
        Room room = roomRepository.findRoomById(roomId);

        LocalDateTime startTime = LocalDateTime.parse(row[START_TIME_COLUMN].trim());
        LocalDateTime endTime = LocalDateTime.parse(row[END_TIME_COLUMN].trim());
        double depositAmount = Double.parseDouble(row[DEPOSIT_COLUMN].trim());
        double finalCost = Double.parseDouble(row[FINAL_COST_COLUMN].trim());


        BookingStatus status = BookingStatus.valueOf(row[STATUS_COLUMN].trim().toUpperCase());

        return new Booking(bookingId, account, room, startTime, endTime, depositAmount, finalCost,  status);
    }
    
    

}