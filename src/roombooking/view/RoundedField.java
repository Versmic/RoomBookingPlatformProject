package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * RoundedField
 * ------------
 * A rounded, dark input field containing a borderless text field
 * with placeholder support.
 *
 * It also supports the same scale-and-fade entrance animation used
 * by WelcomeScreen.RoundedButton.
 */
public class RoundedField extends JPanel {

    private float entranceProgress = 1f;
    private Timer entranceTimer;

    /**
     * Creates a rounded field containing the supplied text field.
     */
    public RoundedField(JTextField field) {
        setOpaque(false);
        setLayout(new BorderLayout());

        // Padding around the text inside the rounded field
        setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setForeground(Colours.TEXT_LIGHT);
        field.setCaretColor(Colours.TEXT_LIGHT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));

        add(field, BorderLayout.CENTER);
    }

    /**
     * Plays a scale-and-fade entrance animation.
     */
    public void playEntranceAnimation() {
        if (entranceTimer != null && entranceTimer.isRunning()) {
            entranceTimer.stop();
        }

        long startTime = System.currentTimeMillis();
        int durationMs = 250;

        entranceProgress = 0f;

        entranceTimer = new Timer(12, null);

        entranceTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;

            float t = Math.min(
                    1f,
                    elapsed / (float) durationMs
            );

            float f = t - 1f;

            // Ease-out cubic animation
            entranceProgress = f * f * f + 1f;

            repaint();

            if (t >= 1f) {
                entranceProgress = 1f;
                entranceTimer.stop();
            }
        });

        entranceTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        /*
         * Scale and fade the entire field during its entrance animation.
         */
        if (entranceProgress < 1f) {
            float scaleX = 0.85f + 0.15f * entranceProgress;
            int centerX = width / 2;

            g2.translate(centerX, 0);
            g2.scale(scaleX, 1.0);
            g2.translate(-centerX, 0);

            float alpha = Math.max(
                    0f,
                    Math.min(1f, entranceProgress)
            );

            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            alpha
                    )
            );
        }

        // Dark field background
        g2.setColor(new Color(0x1B, 0x22, 0x33));

        g2.fill(new RoundRectangle2D.Double(
                0,
                0,
                width,
                height,
                18,
                18
        ));

        // Faint border
        g2.setColor(new Color(255, 255, 255, 22));
        g2.setStroke(new BasicStroke(1f));

        g2.draw(new RoundRectangle2D.Double(
                0.5,
                0.5,
                width - 1,
                height - 1,
                18,
                18
        ));

        // Paint the text field using the same animation transformation
        super.paintComponent(g2);

        g2.dispose();
    }

    /**
     * JTextField that draws placeholder text whenever the field is empty.
     */
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

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(Colours.TEXT_MUTED);
                g2.setFont(getFont());

                FontMetrics fontMetrics = g2.getFontMetrics();

                int y = (
                        getHeight()
                        + fontMetrics.getAscent()
                        - fontMetrics.getDescent()
                ) / 2;

                g2.drawString(placeholder, 2, y);

                g2.dispose();
            }
        }
    }

    /**
     * JPasswordField that draws placeholder text whenever the field is empty.
     */
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

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(Colours.TEXT_MUTED);
                g2.setFont(getFont());

                FontMetrics fontMetrics = g2.getFontMetrics();

                int y = (
                        getHeight()
                        + fontMetrics.getAscent()
                        - fontMetrics.getDescent()
                ) / 2;

                g2.drawString(placeholder, 2, y);

                g2.dispose();
            }
        }
    }
}
