package roombooking.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.format.DateTimeFormatter;

import roombooking.controller.BookingController;
import roombooking.model.Booking;

// lets the user pick how many hours to extend an existing booking by,
// shown after clicking "Extend" on a booking card in AppBookingsBrowserPanel
public class AppExtendBookingPanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final Color SUCCESS_COLOR = new Color(0x4A, 0xD9, 0x8A);
    private static final Color ERROR_COLOR = new Color(0xFF, 0x6B, 0x6B);
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a");

    private final BookingController bookingController = new BookingController();
    private final Booking booking;
    private final Runnable onSuccess;

    private JTextField hoursField;
    private RoundedButton confirmBtn;
    private JLabel statusLabel;

    public AppExtendBookingPanel(Booking booking, Runnable onBack, Runnable onSuccess) {
        this.booking = booking;
        this.onSuccess = onSuccess;

        setOpaque(false); // background comes from AppShellFrame's GlowBackgroundPanel
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

        JLabel title = new JLabel("Extend Booking");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        String roomTitle = booking.getRoom().getBuildingName() + " - Room " + booking.getRoom().getRoomNumber();
        String currentEnd = booking.getEndTime().format(DATE_TIME_FORMAT);

        JLabel subtitle = new JLabel(roomTitle + "   |   Currently ends " + currentEnd);
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
        card.setPreferredSize(new Dimension(380, 260));
        card.setMaximumSize(new Dimension(380, 260));

        JLabel label = new JLabel("Extend by how many hours?");
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(Colours.TEXT_LIGHT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(label);
        card.add(Box.createVerticalStrut(10));

        hoursField = new JTextField();
        hoursField.setBackground(new Color(0x1B, 0x22, 0x33));
        hoursField.setForeground(Colours.TEXT_LIGHT);
        hoursField.setCaretColor(Colours.TEXT_LIGHT);
        hoursField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        hoursField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        hoursField.setAlignmentX(Component.LEFT_ALIGNMENT);
        hoursField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(hoursField);
        card.add(Box.createVerticalStrut(16));

        // reserves its own space so the layout does not jump when a message appears
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(8));

        confirmBtn = new RoundedButton("Confirm Extension", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        confirmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmBtn.setEnabled(false); // stays disabled until the field has something in it
        confirmBtn.addActionListener(e -> submitExtension());
        card.add(confirmBtn);

        card.add(Box.createVerticalGlue());

        hoursField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { confirmBtn.setEnabled(!hoursField.getText().isBlank()); }
            @Override public void removeUpdate(DocumentEvent e) { confirmBtn.setEnabled(!hoursField.getText().isBlank()); }
            @Override public void changedUpdate(DocumentEvent e) { confirmBtn.setEnabled(!hoursField.getText().isBlank()); }
        });

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);
        return wrapper;
    }

    private void submitExtension() {
        int hours;
        try {
            hours = Integer.parseInt(hoursField.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Please enter a whole number of hours.");
            return;
        }

        if (hours <= 0) {
            showError("Hours must be greater than zero.");
            return;
        }

        // TODO: BookingController needs a method with this exact signature added -
        // boolean extendBooking(Booking booking, int additionalHours) - it should
        // apply whatever validation makes sense there (e.g. checking the room is
        // actually free for the extended time) and return true on success, false on failure
        
        //boolean successful = bookingController.extendBooking(booking, hours);
        boolean successful = bookingController.extendBooking(booking, hours);
        
        if (successful) {
            showSuccess();
        } else {
            showError("This booking could not be extended. The room may not be available for that long.");
        }
    }

    private void showSuccess() {
        statusLabel.setForeground(SUCCESS_COLOR);
        statusLabel.setText("Booking extended successfully!");
        hoursField.setEnabled(false);
        confirmBtn.setEnabled(false);

        Timer timer = new Timer(2000, e -> { if (onSuccess != null) onSuccess.run(); });
        timer.setRepeats(false);
        timer.start();
    }

    private void showError(String message) {
        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setText(message);
        // form stays enabled so the person can correct the value and retry
    }
}