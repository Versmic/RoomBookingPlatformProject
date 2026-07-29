package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * DatePickerWidget
 * ----------------
 * A self-contained date picker: a purple header showing the currently
 * selected date, a month/year navigation row (dropdowns for jumping
 * directly, arrows for stepping one month at a time), and a day grid
 * where today gets an outline circle and the selected day gets a filled
 * circle - styled dark to match the rest of the app instead of the usual
 * white Material date picker background.
 *
 * Dates before today are shown greyed out and are not clickable, so it's
 * not possible to select an invalid (past) booking date from this widget
 * in the first place.
 */
public class DatePickerWidget extends JPanel {

    private static final Color HEADER_TEXT_MUTED = new Color(255, 255, 255, 170);
    private static final Color PAST_DATE_COLOR = new Color(255, 255, 255, 55); // dim, unclickable

    private LocalDate selectedDate;
    private YearMonth displayedMonth; // the month currently shown in the grid, independent of selectedDate

    private final JLabel headerDateLabel;
    private final JLabel monthYearLabel;
    private final JPanel dayGridPanel;

    private final Consumer<LocalDate> onDateSelected;

    public DatePickerWidget(LocalDate initialDate, Consumer<LocalDate> onDateSelected) {
        this.selectedDate = initialDate;
        this.displayedMonth = YearMonth.from(initialDate);
        this.onDateSelected = onDateSelected;

        setOpaque(false);
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(360, 420));
        setPreferredSize(new Dimension(360, 420));

        // ---- header: gradient bar with the selected date spelled out ----
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS)) ;
        header.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel smallLabel = new JLabel("SELECT DATE");
        smallLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        smallLabel.setForeground(HEADER_TEXT_MUTED);
        smallLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerDateLabel = new JLabel();
        headerDateLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerDateLabel.setForeground(Colours.TEXT_LIGHT);
        headerDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(smallLabel);
        header.add(Box.createVerticalStrut(6));
        header.add(headerDateLabel);

        // paint the purple -> blue gradient behind the header text
        JPanel gradientHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                // opaque is false on this panel (set below) so JPanel's default
                // paintComponent would be a no-op - safe to call super first,
                // then draw our gradient on top without it getting erased
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, Colours.PURPLE, getWidth(), getHeight(), Colours.BLUE));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        gradientHeader.setOpaque(false); // critical - true here would paint a flat bg AFTER our gradient and hide it
        header.setOpaque(false);
        gradientHeader.add(header, BorderLayout.CENTER);
        add(gradientHeader, BorderLayout.NORTH);

        // ---- body: month navigation + day grid, dark card background ----
        JPanel body = new JPanel();
        body.setOpaque(true);
        body.setBackground(new Color(0x1B, 0x22, 0x33));
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(14, 16, 16, 16));

        // plain text showing where we are in the calendar - "July 2026" - sits
        // above the dropdowns so it's readable at a glance without having to
        // parse the dropdown selections themselves
        monthYearLabel = new JLabel();
        monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        monthYearLabel.setForeground(Colours.TEXT_LIGHT);
        monthYearLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(monthYearLabel);
        body.add(Box.createVerticalStrut(8));

        // month/year dropdowns + prev/next arrows
        JPanel navRow = new JPanel(new BorderLayout());
        navRow.setOpaque(false);
        navRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        navRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JPanel arrows = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        arrows.setOpaque(false);
        JButton prevMonthBtn = buildArrowButton(false);
        JButton nextMonthBtn = buildArrowButton(true);
        prevMonthBtn.addActionListener(e -> {
            displayedMonth = displayedMonth.minusMonths(1);
            refreshGrid();
        });
        nextMonthBtn.addActionListener(e -> {
            displayedMonth = displayedMonth.plusMonths(1);
            refreshGrid();
        });
        arrows.add(prevMonthBtn);
        arrows.add(nextMonthBtn);
        navRow.add(arrows, BorderLayout.EAST);

        body.add(navRow);
        body.add(Box.createVerticalStrut(10));

        // day-of-week header row
        JPanel weekHeader = new JPanel(new GridLayout(1, 7, 2, 2));
        weekHeader.setOpaque(false);
        weekHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        weekHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        for (String d : new String[]{"S", "M", "T", "W", "T", "F", "S"}) {
            JLabel dl = new JLabel(d, SwingConstants.CENTER);
            dl.setFont(new Font("SansSerif", Font.BOLD, 11));
            dl.setForeground(Colours.TEXT_MUTED);
            weekHeader.add(dl);
        }
        body.add(weekHeader);
        body.add(Box.createVerticalStrut(6));

        dayGridPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        dayGridPanel.setOpaque(false);
        dayGridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(dayGridPanel);

        add(body, BorderLayout.CENTER);

        updateHeaderLabel();
        refreshGrid();
    }

    /** a small chevron button, drawn with Graphics2D rather than a font character so it always renders correctly */
    private JButton buildArrowButton(boolean pointsRight) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colours.TEXT_LIGHT);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int w = getWidth(), h = getHeight();
                if (pointsRight) {
                    g2.draw(new Line2D.Double(w * 0.38, h * 0.25, w * 0.62, h * 0.5));
                    g2.draw(new Line2D.Double(w * 0.62, h * 0.5, w * 0.38, h * 0.75));
                } else {
                    g2.draw(new Line2D.Double(w * 0.62, h * 0.25, w * 0.38, h * 0.5));
                    g2.draw(new Line2D.Double(w * 0.38, h * 0.5, w * 0.62, h * 0.75));
                }
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(26, 26));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateHeaderLabel() {
        headerDateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d")));
    }

    /** rebuilds the day grid for whatever month is currently displayed */
    private void refreshGrid() {
        monthYearLabel.setText(displayedMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + displayedMonth.getYear());

        dayGridPanel.removeAll();

        LocalDate today = LocalDate.now();
        LocalDate firstOfMonth = displayedMonth.atDay(1);
        int leadingBlanks = firstOfMonth.getDayOfWeek().getValue() % 7; // sunday-first grid

        for (int i = 0; i < leadingBlanks; i++) {
            dayGridPanel.add(new JLabel(""));
        }

        // displayedMonth.lengthOfMonth() is from java.time and already implements
        // real Gregorian leap year rules (divisible by 4, except centuries, except
        // every 400th year) - so February correctly gets 28 or 29 days with no
        // extra code needed here
        for (int day = 1; day <= displayedMonth.lengthOfMonth(); day++) {
            LocalDate cellDate = displayedMonth.atDay(day);
            boolean isToday = cellDate.equals(today);
            boolean isSelected = cellDate.equals(selectedDate);
            boolean isPast = cellDate.isBefore(today); // can't book a room in the past

            DayCell cell = new DayCell(day, isToday, isSelected, isPast);
            if (!isPast) {
                cell.addActionListener(e -> {
                    selectedDate = cellDate;
                    updateHeaderLabel();
                    refreshGrid(); // rebuild so the filled circle moves to the new selection
                    if (onDateSelected != null) onDateSelected.accept(selectedDate);
                });
            }
            dayGridPanel.add(cell);
        }

        dayGridPanel.revalidate();
        dayGridPanel.repaint();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    /**
     * a single day number - filled purple circle when selected, outline circle
     * when it's today, plain otherwise. Past dates are dimmed and disabled so
     * they simply can't be clicked - the cleanest way to keep an invalid date
     * from ever being chosen in the first place.
     */
    private static class DayCell extends JButton {
        private final boolean today;
        private final boolean selected;

        DayCell(int day, boolean today, boolean selected, boolean pastDate) {
            super(String.valueOf(day));
            this.today = today;
            this.selected = selected;
            setPreferredSize(new Dimension(34, 34));
            setMargin(new Insets(0, 0, 0, 0)); // JButton's default margin was eating most of the cell width,
                                                // which is why "10", "11" etc were rendering as "..."
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("SansSerif", Font.PLAIN, 12));

            if (pastDate) {
                setEnabled(false); // blocks clicks entirely, not just visual styling
                setForeground(PAST_DATE_COLOR);
                setCursor(Cursor.getDefaultCursor());
            } else {
                setForeground(selected ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int d = Math.min(getWidth(), getHeight()) - 4;
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;

            if (selected) {
                g2.setColor(Colours.PURPLE);
                g2.fill(new Ellipse2D.Double(x, y, d, d));
            } else if (today) {
                g2.setColor(Colours.TEXT_MUTED);
                g2.setStroke(new BasicStroke(1.4f));
                g2.draw(new Ellipse2D.Double(x + 1, y + 1, d - 2, d - 2));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}