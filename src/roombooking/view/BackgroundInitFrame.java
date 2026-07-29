package roombooking.view;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;

// permanent shared background for the initial screens
public class BackgroundInitFrame extends JPanel {

    private static final int TRANSITION_DURATION_MS = 260;
    private static final int TIMER_DELAY_MS = 12;
    
    // background itself never changes, instead each panel 
    // ie. login, signup etc is placed inside AlphaScaleWrapper 
    // which controls the screen opacity and size
    private AlphaScaleWrapper currentWrapper;
    private boolean transitioning;

    public BackgroundInitFrame() {
        setOpaque(true);
        setLayout(new BorderLayout());
    }

    // shows the first screen without a transition
    public void showInitialScreen(JPanel screen) {
        removeAll();

        currentWrapper = new AlphaScaleWrapper(screen);
        currentWrapper.setProgress(1f);
        add(currentWrapper, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    // fades out the current screen and fades in the next screen
    public void transitionTo(JPanel nextScreen) {
    	
        if (transitioning || nextScreen == null) {
            return;
        }

        if (currentWrapper == null) {
            showInitialScreen(nextScreen);
            return;
        }

        transitioning = true;

        animate(currentWrapper, 1f, 0f, false, () -> {
            remove(currentWrapper);

            currentWrapper = new AlphaScaleWrapper(nextScreen);
            //currentWrapper.setProgress(0f);
            add(currentWrapper, BorderLayout.CENTER);

            revalidate();
            repaint();

            animate(currentWrapper, 0f, 1f, true, () -> {
                transitioning = false;
                currentWrapper.setProgress(1f);
            });
        });
        
    }

    // updates the fade and scale animation
    private void animate(AlphaScaleWrapper wrapper, float start, float end, boolean entrance, Runnable onComplete) {
        long startTime = System.currentTimeMillis();
        wrapper.setProgress(start);

        Timer timer = new Timer(TIMER_DELAY_MS, null);

        timer.addActionListener(event -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float timeProgress = Math.min(1f, elapsed / (float) TRANSITION_DURATION_MS);
            float easedProgress = entrance ? easeOutCubic(timeProgress) : easeInCubic(timeProgress);
            float value = start + (end - start) * easedProgress;

            wrapper.setProgress(value);

            if (timeProgress >= 1f) {
                timer.stop();
                wrapper.setProgress(end);

                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        timer.start();
    }

    // starts quickly and slows near the end
    private float easeOutCubic(float progress) {
        float shiftedProgress = progress - 1f;
        return shiftedProgress * shiftedProgress * shiftedProgress + 1f;
    }

    // starts slowly and speeds up
    private float easeInCubic(float progress) {
        return progress * progress * progress;
    }

    // paints the shared background
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2.setColor(Colours.BG_NAVY);
        g2.fillRect(0, 0, width, height);

        g2.setColor(new Color(Colours.PURPLE.getRed(), Colours.PURPLE.getGreen(), Colours.PURPLE.getBlue(), 70));
        g2.fill(new Ellipse2D.Double(-width * 0.25, -height * 0.10, width * 0.90, width * 0.90));

        g2.setColor(new Color(Colours.BLUE.getRed(), Colours.BLUE.getGreen(), Colours.BLUE.getBlue(), 90));
        g2.fill(new Ellipse2D.Double(width * 0.45, -height * 0.05, width * 0.85, width * 0.85));

        GradientPaint lowerGlow = new GradientPaint(0, (float) (height * 0.75), Colours.CYAN, width, height, Colours.BLUE);
        g2.setPaint(lowerGlow);
        g2.fill(new Ellipse2D.Double(width * 0.10, height * 0.80, width * 0.95, width * 0.95));

        g2.dispose();
    }

    // wraps a screen so it can fade and scale
    private static class AlphaScaleWrapper extends JPanel {

        private float progress = 1f;

        AlphaScaleWrapper(JPanel screen) {
            setOpaque(false);
            setLayout(new BorderLayout());

            screen.setOpaque(false);
            add(screen, BorderLayout.CENTER);
        }

        // keeps progress between zero and one
        void setProgress(float progress) {
            this.progress = Math.max(0f, Math.min(1f, progress));
            repaint();
        }

        // applies opacity and horizontal scaling
        @Override
        public void paint(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int centerX = getWidth() / 2;
            float scaleX = 0.88f + 0.12f * progress;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, progress));

            g2.translate(centerX, 0);
            g2.scale(scaleX, 1.0);
            g2.translate(-centerX, 0);

            super.paint(g2);
            g2.dispose();
        }
    }
}
