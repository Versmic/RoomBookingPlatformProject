package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import roombooking.controller.BookingController;
import roombooking.model.Account;
import roombooking.model.Room;

// shows the booking cost and required deposit
public class AppPayDepositPanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a");
    
    private final BookingController bookingController = new BookingController();
    
    public AppPayDepositPanel(Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, Runnable onBack, Runnable onSuccess, Consumer<JComponent> showPanel) {
    	
    	setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));
        
        add(buildHeader(onBack), BorderLayout.NORTH);
        add(buildPaymentCard(account, room, startTime, endTime, onSuccess, showPanel), BorderLayout.CENTER);
    }

    // builds the top section
    private JPanel buildHeader(Runnable onBack) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        RoundedButton backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        JLabel title = new JLabel("Pay Deposit");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Review your booking cost and required deposit");
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

    // builds the booking payment summary
    private JComponent buildPaymentCard(Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, Runnable onSuccess, Consumer<JComponent> showPanel) {
        double finalCost = bookingController.calculateFinalCost(account, startTime, endTime);
        double depositAmount = bookingController.calculateInitialDeposit(account);
        double remainingBalance = Math.max(0, finalCost - depositAmount);

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(24, 28, 24, 28)));
        card.setPreferredSize(new Dimension(680, 410));
        card.setMaximumSize(new Dimension(680, 410));

        JLabel roomLabel = buildLabel(room.getBuildingName() + " - Room " + room.getRoomNumber(), 18, Font.BOLD, Colours.TEXT_LIGHT);
        JLabel timeLabel = buildLabel(startTime.format(DATE_TIME_FORMAT) + "   |   " + endTime.format(DATE_TIME_FORMAT), 12, Font.PLAIN, Colours.TEXT_MUTED);

        card.add(roomLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(timeLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(buildDivider());
        card.add(Box.createVerticalStrut(20));

        card.add(buildAmountRow("Total booking cost", finalCost));
        card.add(Box.createVerticalStrut(14));
        card.add(buildAmountRow("Deposit due now", depositAmount));
        card.add(Box.createVerticalStrut(14));
        card.add(buildAmountRow("Remaining balance", remainingBalance));
        card.add(Box.createVerticalStrut(24));

        JLabel note = buildLabel("The deposit equals one hour of the account's booking rate", 12, Font.PLAIN, Colours.TEXT_MUTED);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(note);
        card.add(Box.createVerticalGlue());

        card.add(buildPaymentButtonRow(account, room, startTime, endTime, depositAmount, finalCost, onSuccess, showPanel));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);

        return wrapper;
    }

    // builds the payment method buttons - each one navigates to its own payment form,
    // passing the deposit amount, a way back to this screen, and a way to reach the
    // dashboard on success
    private JPanel buildPaymentButtonRow(Account account, Room room, LocalDateTime startTime, LocalDateTime endTime, double depositAmount, double finalCost,Runnable onSuccess, Consumer<JComponent> showPanel) {
        JPanel buttonRow = new JPanel(new GridLayout(1, 3, 12, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        RoundedButton debitBtn = new RoundedButton("Pay with Debit Card", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        RoundedButton creditBtn = new RoundedButton("Pay with Credit Card", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        RoundedButton institutionalBtn = new RoundedButton("Institutional Billing", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);

        Runnable backToThisScreen = () -> showPanel.accept(this);

        debitBtn.addActionListener(e -> showPanel.accept(new AppPayDepositDebitCard(depositAmount, backToThisScreen, onSuccess, account, room, startTime, endTime, finalCost)));
        creditBtn.addActionListener(e -> showPanel.accept(new AppPayDepositCreditCard(depositAmount, backToThisScreen, onSuccess, account, room, startTime, endTime, finalCost)));
        institutionalBtn.addActionListener(e -> showPanel.accept(new AppPayDepositInstitutionalBilling(depositAmount, backToThisScreen, onSuccess, account, room, startTime, endTime, finalCost)));

        buttonRow.add(debitBtn);
        buttonRow.add(creditBtn);
        buttonRow.add(institutionalBtn);

        return buttonRow;
    }

    // builds one payment amount row
    private JPanel buildAmountRow(String label, double amount) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelText = buildLabel(label, 13, Font.PLAIN, Colours.TEXT_MUTED);
        JLabel amountText = buildLabel(String.format("$%.2f", amount), 15, Font.BOLD, Colours.TEXT_LIGHT);

        row.add(labelText, BorderLayout.WEST);
        row.add(amountText, BorderLayout.EAST);

        return row;
    }

    // builds a text label
    private JLabel buildLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);

        return label;
    }

    // builds a divider
    private JComponent buildDivider() {
        JPanel divider = new JPanel();
        divider.setBackground(CARD_BORDER);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);

        return divider;
    }
}