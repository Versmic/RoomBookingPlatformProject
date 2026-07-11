package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

/**
 * RoundedField
 * ------------
 * A rounded, dark "card" input field with a small hand-drawn icon on the
 * left and a borderless text field (with placeholder support) on the
 * right - matches the input style from the login screen reference image.
 * Also supports the same scale+fade pop-in entrance animation used by
 * WelcomeScreen.RoundedButton, so form fields can cascade in alongside
 * the buttons.
 */
public class RoundedField extends JPanel {

    public enum IconType { EMAIL, PASSWORD }

    private float entranceProgress = 1f;
    private Timer entranceTimer;

    public RoundedField(JTextField field, IconType iconType) {
        setOpaque(false);
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16)); // inner padding around icon + text

        JComponent icon = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(1.6f));
                g2.setColor(Colours.TEXT_MUTED);

                int w = getWidth();
                int h = getHeight();

                if (iconType == IconType.EMAIL) {
                    // simple envelope: outline rectangle + a "V" fold line
                    g2.draw(new RoundRectangle2D.Double(1, 3, w - 3, h - 7, 3, 3));
                    g2.draw(new Line2D.Double(1, 4, w / 2.0, h / 2.0 + 1));
                    g2.draw(new Line2D.Double(w - 2, 4, w / 2.0, h / 2.0 + 1));
                } else {
                    // simple padlock: arc shackle + filled rounded body
                    g2.draw(new Arc2D.Double(w * 0.20, 0, w * 0.6, h * 0.65, 0, 180, Arc2D.OPEN));
                    g2.fill(new RoundRectangle2D.Double(w * 0.12, h * 0.42, w * 0.76, h * 0.5, 4, 4));
                }

                g2.dispose();
            }
        };
        icon.setOpaque(false);
        icon.setPreferredSize(new Dimension(22, 22));

        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setForeground(Colours.TEXT_LIGHT);
        field.setCaretColor(Colours.TEXT_LIGHT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));

        add(icon, BorderLayout.WEST);
        add(field, BorderLayout.CENTER);
    }

    /** Plays the same pop-in entrance animation used by RoundedButton, ~250ms ease-out. */
    public void playEntranceAnimation() {
        if (entranceTimer != null && entranceTimer.isRunning()) entranceTimer.stop();

        long startTime = System.currentTimeMillis();
        int durationMs = 250;
        entranceProgress = 0f;

        entranceTimer = new Timer(12, null);
        entranceTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float t = Math.min(1f, elapsed / (float) durationMs);
            float f = t - 1f;
            entranceProgress = f * f * f + 1f; // ease-out cubic
            repaint();
            if (t >= 1f) entranceTimer.stop();
        });
        entranceTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (entranceProgress < 1f) {
            float scaleX = 0.85f + 0.15f * entranceProgress;
            int centerX = w / 2;
            g2.translate(centerX, 0);
            g2.scale(scaleX, 1.0);
            g2.translate(-centerX, 0);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, Math.min(1f, entranceProgress))));
        }

        g2.setColor(new Color(0x1B, 0x22, 0x33)); // dark card fill, slightly lighter than the navy background
        g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 18, 18));

        g2.setColor(new Color(255, 255, 255, 22)); // faint border for definition against the background
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(0.5, 0.5, w - 1, h - 1, 18, 18));

        super.paintComponent(g2); // paint the icon + text field within the same transform/fade
        g2.dispose();
    }

    /** JTextField that draws placeholder text (in TEXT_MUTED) whenever it's empty. */
    public static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colours.TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(placeholder, 2, y);
                g2.dispose();
            }
        }
    }

    /** JPasswordField that draws placeholder text (in TEXT_MUTED) whenever it's empty. */
    public static class PlaceholderPasswordField extends JPasswordField {
        private final String placeholder;

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colours.TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(placeholder, 2, y);
                g2.dispose();
            }
        }
    }
}