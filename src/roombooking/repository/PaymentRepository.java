package roombooking.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Payment;
import roombooking.strategy.CreditCardProcessorStrategy;
import roombooking.strategy.DebitCardProcessorStrategy;
import roombooking.strategy.InstitutionalBillingProcessorStrategy;
import roombooking.strategy.PaymentProcessorStrategy;

// handles payment csv storage
public class PaymentRepository {

    private static final String FILE_NAME = "src/roombooking/database/payments.csv";

    private static final int PAYMENT_ID_COLUMN = 0;
    private static final int BOOKING_ID_COLUMN = 1;
    private static final int AMOUNT_COLUMN = 2;
    private static final int PAYMENT_DATE_COLUMN = 3;
    private static final int PAYMENT_TYPE_COLUMN = 4;
    private static final int PAYMENT_METHOD_COLUMN = 5;
    private static final int REQUIRED_COLUMNS = 6;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    // saves a new payment
    public void savePayment(Payment payment) {
        if (payment == null) {
            return;
        }

        if (findPaymentById(payment.getPaymentId()) != null) {
            throw new IllegalArgumentException("payment id already exists");
        }

        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(payment));
        db.writeCSV(FILE_NAME, rows);
    }

    // updates a payment using its id
    public void updatePayment(Payment payment) {
        if (payment == null) {
            return;
        }

        db.updateRow(FILE_NAME, PAYMENT_ID_COLUMN, payment.getPaymentId(), toRow(payment));
    }

    // finds a payment using its id
    public Payment findPaymentById(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            return null;
        }

        String searchedId = paymentId.trim();

        for (String[] row : db.readCSV(FILE_NAME)) {
            if (!isValidPaymentRow(row)) {
                continue;
            }

            if (row[PAYMENT_ID_COLUMN].trim().equalsIgnoreCase(searchedId)) {
                return toPayment(row);
            }
        }

        return null;
    }

    // finds all payments for one booking
    public List<Payment> findPaymentsByBookingId(String bookingId) {
        List<Payment> payments = new ArrayList<>();

        if (bookingId == null || bookingId.isBlank()) {
            return payments;
        }

        String searchedBookingId = bookingId.trim();

        for (String[] row : db.readCSV(FILE_NAME)) {
            if (!isValidPaymentRow(row)) {
                continue;
            }

            if (row[BOOKING_ID_COLUMN].trim().equalsIgnoreCase(searchedBookingId)) {
                payments.add(toPayment(row));
            }
        }

        return payments;
    }

    // returns every valid payment
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();

        for (String[] row : db.readCSV(FILE_NAME)) {
            if (isValidPaymentRow(row)) {
                payments.add(toPayment(row));
            }
        }

        return payments;
    }

    // deletes a payment using its id
    public void deletePayment(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            return;
        }

        db.deleteRow(FILE_NAME, PAYMENT_ID_COLUMN, paymentId.trim());
    }

    // generates the next payment id
    public String generatePaymentId() {
        int highestNumber = 0;

        for (Payment payment : getAllPayments()) {
            String paymentId = payment.getPaymentId();

            if (paymentId != null && paymentId.matches("P\\d+")) {
                int number = Integer.parseInt(paymentId.substring(1));
                highestNumber = Math.max(highestNumber, number);
            }
        }

        return String.format("P%03d", highestNumber + 1);
    }

    // converts a payment into a csv row
    private String[] toRow(Payment payment) {
        return new String[] {
                payment.getPaymentId(),
                payment.getBookingId(),
                String.valueOf(payment.getAmount()),
                payment.getPaymentDate().toString(),
                payment.getPaymentType().name(),
                payment.getPaymentMethod().name()
        };
    }

    // converts a csv row into a payment
    private Payment toPayment(String[] row) {
        String paymentId = row[PAYMENT_ID_COLUMN].trim();
        String bookingId = row[BOOKING_ID_COLUMN].trim();
        double amount = Double.parseDouble(row[AMOUNT_COLUMN].trim());
        LocalDateTime paymentDate = LocalDateTime.parse(row[PAYMENT_DATE_COLUMN].trim());
        PaymentType paymentType = PaymentType.valueOf(row[PAYMENT_TYPE_COLUMN].trim().toUpperCase());
        PaymentMethod paymentMethod = PaymentMethod.valueOf(row[PAYMENT_METHOD_COLUMN].trim().toUpperCase());
        PaymentProcessorStrategy paymentProcessor = createPaymentProcessor(paymentMethod);

        return new Payment(paymentId, bookingId, amount, paymentDate, paymentType, paymentMethod, paymentProcessor);
    }

    // recreates the correct processor after loading
    private PaymentProcessorStrategy createPaymentProcessor(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case CREDITCARD -> new CreditCardProcessorStrategy();
            case DEBITCARD -> new DebitCardProcessorStrategy();
            case INSTITUTIONALBILLING -> new InstitutionalBillingProcessorStrategy();
        };
    }

    // checks that a row contains valid payment data
    private boolean isValidPaymentRow(String[] row) {
        if (row == null || row.length < REQUIRED_COLUMNS) {
            return false;
        }

        try {
            Double.parseDouble(row[AMOUNT_COLUMN].trim());
            LocalDateTime.parse(row[PAYMENT_DATE_COLUMN].trim());
            PaymentType.valueOf(row[PAYMENT_TYPE_COLUMN].trim().toUpperCase());
            PaymentMethod.valueOf(row[PAYMENT_METHOD_COLUMN].trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}