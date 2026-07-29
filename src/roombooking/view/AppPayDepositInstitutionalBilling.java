package roombooking.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import roombooking.controller.PaymentController;
import roombooking.enums.PaymentMethod;
import roombooking.model.Account;
import roombooking.model.Room;

// institutional billing payment form - shown after picking "Institutional Billing" on AppPayDepositPanel
public class AppPayDepositInstitutionalBilling extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final Color SUCCESS_COLOR = new Color(0x4A, 0xD9, 0x8A);
    private static final Color ERROR_COLOR = new Color(0xFF, 0x6B, 0x6B);

    private final PaymentController paymentController = new PaymentController();
    private final double amount;
    private final Runnable onSuccess;

    private JTextField institutionNameField;
    private JTextField accountNumberField;
    private JTextField purchaseOrderField;
    private JTextField authorizedByField;
    private RoundedButton payBtn;
    private JLabel statusLabel;
    
    private final Account account;
    private final Room room;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final double finalCost;

    public AppPayDepositInstitutionalBilling(double amount, Runnable onBack, Runnable onSuccess, Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, double finalCost) {
    	
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

        JLabel title = new JLabel("Institutional Billing");
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

        institutionNameField = buildInputField();
        accountNumberField = buildInputField();
        purchaseOrderField = buildInputField();
        authorizedByField = buildInputField();

        card.add(buildFieldLabel("Institution Name"));
        card.add(Box.createVerticalStrut(6));
        card.add(institutionNameField);
        card.add(Box.createVerticalStrut(16));

        card.add(buildFieldLabel("Account Number"));
        card.add(Box.createVerticalStrut(6));
        card.add(accountNumberField);
        card.add(Box.createVerticalStrut(16));

        card.add(buildFieldLabel("Purchase Order Number"));
        card.add(Box.createVerticalStrut(6));
        card.add(purchaseOrderField);
        card.add(Box.createVerticalStrut(16));

        card.add(buildFieldLabel("Authorized By"));
        card.add(Box.createVerticalStrut(6));
        card.add(authorizedByField);
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

        DocumentListener validityListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { updatePayButtonState(); }
            @Override public void removeUpdate(DocumentEvent e) { updatePayButtonState(); }
            @Override public void changedUpdate(DocumentEvent e) { updatePayButtonState(); }
        };
        institutionNameField.getDocument().addDocumentListener(validityListener);
        accountNumberField.getDocument().addDocumentListener(validityListener);
        purchaseOrderField.getDocument().addDocumentListener(validityListener);
        authorizedByField.getDocument().addDocumentListener(validityListener);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        return wrapper;
    }

    private void updatePayButtonState() {
        boolean allFilled = !institutionNameField.getText().isBlank()
                && !accountNumberField.getText().isBlank()
                && !purchaseOrderField.getText().isBlank()
                && !authorizedByField.getText().isBlank();
        payBtn.setEnabled(allFilled);
    }

    private void submitPayment() {
        // TODO: confirm this ordering matches what InstitutionalBillingProcessorStrategy expects
        ArrayList<String> paymentDetails = new ArrayList<>();
        paymentDetails.add(institutionNameField.getText().trim());
        paymentDetails.add(accountNumberField.getText().trim());
        paymentDetails.add(purchaseOrderField.getText().trim());
        paymentDetails.add(authorizedByField.getText().trim());

        boolean successful = paymentController.processPayment(amount, PaymentMethod.INSTITUTIONALBILLING, paymentDetails);

        if (successful) {
            showSuccess();
        } else {
            showError("Payment was unsuccessful. Invalid billing details.");
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
        institutionNameField.setEnabled(enabled);
        accountNumberField.setEnabled(enabled);
        purchaseOrderField.setEnabled(enabled);
        authorizedByField.setEnabled(enabled);
        payBtn.setEnabled(enabled);
    }

    private JTextField buildInputField() {
        JTextField field = new JTextField();
        field.setBackground(new Color(0x1B, 0x22, 0x33));
        field.setForeground(Colours.TEXT_LIGHT);
        field.setCaretColor(Colours.TEXT_LIGHT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return field;
    }

    private JLabel buildFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Colours.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}