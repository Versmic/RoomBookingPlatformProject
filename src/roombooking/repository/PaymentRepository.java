package roombooking.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import roombooking.model.Payment;
import roombooking.model.PaymentMethod;
import users.PaymentType;


public class PaymentRepository {

    private static final String FILE_NAME = "payments.csv";
    private static final int ID_COLUMN = 0;
    private static final int BOOKING_ID_COLUMN = 1;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    public void savePayment(Payment payment) {
        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(payment));
        db.writeCSV(FILE_NAME, rows);
    }

    public void updatePayment(Payment payment) {
        db.updateRow(FILE_NAME, ID_COLUMN, String.valueOf(payment.getPaymentID()), toRow(payment));
    }

    public Payment findPaymentByID(int paymentID) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > ID_COLUMN && row[ID_COLUMN].equals(String.valueOf(paymentID))) {
                return toPayment(row);
            }
        }
        return null;
    }

    public List<Payment> findPaymentsByBooking(int bookingID) {
        List<Payment> payments = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > BOOKING_ID_COLUMN && row[BOOKING_ID_COLUMN].equals(String.valueOf(bookingID))) {
                payments.add(toPayment(row));
            }
        }
        return payments;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            payments.add(toPayment(row));
        }
        return payments;
    }

    private String[] toRow(Payment payment) {
        return new String[] {
                String.valueOf(payment.getPaymentID()),
                String.valueOf(payment.getBookingID()),
                String.valueOf(payment.getAmount()),
                payment.getPaymentDate().toString(),
                payment.getPaymentType().name(),
                payment.getPaymentMethod().name()
        };
    }

    private Payment toPayment(String[] row) {
        int paymentID = Integer.parseInt(row[0]);
        int bookingID = Integer.parseInt(row[1]);
        double amount = Double.parseDouble(row[2]);
        LocalDateTime paymentDate = LocalDateTime.parse(row[3]);
        PaymentType paymentType = PaymentType.valueOf(row[4]);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(row[5]);
        return new Payment(paymentID, bookingID, amount, paymentDate, paymentType, paymentMethod);
    }
}