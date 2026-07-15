package roombooking.view;

import javax.swing.*;
import java.awt.*;

import static roombooking.view.DashboardWidgets.*;

// the dashboard content shown to the right of the sidebar in AppShellFrame
// page title up top, then a row of stat cards, then quick book + calendar/clock
public class AppDashBoardPanel extends GlowBackgroundPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);

    public AppDashBoardPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        pageTitle.setForeground(Colours.TEXT_LIGHT);
        pageTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        add(pageTitle, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        // stat cards row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        // cap the height so this row does not stretch and push the cards row off screen
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        statsRow.add(buildStatCard("Total Rooms", "128", null, Glyph.CALENDAR));
        statsRow.add(buildStatCard("Active Bookings", "1,024", null, Glyph.PLAY));
        statsRow.add(buildStatCard("Registered Users", "4.2k", null, Glyph.PEOPLE));
        body.add(statsRow);
        body.add(Box.createVerticalStrut(24));

        // quick book and calendar/clock row
        JPanel cardsRow = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsRow.setOpaque(false);
        cardsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        // bumped up from 300 to give the calendar grid room to breathe
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        cardsRow.add(buildQuickBookCard());
        cardsRow.add(buildCalendarClockCard());
        body.add(cardsRow);

        add(body, BorderLayout.CENTER);
    }

    private JPanel buildStatCard(String label, String value, String trendText, Glyph icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(new Color(24, 27, 36));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        labelLbl.setForeground(Colours.TEXT_MUTED);
        header.add(labelLbl, BorderLayout.WEST);

        JComponent iconComp = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                drawGlyph(g2, icon, getWidth(), getHeight(), Colours.TEXT_MUTED);
                g2.dispose();
            }
        };
        iconComp.setPreferredSize(new Dimension(18, 18));
        header.add(iconComp, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        valueLbl.setForeground(Colours.TEXT_LIGHT);
        valueLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottom.add(valueLbl);

        if (trendText != null) {
            bottom.add(Box.createVerticalStrut(6));
            JLabel trendLbl = new JLabel("\u2191 " + trendText);
            trendLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            trendLbl.setForeground(new Color(0x4A, 0xD9, 0x8A)); // soft green, standard "positive trend" color
            trendLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            bottom.add(trendLbl);
        }

        card.add(bottom, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildQuickBookCard() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));

        JLabel title = new JLabel("Quick Book");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(12));

        JLabel body = new JLabel("<html><div style='width:320px;'>Ready to book your next room? "
                + "Find and reserve a space in real-time, with instant availability across every building.</div></html>");
        body.setFont(new Font("SansSerif", Font.PLAIN, 13));
        body.setForeground(Colours.TEXT_MUTED);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(body);
        card.add(Box.createVerticalStrut(20));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton newBookingBtn = new RoundedButton("New Booking", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        newBookingBtn.setPreferredSize(new Dimension(150, 42));
        newBookingBtn.addActionListener(e -> System.out.println("New Booking clicked"));

        RoundedButton browseBtn = new RoundedButton("Browse Rooms", Colours.TEXT_MUTED, Colours.TEXT_MUTED, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.OUTLINE);
        browseBtn.setPreferredSize(new Dimension(150, 42));
        browseBtn.addActionListener(e -> System.out.println("Browse Rooms clicked"));

        buttonRow.add(newBookingBtn);
        buttonRow.add(browseBtn);
        card.add(buttonRow);

        card.add(Box.createVerticalGlue());
        return card;
    }

    // builds calendar and clock panel
    private JPanel buildCalendarClockCard() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));

        // reuses DashboardWidgets.ClockWidget - already live-updating every second
        ClockWidget clock = new ClockWidget();
        clock.setAlignmentX(Component.LEFT_ALIGNMENT);
        clock.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.add(clock);
        card.add(Box.createVerticalStrut(16));

        // reuses DashboardWidgets.CalendarWidget - current month, today highlighted
        CalendarWidget calendar = new CalendarWidget();
        calendar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(calendar);

        card.add(Box.createVerticalGlue());
        return card;
    }
}