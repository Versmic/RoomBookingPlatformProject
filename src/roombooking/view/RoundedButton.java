package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Reusable rounded button with hover and transition animations
 */
public class RoundedButton extends JButton {

    public enum ButtonStyle {
        GRADIENT_FILL,
        OUTLINE,
        SUBTLE_FILL
    }

    private final Color colorA;
    private final Color colorB;
    private final ButtonStyle style;

    private float hoverProgress = 0f;
    private float entranceProgress = 1f;

    private Timer hoverTimer;
    private Timer entranceTimer;

    public RoundedButton(String text, Color colorA, Color colorB, Color textColor, ButtonStyle style) {
        super(text);

        this.colorA = colorA;
        this.colorB = colorB;
        this.style = style;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(textColor);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                animateHoverTo(1f);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                animateHoverTo(0f);
            }
        });
    }

    // Smoothly changes the button hover effect
    private void animateHoverTo(float target) {
        if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();

        hoverTimer = new Timer(10, null);

        hoverTimer.addActionListener(event -> {
            hoverProgress += (target - hoverProgress) * 0.25f;

            if (Math.abs(target - hoverProgress) < 0.01f) {
                hoverProgress = target;
                hoverTimer.stop();
            }

            repaint();
        });

        hoverTimer.start();
    }

    // Plays the entrance animation
    public void playEntranceAnimation() {
        playEntranceAnimation(null);
    }

    // Plays the entrance animation and runs code when finished
    public void playEntranceAnimation(Runnable onComplete) {
        runProgressAnimation(true, onComplete);
    }

    // Plays the exit animation and runs code when finished
    public void playExitAnimation(Runnable onComplete) {
        runProgressAnimation(false, onComplete);
    }

    // Runs either the entrance or exit animation
    private void runProgressAnimation(boolean isEntrance, Runnable onComplete) {
        if (entranceTimer != null && entranceTimer.isRunning()) entranceTimer.stop();

        long startTime = System.currentTimeMillis();
        int durationMs = 250;

        float startValue = isEntrance ? 0f : 1f;
        float endValue = isEntrance ? 1f : 0f;

        entranceProgress = startValue;
        entranceTimer = new Timer(12, null);

        entranceTimer.addActionListener(event -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float timeProgress = Math.min(1f, elapsed / (float) durationMs);
            float easedProgress = isEntrance ? easeOutCubic(timeProgress) : easeInCubic(timeProgress);

            entranceProgress = startValue + (endValue - startValue) * easedProgress;
            repaint();

            if (timeProgress >= 1f) {
                entranceProgress = endValue;
                entranceTimer.stop();

                if (onComplete != null) onComplete.run();
            }
        });

        entranceTimer.start();
    }

    // Starts quickly and slows near the end
    private float easeOutCubic(float timeProgress) {
        float adjusted = timeProgress - 1f;
        return adjusted * adjusted * adjusted + 1f;
    }

    // Starts slowly and speeds up
    private float easeInCubic(float timeProgress) {
        return timeProgress * timeProgress * timeProgress;
    }

    // Blends two colours together
    private Color blend(Color first, Color second, float ratio) {
        int red = (int) (first.getRed() + (second.getRed() - first.getRed()) * ratio);
        int green = (int) (first.getGreen() + (second.getGreen() - first.getGreen()) * ratio);
        int blue = (int) (first.getBlue() + (second.getBlue() - first.getBlue()) * ratio);

        return new Color(red, green, blue);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = height;

        // Scales and fades the button during entrance and exit
        if (entranceProgress < 1f) {
            float scaleX = 0.85f + 0.15f * entranceProgress;
            int centerX = width / 2;

            graphics2D.translate(centerX, 0);
            graphics2D.scale(scaleX, 1.0);
            graphics2D.translate(-centerX, 0);

            float alpha = Math.max(0f, Math.min(1f, entranceProgress));
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }

        switch (style) {
            case GRADIENT_FILL -> paintGradient(graphics2D, width, height, arc);
            case OUTLINE -> paintOutline(graphics2D, width, height, arc);
            case SUBTLE_FILL -> paintSubtleFill(graphics2D, width, height, arc);
        }

        // Paints the button text with the same transform and transparency
        super.paintComponent(graphics2D);
        graphics2D.dispose();
    }

    // Paints the gradient button style
    private void paintGradient(Graphics2D graphics2D, int width, int height, int arc) {
        Color startColor = blend(colorA, Color.WHITE, hoverProgress * 0.2f);
        Color endColor = blend(colorB, Color.WHITE, hoverProgress * 0.2f);

        GradientPaint gradient = new GradientPaint(0, 0, startColor, width, 0, endColor);
        graphics2D.setPaint(gradient);
        graphics2D.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
    }

    // Paints the outline button style
    private void paintOutline(Graphics2D graphics2D, int width, int height, int arc) {
        int alpha = (int) (hoverProgress * 40);

        graphics2D.setColor(new Color(colorA.getRed(), colorA.getGreen(), colorA.getBlue(), alpha));
        graphics2D.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));

        graphics2D.setColor(colorB);
        graphics2D.setStroke(new BasicStroke(2f));
        graphics2D.draw(new RoundRectangle2D.Double(1, 1, width - 2, height - 2, arc, arc));
    }

    // Paints the subtle fill button style
    private void paintSubtleFill(Graphics2D graphics2D, int width, int height, int arc) {
        Color fillColor = blend(colorA, Color.WHITE, hoverProgress * 0.15f);

        graphics2D.setColor(fillColor);
        graphics2D.fill(new RoundRectangle2D.Double(0, 0, width, height, arc, arc));
    }
}