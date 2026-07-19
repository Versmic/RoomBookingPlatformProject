package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

// the booking time selection screen, shown after clicking create a booking in the sidebar
// only replaces the center content in AppShellFrame, the sidebar stays where it is
// this panel is used twice in a row to select start time and end time
public class AppSelectBookingTimePanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final Color ERROR_COLOR = new Color(0xFF, 0x6B, 0x6B);
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("h:mm a");
    private static final long MINIMUM_BOOKING_MINUTES = 60;

    private DatePickerWidget datePicker;

    // keeps every time slot button in one place so only one can be selected at a time
    private final List<TimeSlotButton> timeSlotButtons = new ArrayList<>();
    private TimeSlotButton selectedSlot;
    private RoundedButton confirmBtn;
    private JLabel errorLabel;

    // null on screen 1 (nothing to validate against yet). Set to the chosen
    // start LocalDateTime on screen 2, so a picked end time can be checked
    // against it before being accepted.
    private final LocalDateTime startDateTime;

    // called with (selected date, selected time) once the button on THIS screen is clicked -
    // only fires once the selection has actually passed validation
    private final BiConsumer<LocalDate, LocalTime> onConfirm;

    public AppSelectBookingTimePanel(boolean selectingEndTime, LocalDateTime startDateTime, BiConsumer<LocalDate, LocalTime> onConfirm) {
        this.startDateTime = startDateTime;
        this.onConfirm = onConfirm;

        setOpaque(false); // background comes from AppShellFrame's GlowBackgroundPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        JLabel pageTitle = new JLabel("Create a Booking");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        pageTitle.setForeground(Colours.TEXT_LIGHT);
        pageTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel pageSubtitle = new JLabel(selectingEndTime ? "Pick end time" : "Pick a date, then a time");
        pageSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pageSubtitle.setForeground(Colours.TEXT_MUTED);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        header.add(pageTitle);
        header.add(pageSubtitle);
        add(header, BorderLayout.NORTH);

        add(buildBody(selectingEndTime), BorderLayout.CENTER);
    }

    private JComponent buildBody(boolean selectingEndTime) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        row.setOpaque(false);

        //create date picker widget
        datePicker = new DatePickerWidget(LocalDate.now(), date -> {
        	//call back does nothing right now
           });
        row.add(datePicker);
        row.add(buildTimeSlotCard(selectingEndTime));

        // wrapping in a GridBagLayout with no constraints centers the row both
        // horizontally AND vertically within the available space, instead of
        // FlowLayout which only centers horizontally and sticks to the top
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(row);
        return wrapper;
    }

    private JPanel buildTimeSlotCard(boolean selectingEndTime) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        card.setPreferredSize(new Dimension(320, 480));
        card.setMaximumSize(new Dimension(320, 480));

        JLabel label = new JLabel("Available Times");
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(Colours.TEXT_LIGHT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(label);
        card.add(Box.createVerticalStrut(14));

        card.add(buildTimeSlotGrid());
        card.add(Box.createVerticalStrut(8));

        // reserves its own space (even when blank) so the layout does not jump
        // around the moment an error message appears - same trick used on LoginScreen
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(8));

        confirmBtn = new RoundedButton(selectingEndTime ? "Confirm Booking" : "Select End Date",
                Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        confirmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        confirmBtn.setEnabled(false); // stays disabled until a time slot is actually picked
        confirmBtn.addActionListener(e -> attemptConfirm());
        card.add(confirmBtn);

        card.add(Box.createVerticalGlue());
        return card;
    }

    // a fixed set of half hour slots for now, swap for real availability data once that exists
    private JComponent buildTimeSlotGrid() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime cursor = LocalTime.of(9, 0);
        LocalTime last = LocalTime.of(16, 30);
        while (!cursor.isAfter(last)) {
            slots.add(cursor);
            cursor = cursor.plusMinutes(30);
        }

        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (LocalTime slotTime : slots) {
            TimeSlotButton button = new TimeSlotButton(slotTime);
            button.addActionListener(e -> selectTimeSlot(button));
            timeSlotButtons.add(button);
            grid.add(button);
        }
        return grid;
    }

    // picking a time just highlights it and unlocks the confirm/next button -
    // it does NOT navigate away immediately, so the highlight is actually visible.
    // any previous error message is cleared, since the person is actively fixing their pick
    private void selectTimeSlot(TimeSlotButton button) {
        errorLabel.setText(" ");
        if (selectedSlot != null) selectedSlot.setSelectedState(false);
        selectedSlot = button;
        selectedSlot.setSelectedState(true);
        confirmBtn.setEnabled(true);
    }

    // validates time selection (if this is the end-time screen)
    private void attemptConfirm() {
        if (selectedSlot == null || onConfirm == null) return;

        LocalDate selectedDate = datePicker.getSelectedDate();
        LocalTime selectedTime = selectedSlot.getTime();

        if (startDateTime != null) {
            LocalDateTime candidateEnd = LocalDateTime.of(selectedDate, selectedTime);

            if (!candidateEnd.isAfter(startDateTime)) {
                errorLabel.setText("End date and time must be after the start date and time");
                return;
            }

            long minutesBetween = Duration.between(startDateTime, candidateEnd).toMinutes();
            if (minutesBetween < MINIMUM_BOOKING_MINUTES) {
                errorLabel.setText("Booking must be at least 1 hour long");
                return;
            }
        }

        errorLabel.setText(" ");
        onConfirm.accept(selectedDate, selectedTime);
    }

    // a single selectable time slot, purple filled when selected, subtle outline otherwise
    private static class TimeSlotButton extends JButton {
        private final LocalTime time;
        private boolean selected = false;

        TimeSlotButton(LocalTime time) {
            super(time.format(DISPLAY_FORMAT));
            this.time = time;
            setMargin(new Insets(0, 0, 0, 0)); // same JButton margin fix as DayCell - avoids text truncating to "..."
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("SansSerif", Font.PLAIN, 12));
            setForeground(Colours.TEXT_MUTED);
            setPreferredSize(new Dimension(120, 38));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        LocalTime getTime() {
            return time;
        }

        void setSelectedState(boolean selected) {
            this.selected = selected;
            setForeground(selected ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            if (selected) {
                g2.setPaint(new GradientPaint(0, 0, Colours.PURPLE, w, 0, Colours.BLUE));
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
            } else {
                g2.setColor(new Color(0x1B, 0x22, 0x33));
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
                g2.setColor(CARD_BORDER);
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 10, 10));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}