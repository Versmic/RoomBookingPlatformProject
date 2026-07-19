package roombooking.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import roombooking.controller.BookingController;
import roombooking.controller.PaymentController;
import roombooking.enums.PaymentMethod;
import roombooking.enums.PaymentType;
import roombooking.model.Account;
import roombooking.model.Room;
import roombooking.model.Booking;
import roombooking.repository.BookingRepository;
import roombooking.repository.PaymentRepository;
import roombooking.enums.BookingStatus;

// debit card payment form - shown after picking "Pay with Debit Card" on AppPayDepositPanel
public class AppPayDepositDebitCard extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final Color SUCCESS_COLOR = new Color(0x4A, 0xD9, 0x8A);
    private static final Color ERROR_COLOR = new Color(0xFF, 0x6B, 0x6B);

    private final PaymentController paymentController = new PaymentController();
    private final double amount;
    private final Runnable onSuccess;

    private JTextField cardholderNameField;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JPasswordField pinField;
    private RoundedButton payBtn;
    private JLabel statusLabel;
    
    private final Account account;
    private final Room room;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final double finalCost;
    
    public AppPayDepositDebitCard(double amount, Runnable onBack, Runnable onSuccess, Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, double finalCost) {
        this.amount = amount;
        this.onSuccess = onSuccess;
        this.account = account;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.finalCost = finalCost;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        add(buildHeader(onBack), BorderLayout.NORTH);
        add(buildFormCard(), BorderLayout.CENTER);
    }

    private JPanel buildHeader(Runnable onBack) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        RoundedButton backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> { if (onBack != null) onBack.run(); });

        JLabel title = new JLabel("Pay with Debit Card");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(String.format("Deposit due: $%.2f", amount));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(backBtn);
        header.add(Box.createVerticalStrut(16));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);
        return header;
    }

    private JComponent buildFormCard() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(24, 28, 24, 28)));
        card.setPreferredSize(new Dimension(420, 420));
        card.setMaximumSize(new Dimension(420, 420));

        cardholderNameField = buildInputField();
        cardNumberField = buildInputField();
        expiryField = buildInputField();
        pinField = buildPasswordField();

        card.add(buildFieldLabel("Cardholder Name"));
        card.add(Box.createVerticalStrut(6));
        card.add(cardholderNameField);
        card.add(Box.createVerticalStrut(16));

        card.add(buildFieldLabel("Card Number"));
        card.add(Box.createVerticalStrut(6));
        card.add(cardNumberField);
        card.add(Box.createVerticalStrut(16));

        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel expiryCol = new JPanel();
        expiryCol.setOpaque(false);
        expiryCol.setLayout(new BoxLayout(expiryCol, BoxLayout.Y_AXIS));
        expiryCol.add(buildFieldLabel("Expiry (MM/YY)"));
        expiryCol.add(Box.createVerticalStrut(6));
        expiryCol.add(expiryField);

        JPanel pinCol = new JPanel();
        pinCol.setOpaque(false);
        pinCol.setLayout(new BoxLayout(pinCol, BoxLayout.Y_AXIS));
        pinCol.add(buildFieldLabel("PIN"));
        pinCol.add(Box.createVerticalStrut(6));
        pinCol.add(pinField);

        row.add(expiryCol);
        row.add(pinCol);
        card.add(row);
        card.add(Box.createVerticalStrut(20));

        // reserves its own space so the layout does not jump when a message appears
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(8));

        payBtn = new RoundedButton("Pay", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        payBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        payBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        payBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        payBtn.setEnabled(false); // stays disabled until every field has something in it
        payBtn.addActionListener(e -> submitPayment());
        card.add(payBtn);

        card.add(Box.createVerticalGlue());
        
        // listeners to see if inserts or delete are made on text
        DocumentListener validityListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updatePayButtonState(); }
            @Override public void removeUpdate(DocumentEvent e) { updatePayButtonState(); }
            @Override public void changedUpdate(DocumentEvent e) { updatePayButtonState(); }
        };
        // add listeners to each field
        cardholderNameField.getDocument().addDocumentListener(validityListener);
        cardNumberField.getDocument().addDocumentListener(validityListener);
        expiryField.getDocument().addDocumentListener(validityListener);
        pinField.getDocument().addDocumentListener(validityListener);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        return wrapper;
    }

    private void updatePayButtonState() {
        boolean allFilled = !cardholderNameField.getText().isBlank()
                && !cardNumberField.getText().isBlank()
                && !expiryField.getText().isBlank()
                && pinField.getPassword().length > 0;
        payBtn.setEnabled(allFilled);
    }

    private void submitPayment() {
        // TODO: confirm this ordering matches what DebitCardProcessorStrategy expects
        ArrayList<String> paymentDetails = new ArrayList<>();
        paymentDetails.add(cardholderNameField.getText().trim());
        paymentDetails.add(cardNumberField.getText().trim());
        paymentDetails.add(expiryField.getText().trim());
        paymentDetails.add(new String(pinField.getPassword()).trim());
        
        
        boolean successful = paymentController.processPayment(amount, PaymentMethod.DEBITCARD, paymentDetails);

        if (successful) {
        	BookingController bookingController = new BookingController();
        	
        	//store booking and payment
        	Booking booking = bookingController.storeBooking(account, room, startTime, endTime, BookingStatus.ACTIVE);
        	paymentController.storePayment(booking.getBookingId(), this.amount, PaymentType.DEPOSIT, PaymentMethod.DEBITCARD);
        	
            showSuccess();
        } else {
            showError("Payment was unsuccessful. Invalid card details.");
        }
    }

    private void showSuccess() {
        statusLabel.setForeground(SUCCESS_COLOR);
        statusLabel.setText("Payment successful!");
        setFormEnabled(false);

        Timer timer = new Timer(2500, e -> { if (onSuccess != null) onSuccess.run(); });
        timer.setRepeats(false);
        timer.start();
    }

    private void showError(String message) {
        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setText(message);
        // form stays enabled so the person can correct the details and retry
    }

    private void setFormEnabled(boolean enabled) {
        cardholderNameField.setEnabled(enabled);
        cardNumberField.setEnabled(enabled);
        expiryField.setEnabled(enabled);
        pinField.setEnabled(enabled);
        payBtn.setEnabled(enabled);
    }

    private JTextField buildInputField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private JPasswordField buildPasswordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(0x1B, 0x22, 0x33));
        field.setForeground(Colours.TEXT_LIGHT);
        field.setCaretColor(Colours.TEXT_LIGHT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    }

    private JLabel buildFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Colours.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}