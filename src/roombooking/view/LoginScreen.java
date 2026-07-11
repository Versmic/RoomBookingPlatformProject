package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * LoginScreen
 * -----------
 * Reached by tapping "Log In" on WelcomeScreen. Same navy + glow background
 * style as WelcomeScreen, styled to match the reference image: centered
 * two-line title, left-aligned subtitle, rounded dark email/password fields
 * with small icons, and a solid gradient LOGIN button.
 *
 * Per the brief, this intentionally skips: remember me, forgot password,
 * password visibility toggle, other sign-in options, and the "don't have
 * an account" link - just a Back button that returns to WelcomeScreen.
 *
 * This reuses WelcomeScreen.RoundedButton (rather than duplicating that
 * ~200-line class) - see the small ButtonStyle visibility change made in
 * WelcomeScreen.java for why that's possible.
 */
public class LoginScreen extends JPanel {

    private final MainFrame mainFrame;

    // Tracks how "faded in" the background currently is - same pattern as WelcomeScreen.
    private float contentAlpha = 0f;

    private final WelcomeScreen.RoundedButton backBtn;
    private final RoundedField emailField;
    private final RoundedField passwordField;
    private final WelcomeScreen.RoundedButton loginBtn;

    public LoginScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setOpaque(true);

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // ---- back button: returns to WelcomeScreen ----
        backBtn = new WelcomeScreen.RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, WelcomeScreen.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> mainFrame.navigateTo(new WelcomeScreen(mainFrame)));

        // ---- title: two centered lines, matching the reference image ----
        JLabel title = new JLabel("<html><div style='text-align:center;'>LOGIN TO<br>YOUR ACCOUNT</div></html>");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // ---- subtitle ----
        JLabel subtitle = new JLabel("Enter your login information");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ---- email field ----
        RoundedField.PlaceholderTextField emailInput = new RoundedField.PlaceholderTextField("Email");
        emailField = new RoundedField(emailInput, RoundedField.IconType.EMAIL);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        emailField.setPreferredSize(new Dimension(10, 52));

        // ---- password field (no visibility toggle, per the brief) ----
        RoundedField.PlaceholderPasswordField passwordInput = new RoundedField.PlaceholderPasswordField("Password");
        passwordField = new RoundedField(passwordInput, RoundedField.IconType.PASSWORD);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        passwordField.setPreferredSize(new Dimension(10, 52));

        // ---- login button: solid purple->blue gradient, matches the image's indigo-blue button ----
        loginBtn = new WelcomeScreen.RoundedButton("LOGIN", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, WelcomeScreen.ButtonStyle.GRADIENT_FILL);
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // TODO: replace with real authentication once the backend/user model exists
        loginBtn.addActionListener(e -> {
            String email = emailInput.getText();
            char[] password = passwordInput.getPassword();
            System.out.println("Login clicked - email: " + email + ", password length: " + password.length);
        });

        inner.add(backBtn);
        inner.add(Box.createVerticalStrut(30));
        inner.add(title);
        inner.add(Box.createVerticalStrut(40));
        inner.add(subtitle);
        inner.add(Box.createVerticalStrut(20));
        inner.add(emailField);
        inner.add(Box.createVerticalStrut(16));
        inner.add(passwordField);
        inner.add(Box.createVerticalStrut(30));
        inner.add(loginBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(50, 36, 50, 36);
        add(inner, gbc);
    }

    /**
     * addNotify() is called by Swing the moment this panel actually becomes
     * part of a visible window (i.e. right when MainFrame.navigateTo adds it
     * to the container). Kicking the entrance animation off here means
     * MainFrame doesn't need any LoginScreen-specific code the way it
     * currently special-cases WelcomeScreen in showInitialScreen().
     */
    @Override
    public void addNotify() {
        super.addNotify();
        playEntranceAnimation();
    }

    private void playEntranceAnimation() {
        // ---- 1) fade the background glow in ----
        contentAlpha = 0f;
        Timer bgTimer = new Timer(15, null);
        bgTimer.addActionListener(e -> {
            contentAlpha += 0.07f;
            if (contentAlpha >= 1f) {
                contentAlpha = 1f;
                bgTimer.stop();
            }
            repaint();
        });
        bgTimer.start();

        // ---- 2) pop the back button in immediately, then cascade the form in ----
        backBtn.playEntranceAnimation();

        JComponent[] sequence = {emailField, passwordField, loginBtn};
        int staggerMs = 70;
        for (int i = 0; i < sequence.length; i++) {
            JComponent c = sequence[i];
            Timer delay = new Timer(staggerMs * (i + 1), e -> {
                if (c instanceof RoundedField rf) rf.playEntranceAnimation();
                else if (c instanceof WelcomeScreen.RoundedButton rb) rb.playEntranceAnimation();
            });
            delay.setRepeats(false);
            delay.start();
        }
    }

    /** Same navy + glow background painting as WelcomeScreen, for visual consistency. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, contentAlpha));

        int w = getWidth();
        int h = getHeight();

        g2.setColor(Colours.BG_NAVY);
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(Colours.PURPLE.getRed(), Colours.PURPLE.getGreen(), Colours.PURPLE.getBlue(), 70));
        g2.fill(new Ellipse2D.Double(-w * 0.25, -h * 0.10, w * 0.9, w * 0.9));

        g2.setColor(new Color(Colours.BLUE.getRed(), Colours.BLUE.getGreen(), Colours.BLUE.getBlue(), 90));
        g2.fill(new Ellipse2D.Double(w * 0.45, -h * 0.05, w * 0.85, w * 0.85));

        GradientPaint gradient = new GradientPaint(
                0, (float) (h * 0.75), Colours.CYAN,
                w, h, Colours.BLUE
        );
        g2.setPaint(gradient);
        g2.fill(new Ellipse2D.Double(w * 0.10, h * 0.80, w * 0.95, w * 0.95));

        g2.dispose();
    }
}