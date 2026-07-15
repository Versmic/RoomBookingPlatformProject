package roombooking.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import roombooking.model.Booking;
import users.BookingStatus;

public class BookingRepository {

    private static final String FILE_NAME = "bookings.csv";
    private static final int ID_COLUMN = 0;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    public void saveBooking(Booking booking) {
        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(booking));
        db.writeCSV(FILE_NAME, rows);
    }

    public Booking findBookingByID(String bookingID) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > ID_COLUMN && row[ID_COLUMN].equals(bookingID)) {
                return toBooking(row);
            }
        }
        return null;
    }

    public void updateBooking(Booking booking) {
        db.updateRow(FILE_NAME, ID_COLUMN, String.valueOf(booking.getBookingID()), toRow(booking));
    }

    public void deleteBooking(int bookingID) {
        db.deleteRow(FILE_NAME, ID_COLUMN, String.valueOf(bookingID));
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            bookings.add(toBooking(row));
        }
        return bookings;
    }

    private String[] toRow(Booking booking) {
        return new String[] {
                String.valueOf(booking.getBookingID()),
                String.valueOf(booking.getUserID()),
                String.valueOf(booking.getRoomID()),
                booking.getStartTime().toString(),
                booking.getEndTime().toString(),
                String.valueOf(booking.getDepositAmount()),
                String.valueOf(booking.getFinalCost()),
                booking.getCheckInTime() == null ? "" : booking.getCheckInTime().toString(),
                booking.getStatus().name()
        };
    }

    private Booking toBooking(String[] row) {
        int bookingID = Integer.parseInt(row[0]);
        int userID = Integer.parseInt(row[1]);
        int roomID = Integer.parseInt(row[2]);
        LocalDateTime startTime = LocalDateTime.parse(row[3]);
        LocalDateTime endTime = LocalDateTime.parse(row[4]);
        double depositAmount = Double.parseDouble(row[5]);
        double finalCost = Double.parseDouble(row[6]);
        LocalDateTime checkInTime = row[7].isEmpty() ? null : LocalDateTime.parse(row[7]);
        BookingStatus status = BookingStatus.valueOf(row[8]);
        return new Booking(bookingID, userID, roomID, startTime, endTime,
                depositAmount, finalCost, checkInTime, status);
    }
}
