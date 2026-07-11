package roombooking.view;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame
 * ---------
 * The single JFrame for the whole app. Screens (WelcomeScreen, LoginScreen,
 * SignUpScreen, ...) are JPanels that get swapped in/out of this frame.
 *
 * Rather than plain CardLayout (which cuts instantly between panels),
 * this uses a simple absolute-positioned container + Swing Timer so we
 * can slide the incoming screen in while the outgoing one slides out.
 */
public class MainFrame extends JFrame {

    private final JPanel container; // holds exactly the panels involved in an animation
    private JPanel currentScreen;
    private boolean animating = false;

    public MainFrame() {
        setTitle("RoomBookingPlatform");
        setSize(420, 600);
        setMinimumSize(new Dimension(360, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        container = new JPanel(null); // absolute positioning for animation
        setContentPane(container);

        // First screen shown on launch
        WelcomeScreen welcomeScreen = new WelcomeScreen(this);
        showInitialScreen(welcomeScreen);

        setVisible(true);
    }

    /** Shows the very first screen with no transition, just a fade-in. */
    private void showInitialScreen(JPanel screen) {
        currentScreen = screen;
        screen.setBounds(0, 0, getWidth(), getHeight());
        container.add(screen);
        container.revalidate();
        container.repaint();
        
        if (screen instanceof WelcomeScreen) {
           ((WelcomeScreen) screen).playEntranceAnimation();
        	
        }
    }

    /**
     * Slides from the current screen to newScreen.
     * Call this from button handlers, e.g.:
     *   mainFrame.navigateTo(new LoginScreen(mainFrame));
     */
    public void navigateTo(JPanel newScreen) {
        if (animating || currentScreen == null) return;
        animating = true;

        int w = container.getWidth();
        int h = container.getHeight();

        newScreen.setBounds(w, 0, w, h); // start just off-screen to the right
        container.add(newScreen);
        container.setComponentZOrder(newScreen, 0);

        Timer timer = new Timer(10, null);
        final int[] dx = {0};
        final int totalSteps = 25;
        final int stepSize = Math.max(1, w / totalSteps);

        timer.addActionListener(e -> {
            dx[0] += stepSize;
            int outX = -Math.min(dx[0], w);
            int inX = Math.max(w - dx[0], 0);

            currentScreen.setBounds(outX, 0, w, h);
            newScreen.setBounds(inX, 0, w, h);

            if (dx[0] >= w) {
                timer.stop();
                container.remove(currentScreen);
                currentScreen = newScreen;
                animating = false;
                container.revalidate();
                container.repaint();
            }
        });
        timer.start();
    }
}

