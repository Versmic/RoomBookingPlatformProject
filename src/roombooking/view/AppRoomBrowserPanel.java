package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import roombooking.controller.BookingController;
import roombooking.model.Room;

// shows rooms available for the selected booking period
public class AppRoomBrowserPanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy h:mm a");

    private final BookingController bookingController = new BookingController();
    private final JLabel errorLabel;
    private Timer errorTimer;

    public AppRoomBrowserPanel(LocalDateTime startTime, LocalDateTime endTime, Runnable onBack, Consumer<Room> onRoomSelected) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(0xFF, 0x6B, 0x6B));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(buildHeader(startTime, endTime, onBack), BorderLayout.NORTH);
        add(buildRoomList(startTime, endTime, onRoomSelected), BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
    }

    // builds the top section
    private JPanel buildHeader(LocalDateTime startTime, LocalDateTime endTime, Runnable onBack) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        // returns to the time selection screen
        RoundedButton backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        JLabel title = new JLabel("Available Rooms");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        String startText = startTime.format(DATE_TIME_FORMAT);
        String endText = endTime.format(DATE_TIME_FORMAT);

        JLabel subtitle = new JLabel(startText + "   |   " + endText);
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

    // builds the room list
    private JComponent buildRoomList(LocalDateTime startTime, LocalDateTime endTime, Consumer<Room> onRoomSelected) {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        List<Room> availableRooms = bookingController.getAvailableRooms(startTime, endTime);

        if (availableRooms.isEmpty()) {
            list.add(buildEmptyState());
        } else {
            for (int i = 0; i < availableRooms.size(); i++) {
                list.add(buildRoomCard(availableRooms.get(i), startTime, endTime, onRoomSelected));

                if (i < availableRooms.size() - 1) {
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

    // shown when there are no rooms
    private JLabel buildEmptyState() {
        JLabel empty = new JLabel("No rooms are available during the selected time");
        empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
        empty.setForeground(Colours.TEXT_MUTED);
        empty.setAlignmentX(Component.LEFT_ALIGNMENT);
        return empty;
    }

    // builds one room card
    private JPanel buildRoomCard(Room room, LocalDateTime startTime, LocalDateTime endTime, Consumer<Room> onRoomSelected) {
        JPanel card = new JPanel(new BorderLayout(16, 0));
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel nameLbl = new JLabel(room.getBuildingName() + " - Room " + room.getRoomNumber());
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        nameLbl.setForeground(Colours.TEXT_LIGHT);

        JLabel descLbl = new JLabel("Room ID: " + room.getRoomId() + "   |   Capacity: " + room.getCapacity() + " people");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLbl.setForeground(Colours.TEXT_MUTED);

        textBox.add(nameLbl);
        textBox.add(descLbl);
        card.add(textBox, BorderLayout.CENTER);

        RoundedButton selectBtn = new RoundedButton("Select", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        selectBtn.setPreferredSize(new Dimension(100, 38));
        selectBtn.addActionListener(e -> {
            if (onRoomSelected != null && bookingController.isRoomAvailable(room, startTime, endTime)) {
                onRoomSelected.accept(room);
            } else {
                setErrorLabel("This room is no longer available during the selected time.");
            }
        });

        card.add(selectBtn, BorderLayout.EAST);
        return card;
    }

    private void setErrorLabel(String message) {
        if (errorTimer != null && errorTimer.isRunning()) {
            errorTimer.stop();
        }

        errorLabel.setText(message);
        errorTimer = new Timer(3000, e -> errorLabel.setText(" "));
        errorTimer.setRepeats(false);
        errorTimer.start();
    }
}