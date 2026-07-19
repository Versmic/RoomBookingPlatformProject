package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import static roombooking.view.DashboardWidgets.*;

// the sidebar shown on the left of AppShellFrame
public class AppSideBarPanel extends JPanel {

    private static final Color SIDEBAR_BG = new Color(0x12, 0x14, 0x1C);

    private final String userDisplayName;

    private final NavItem dashboardItem;
    private final NavItem bookItem;
    private final NavItem bookingsItem;
    private final NavItem generateAdminItem;
    private final NavItem roomManagementItem;

    public AppSideBarPanel(String userDisplayName, String email, boolean isChief, boolean canManageRooms, Runnable onSignOut, Runnable onDashboard, Runnable onCreateBooking, Runnable onMyBookings, Runnable onGenerateAdmin, Runnable onRoomManagement) {
        this.userDisplayName = userDisplayName;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 16, 16, 16));

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

        dashboardItem = new NavItem(Glyph.GRID, "Dashboard", true);
        bookItem = new NavItem(Glyph.PLUS, "Create a Booking", false);
        bookingsItem = new NavItem(Glyph.LIST, "My Bookings", false);
        generateAdminItem = isChief ? new NavItem(Glyph.PLUS, "Generate Admin Account", false) : null;
        roomManagementItem = canManageRooms ? new NavItem(Glyph.SETTINGS, "Room Management", false) : null;

        dashboardItem.addActionListener(e -> {
            setActiveItem(dashboardItem);
            onDashboard.run();
        });

        bookItem.addActionListener(e -> {
            setActiveItem(bookItem);
            onCreateBooking.run();
        });

        bookingsItem.addActionListener(e -> {
            setActiveItem(bookingsItem);
            onMyBookings.run();
        });

        if (generateAdminItem != null) {
            generateAdminItem.addActionListener(e -> {
                setActiveItem(generateAdminItem);
                onGenerateAdmin.run();
            });
        }

        if (roomManagementItem != null) {
            roomManagementItem.addActionListener(e -> {
                setActiveItem(roomManagementItem);
                onRoomManagement.run();
            });
        }

        for (NavItem item : new NavItem[] { dashboardItem, bookItem, bookingsItem, generateAdminItem, roomManagementItem }) {
            if (item == null) continue;
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            add(item);
            add(Box.createVerticalStrut(4));
        }

        add(Box.createVerticalGlue());

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

        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        emailLabel.setForeground(Colours.TEXT_MUTED);

        userText.add(nameLabel);
        userText.add(emailLabel);
        userRow.add(userText, BorderLayout.CENTER);

        add(userRow);
        add(Box.createVerticalStrut(10));

        JButton signOut = new JButton("Sign out");
        signOut.setAlignmentX(Component.LEFT_ALIGNMENT);
        signOut.setContentAreaFilled(false);
        signOut.setBorderPainted(false);
        signOut.setFocusPainted(false);
        signOut.setForeground(Colours.TEXT_MUTED);
        signOut.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signOut.setHorizontalAlignment(SwingConstants.LEFT);
        signOut.addActionListener(e -> onSignOut.run());

        add(signOut);
    }

    // updates which sidebar item is highlighted
    private void setActiveItem(NavItem selectedItem) {
        dashboardItem.setActive(selectedItem == dashboardItem);
        bookItem.setActive(selectedItem == bookItem);
        bookingsItem.setActive(selectedItem == bookingsItem);
        if (generateAdminItem != null) generateAdminItem.setActive(selectedItem == generateAdminItem);
        if (roomManagementItem != null) roomManagementItem.setActive(selectedItem == roomManagementItem);
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

    // one sidebar navigation button
    private static class NavItem extends JButton {

        private final Glyph glyph;
        private boolean active;
        private float hoverProgress;
        private Timer hoverTimer;

        NavItem(Glyph glyph, String text, boolean active) {
            super(text);

            this.glyph = glyph;
            this.active = active;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setIconTextGap(10);
            setBorder(BorderFactory.createEmptyBorder(0, 34, 0, 10));

            updateAppearance();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!NavItem.this.active) animateHoverTo(1f);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateHoverTo(0f);
                }
            });
        }

        // changes the highlighted state
        private void setActive(boolean active) {
            this.active = active;

            if (active) {
                hoverProgress = 0f;
                if (hoverTimer != null) hoverTimer.stop();
            }

            updateAppearance();
            repaint();
        }

        private void updateAppearance() {
            setForeground(active ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
            setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        }

        private void animateHoverTo(float target) {
            if (active) return;
            if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();

            hoverTimer = new Timer(10, null);

            hoverTimer.addActionListener(e -> {
                hoverProgress += (target - hoverProgress) * 0.3f;

                if (Math.abs(target - hoverProgress) < 0.01f) {
                    hoverProgress = target;
                    hoverTimer.stop();
                }

                repaint();
            });

            hoverTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (active) {
                g2.setPaint(new GradientPaint(0, 0, Colours.PURPLE, width, 0, Colours.BLUE));
                g2.fill(new RoundRectangle2D.Double(0, 0, width, height, 10, 10));
            } else if (hoverProgress > 0.01f) {
                g2.setColor(new Color(255, 255, 255, (int) (hoverProgress * 20)));
                g2.fill(new RoundRectangle2D.Double(0, 0, width, height, 10, 10));
            }

            int iconSize = 16;
            int iconY = (height - iconSize) / 2;

            g2.translate(10, iconY);
            drawGlyph(g2, glyph, iconSize, iconSize, active ? Colours.TEXT_LIGHT : Colours.TEXT_MUTED);
            g2.translate(-10, -iconY);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}