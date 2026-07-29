package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import roombooking.controller.BookingController;
import roombooking.enums.BookingStatus;
import roombooking.model.Account;
import roombooking.model.Booking;

// shows the current bookings for the logged in account
public class AppBookingsBrowserPanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a");

    private final BookingController bookingController = new BookingController();

    public AppBookingsBrowserPanel(Account account, Runnable onBack, Consumer<Booking> onExtend) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        add(buildHeader(onBack), BorderLayout.NORTH);
        add(buildBookingList(account, onExtend), BorderLayout.CENTER);
    }

    // builds the top section
    private JPanel buildHeader(Runnable onBack) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        // returns to the dashboard
        RoundedButton backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        JLabel title = new JLabel("My Bookings");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("View your active and upcoming room bookings");
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

    // builds the scrollable booking list
    private JComponent buildBookingList(Account account, Consumer<Booking> onExtend) {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        List<Booking> bookings = bookingController.getCurrentBookings(account);

        if (bookings.isEmpty()) {
            list.add(buildEmptyState());
        } else {
            for (int i = 0; i < bookings.size(); i++) {
                list.add(buildBookingCard(bookings.get(i), onExtend));

                if (i < bookings.size() - 1) {
                    list.add(Box.createVerticalStrut(12));
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        return scrollPane;
    }

    // shown when the account has no current bookings
    private JLabel buildEmptyState() {
        JLabel empty = new JLabel("You do not have any current bookings");
        empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
        empty.setForeground(Colours.TEXT_MUTED);
        empty.setAlignmentX(Component.LEFT_ALIGNMENT);
        return empty;
    }

    // builds one card from a booking record
    private JPanel buildBookingCard(Booking booking, Consumer<Booking> onExtend) {
        JPanel card = new JPanel(new BorderLayout(16, 0));
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        // booking information
        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        String roomTitle = booking.getRoom().getBuildingName() + " - Room " + booking.getRoom().getRoomNumber();

        JLabel roomLabel = new JLabel(roomTitle);
        roomLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        roomLabel.setForeground(Colours.TEXT_LIGHT);

        String startText = booking.getStartTime().format(DATE_TIME_FORMAT);
        String endText = booking.getEndTime().format(DATE_TIME_FORMAT);

        JLabel dateTimeLabel = new JLabel(startText + "   |   " + endText);
        dateTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateTimeLabel.setForeground(Colours.TEXT_MUTED);

        JLabel bookingIdLabel = new JLabel("Booking ID: " + booking.getBookingId());
        bookingIdLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bookingIdLabel.setForeground(Colours.TEXT_MUTED);

        textBox.add(roomLabel);
        textBox.add(Box.createVerticalStrut(5));
        textBox.add(dateTimeLabel);
        textBox.add(Box.createVerticalStrut(5));
        textBox.add(bookingIdLabel);

        card.add(textBox, BorderLayout.CENTER);
        card.add(buildActionPanel(booking, onExtend), BorderLayout.EAST);

        return card;
    }

    // builds the status and extend button section
    private JPanel buildActionPanel(Booking booking, Consumer<Booking> onExtend) {
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        JLabel statusLabel = buildStatusLabel(booking);
        actionPanel.add(statusLabel);

        if (booking.getStatus() == BookingStatus.ACTIVE) {
            RoundedButton extendBookingBtn = new RoundedButton("Extend", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
            extendBookingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            extendBookingBtn.setMaximumSize(new Dimension(120, 34));
            extendBookingBtn.setPreferredSize(new Dimension(120, 34));

            // hands the clicked booking up to whoever owns navigation (AppShellFrame),
            // which is what actually opens AppExtendBookingPanel
            extendBookingBtn.addActionListener(e -> {
                if (onExtend != null) {
                    onExtend.accept(booking);
                }
            });

            actionPanel.add(Box.createVerticalStrut(8));
            actionPanel.add(extendBookingBtn);
        }

        return actionPanel;
    }

    // displays the booking status
    private JLabel buildStatusLabel(Booking booking) {
        JLabel statusLabel = new JLabel(booking.getStatus().name().replace('_', ' '));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        statusLabel.setForeground(Colours.TEXT_LIGHT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setMaximumSize(new Dimension(120, 30));
        statusLabel.setPreferredSize(new Dimension(120, 30));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(0x33, 0x3E, 0x55));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));

        return statusLabel;
    }
}