package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * DashboardWidgets
 * ----------------
 * Small reusable pieces used to build DashboardFrame: a translucent "glass"
 * card panel, a circular icon button with hand-drawn glyphs, a live clock,
 * and a month calendar grid. Kept package-private since only DashboardFrame
 * needs to reference them directly.
 */
final class DashboardWidgets {

    private DashboardWidgets() {}

    // translucent rounded card
    static class GlassPanel extends JPanel {
        private final int arc;

        GlassPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // frosted dark fill
            g2.setColor(new Color(20, 24, 34, 150));
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, arc, arc));

            // faint top highlight border, like light catching a glass edge
            g2.setColor(new Color(255, 255, 255, 35));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, arc, arc));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // icons 
    enum Glyph { HOME, SEARCH, CALENDAR, STAR, SETTINGS, LIST, PLUS, LOGOUT, GRID, PEOPLE, PLAY, CHEVRON_RIGHT }

    static void drawGlyph(Graphics2D g2, Glyph glyph, int w, int h, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        switch (glyph) {
            case HOME -> {
                Path2D roof = new Path2D.Double();
                roof.moveTo(w * 0.5, h * 0.15);
                roof.lineTo(w * 0.85, h * 0.45);
                roof.lineTo(w * 0.15, h * 0.45);
                roof.closePath();
                g2.draw(roof);
                g2.draw(new RoundRectangle2D.Double(w * 0.25, h * 0.45, w * 0.5, h * 0.4, 2, 2));
            }
            case SEARCH -> {
                g2.draw(new Ellipse2D.Double(w * 0.18, h * 0.18, w * 0.5, h * 0.5));
                g2.draw(new Line2D.Double(w * 0.62, h * 0.62, w * 0.85, h * 0.85));
            }
            case CALENDAR -> {
                g2.draw(new RoundRectangle2D.Double(w * 0.15, h * 0.22, w * 0.7, h * 0.63, 4, 4));
                g2.draw(new Line2D.Double(w * 0.15, h * 0.40, w * 0.85, h * 0.40));
                g2.draw(new Line2D.Double(w * 0.32, h * 0.14, w * 0.32, h * 0.30));
                g2.draw(new Line2D.Double(w * 0.68, h * 0.14, w * 0.68, h * 0.30));
            }
            case STAR -> {
                Path2D star = new Path2D.Double();
                double cx = w / 2.0, cy = h / 2.0;
                double outerR = w * 0.42, innerR = w * 0.18;
                for (int i = 0; i < 10; i++) {
                    double r = (i % 2 == 0) ? outerR : innerR;
                    double angle = Math.PI / 2 + i * Math.PI / 5;
                    double x = cx + r * Math.cos(angle);
                    double y = cy - r * Math.sin(angle);
                    if (i == 0) star.moveTo(x, y); else star.lineTo(x, y);
                }
                star.closePath();
                g2.draw(star);
            }
            case SETTINGS -> {
                g2.draw(new Ellipse2D.Double(w * 0.30, h * 0.30, w * 0.4, h * 0.4));
                double cx = w / 2.0, cy = h / 2.0;
                for (int i = 0; i < 6; i++) {
                    double angle = i * Math.PI / 3;
                    double x1 = cx + Math.cos(angle) * w * 0.32;
                    double y1 = cy + Math.sin(angle) * h * 0.32;
                    double x2 = cx + Math.cos(angle) * w * 0.46;
                    double y2 = cy + Math.sin(angle) * h * 0.46;
                    g2.draw(new Line2D.Double(x1, y1, x2, y2));
                }
            }
            case LIST -> {
                for (int i = 0; i < 3; i++) {
                    double y = h * (0.28 + i * 0.24);
                    g2.fill(new Ellipse2D.Double(w * 0.15, y - 2, 4, 4));
                    g2.draw(new Line2D.Double(w * 0.30, y, w * 0.85, y));
                }
            }
            case PLUS -> {
                g2.draw(new Line2D.Double(w * 0.5, h * 0.18, w * 0.5, h * 0.82));
                g2.draw(new Line2D.Double(w * 0.18, h * 0.5, w * 0.82, h * 0.5));
            }
            case LOGOUT -> {
                g2.draw(new RoundRectangle2D.Double(w * 0.18, h * 0.18, w * 0.35, h * 0.64, 3, 3));
                g2.draw(new Line2D.Double(w * 0.40, h * 0.5, w * 0.85, h * 0.5));
                g2.draw(new Line2D.Double(w * 0.68, h * 0.34, w * 0.85, h * 0.5));
                g2.draw(new Line2D.Double(w * 0.68, h * 0.66, w * 0.85, h * 0.5));
            }
            case GRID -> {
                double gap = w * 0.08;
                double cell = (w - gap * 3) / 2.0;
                g2.draw(new RoundRectangle2D.Double(gap, gap, cell, cell, 2, 2));
                g2.draw(new RoundRectangle2D.Double(gap * 2 + cell, gap, cell, cell, 2, 2));
                g2.draw(new RoundRectangle2D.Double(gap, gap * 2 + cell, cell, cell, 2, 2));
                g2.draw(new RoundRectangle2D.Double(gap * 2 + cell, gap * 2 + cell, cell, cell, 2, 2));
            }
            case PEOPLE -> {
                g2.draw(new Ellipse2D.Double(w * 0.20, h * 0.16, w * 0.36, w * 0.36));
                g2.draw(new Arc2D.Double(w * 0.08, h * 0.55, w * 0.60, h * 0.4, 0, 180, Arc2D.OPEN));
                g2.draw(new Ellipse2D.Double(w * 0.50, h * 0.24, w * 0.30, w * 0.30));
                g2.draw(new Arc2D.Double(w * 0.42, h * 0.60, w * 0.50, h * 0.34, 0, 180, Arc2D.OPEN));
            }
            case PLAY -> {
                Path2D tri = new Path2D.Double();
                tri.moveTo(w * 0.28, h * 0.20);
                tri.lineTo(w * 0.28, h * 0.80);
                tri.lineTo(w * 0.80, h * 0.5);
                tri.closePath();
                g2.fill(tri);
            }
            case CHEVRON_RIGHT -> {
                g2.draw(new Line2D.Double(w * 0.35, h * 0.22, w * 0.68, h * 0.5));
                g2.draw(new Line2D.Double(w * 0.68, h * 0.5, w * 0.35, h * 0.78));
            }
        }
    }

    // -----------------------------------------------------------------
    // ICON BUTTON - circular glass button with a glyph, subtle hover glow
    // -----------------------------------------------------------------
    static class IconButton extends JButton {
        private final Glyph glyph;
        private final Color glyphColor;
        private float hoverProgress = 0f;
        private Timer hoverTimer;

        IconButton(Glyph glyph, Color glyphColor) {
            this.glyph = glyph;
            this.glyphColor = glyphColor;
            setPreferredSize(new Dimension(42, 42));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { animateHoverTo(1f); }
                @Override public void mouseExited(MouseEvent e) { animateHoverTo(0f); }
            });
        }

        private void animateHoverTo(float target) {
            if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();
            hoverTimer = new Timer(10, null);
            hoverTimer.addActionListener(e -> {
                hoverProgress += (target - hoverProgress) * 0.3f;
                if (Math.abs(target - hoverProgress) < 0.01f) { hoverProgress = target; hoverTimer.stop(); }
                repaint();
            });
            hoverTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int alpha = (int) (30 + hoverProgress * 60);
            g2.setColor(new Color(255, 255, 255, alpha));
            g2.fill(new Ellipse2D.Double(1, 1, w - 2, h - 2));

            int pad = (int) (w * 0.22);
            g2.translate(pad, pad); // move origin into the padded inner box before drawing
            drawGlyph(g2, glyph, w - pad * 2, h - pad * 2, glyphColor);
            g2.dispose();
        }
    }

    // -----------------------------------------------------------------
    // CLOCK WIDGET - live time + date, updates once a second
    // -----------------------------------------------------------------
    static class ClockWidget extends GlassPanel {
        private final JLabel timeLabel;
        private final JLabel dateLabel;

        ClockWidget() {
            super(22);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

            timeLabel = new JLabel();
            timeLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
            timeLabel.setForeground(Colours.TEXT_LIGHT);
            timeLabel.setAlignmentX(LEFT_ALIGNMENT);

            dateLabel = new JLabel();
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            dateLabel.setForeground(Colours.TEXT_MUTED);
            dateLabel.setAlignmentX(LEFT_ALIGNMENT);

            add(timeLabel);
            add(Box.createVerticalStrut(4));
            add(dateLabel);

            updateNow();
            Timer clock = new Timer(1000, e -> updateNow());
            clock.start();
        }

        private void updateNow() {
            LocalTime now = LocalTime.now();
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));
            LocalDate today = LocalDate.now();
            String day = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            dateLabel.setText(day + " " + today.format(DateTimeFormatter.ofPattern("MM/dd")));
        }
    }

    // -----------------------------------------------------------------
    // CALENDAR WIDGET - current month grid with today highlighted
    // -----------------------------------------------------------------
    static class CalendarWidget extends GlassPanel {
        CalendarWidget() {
            super(22);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

            YearMonth month = YearMonth.now();
            LocalDate today = LocalDate.now();

            JLabel monthLabel = new JLabel(month.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
            monthLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            monthLabel.setForeground(Colours.TEXT_LIGHT);
            monthLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
            add(monthLabel, BorderLayout.NORTH);

            JPanel grid = new JPanel(new GridLayout(0, 7, 4, 4));
            grid.setOpaque(false);

            String[] headers = {"S", "M", "T", "W", "T", "F", "S"};
            for (String hd : headers) {
                JLabel hl = new JLabel(hd, SwingConstants.CENTER);
                hl.setFont(new Font("SansSerif", Font.BOLD, 11));
                hl.setForeground(Colours.TEXT_MUTED);
                grid.add(hl);
            }

            LocalDate first = month.atDay(1);
            int leadingBlanks = first.getDayOfWeek().getValue() % 7; // Sunday-first grid
            for (int i = 0; i < leadingBlanks; i++) grid.add(new JLabel(""));

            for (int day = 1; day <= month.lengthOfMonth(); day++) {
                JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
                dayLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                boolean isToday = day == today.getDayOfMonth();
                dayLabel.setForeground(isToday ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
                if (isToday) {
                    dayLabel.setOpaque(false);
                    dayLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    dayLabel = wrapWithTodayCircle(dayLabel, day);
                }
                grid.add(dayLabel);
            }

            add(grid, BorderLayout.CENTER);
        }

        /** Wraps today's number in a small filled accent circle by returning a custom-painted label. */
        private JLabel wrapWithTodayCircle(JLabel base, int day) {
            return new JLabel(String.valueOf(day), SwingConstants.CENTER) {
                { setFont(base.getFont()); setForeground(Colours.TEXT_LIGHT); }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Colours.PURPLE);
                    int d = Math.min(getWidth(), getHeight());
                    g2.fill(new Ellipse2D.Double((getWidth() - d) / 2.0, (getHeight() - d) / 2.0, d, d));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
        }
    }

    // -----------------------------------------------------------------
    // GLOW BACKGROUND PANEL - the same navy + glow-circle look used by
    // DashboardFrame's fallback background, factored out for reuse by
    // other windows/content areas.
    // -----------------------------------------------------------------
    static class GlowBackgroundPanel extends JPanel {
        GlowBackgroundPanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            g2.setColor(Colours.BG_NAVY);
            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(Colours.PURPLE.getRed(), Colours.PURPLE.getGreen(), Colours.PURPLE.getBlue(), 45));
            g2.fill(new Ellipse2D.Double(-w * 0.10, -h * 0.15, w * 0.55, w * 0.55));

            g2.setColor(new Color(Colours.BLUE.getRed(), Colours.BLUE.getGreen(), Colours.BLUE.getBlue(), 40));
            g2.fill(new Ellipse2D.Double(w * 0.65, h * 0.55, w * 0.5, w * 0.5));

            g2.dispose();
        }
    }
}