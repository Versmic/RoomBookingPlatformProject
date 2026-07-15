package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import static roombooking.view.DashboardWidgets.*;

// the sidebar shown on the left of AppShellFrame
// logo up top, nav list in the middle, user info and sign out pinned to the bottom
public class AppSideBarPanel extends JPanel {

    private static final Color SIDEBAR_BG = new Color(0x12, 0x14, 0x1C);

    private final String userDisplayName;

    public AppSideBarPanel(String userDisplayName, Runnable onSignOut) {
        this.userDisplayName = userDisplayName;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(SIDEBAR_BG);
        // fixed width so the sidebar never gets squeezed by the content area
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 16, 16, 16));

        // logo row
        JPanel logoRow = new JPanel(new BorderLayout());
        logoRow.setOpaque(false);
        logoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel logoLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        logoLeft.setOpaque(false);
        logoLeft.add(buildLogoMark());
        JLabel appName = new JLabel("Room Booking");
        appName.setFont(new Font("SansSerif", Font.BOLD, 14));
        appName.setForeground(Colours.TEXT_LIGHT);
        logoLeft.add(appName);
        logoRow.add(logoLeft, BorderLayout.WEST);

        add(logoRow);
        add(Box.createVerticalStrut(28));

        // use helper method to make side bar buttons
        // uses DashboardWidgets class for the icons on the sidebar
        NavItem dashboardItem = new NavItem(Glyph.GRID, "Dashboard", true);
        NavItem bookItem = new NavItem(Glyph.PLUS, "Book a Room", false);
        NavItem bookingsItem = new NavItem(Glyph.LIST, "My Bookings", false);
        NavItem settingsItem = new NavItem(Glyph.SETTINGS, "Settings", false);

        // TODO: wire these up once the Book a Room / My Bookings / Settings screens exist
        bookItem.addActionListener(e -> System.out.println("Book a Room clicked"));
        bookingsItem.addActionListener(e -> System.out.println("My Bookings clicked"));
        settingsItem.addActionListener(e -> System.out.println("Settings clicked"));

        for (NavItem item : new NavItem[]{dashboardItem, bookItem, bookingsItem, settingsItem}) {
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            add(item);
            add(Box.createVerticalStrut(4));
        }

        // pushes the user footer to the bottom
        add(Box.createVerticalGlue());

        // divider
        JPanel divider = new JPanel();
        divider.setBackground(new Color(255, 255, 255, 20));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(divider);
        add(Box.createVerticalStrut(12));

        JPanel userRow = new JPanel(new BorderLayout(10, 0));
        userRow.setOpaque(false);
        userRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        userRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        userRow.add(buildAvatarInitial(), BorderLayout.WEST);

        JPanel userText = new JPanel();
        userText.setOpaque(false);
        userText.setLayout(new BoxLayout(userText, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(userDisplayName);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameLabel.setForeground(Colours.TEXT_LIGHT);
        JLabel emailLabel = new JLabel(userDisplayName.toLowerCase().replace(" ", ".") + "@example.com");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        emailLabel.setForeground(Colours.TEXT_MUTED);
        userText.add(nameLabel);
        userText.add(emailLabel);
        userRow.add(userText, BorderLayout.CENTER);

        add(userRow);
        add(Box.createVerticalStrut(10));

        // sign out
        JButton signOut = new JButton("Sign out");
        signOut.setAlignmentX(Component.LEFT_ALIGNMENT);
        signOut.setContentAreaFilled(false);
        signOut.setBorderPainted(false);
        signOut.setFocusPainted(false);
        signOut.setForeground(Colours.TEXT_MUTED);
        signOut.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signOut.setHorizontalAlignment(SwingConstants.LEFT);
        // just runs whatever callback AppShellFrame handed us, sidebar does not know about the frame itself
        signOut.addActionListener(e -> onSignOut.run());
        add(signOut);
    }

    private JComponent buildLogoMark() {
        JComponent mark = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, Colours.PURPLE, getWidth(), getHeight(), Colours.BLUE));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                drawGlyph(g2, Glyph.HOME, getWidth(), getHeight(), Colours.TEXT_LIGHT);
                g2.dispose();
            }
        };
        mark.setPreferredSize(new Dimension(28, 28));
        return mark;
    }

    private JComponent buildAvatarInitial() {
        String initial = userDisplayName.isEmpty() ? "?" : userDisplayName.substring(0, 1).toUpperCase();
        JComponent avatar = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
                g2.setColor(Colours.TEXT_LIGHT);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(initial)) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initial, tx, ty);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        return avatar;
    }

    // makes side bar button
    private static class NavItem extends JButton {
        private final Glyph glyph;
        private final boolean active;
        private float hoverProgress = 0f;
        private Timer hoverTimer;

        NavItem(Glyph glyph, String text, boolean active) {
            super(text);
            this.glyph = glyph;
            this.active = active;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setForeground(active ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
            setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setIconTextGap(10);
            setBorder(BorderFactory.createEmptyBorder(0, 34, 0, 10));

            if (!active) {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { animateHoverTo(1f); }
                    @Override public void mouseExited(MouseEvent e) { animateHoverTo(0f); }
                });
            }
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

            if (active) {
                g2.setPaint(new GradientPaint(0, 0, Colours.PURPLE, w, 0, Colours.BLUE));
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
            } else if (hoverProgress > 0.01f) {
                g2.setColor(new Color(255, 255, 255, (int) (hoverProgress * 20)));
                g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 10, 10));
            }

            int iconSize = 16;
            int iconY = (h - iconSize) / 2;
            g2.translate(10, iconY);
            drawGlyph(g2, glyph, iconSize, iconSize, active ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
            g2.translate(-10, -iconY);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}