package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * First screen shown to the user.
 *
 * Provides navigation to:
 * - LoginScreen
 * - AccountTypeScreen
 * - Guest flow
 */
public class WelcomeScreen extends JPanel implements AnimatedScreen {

    // main frame reference used for screen navigation
    private final MainFrame mainFrame;

    // buttons are fields so they can be animated when the screen appears
    private final RoundedButton loginBtn;
    private final RoundedButton signUpBtn;
    private final RoundedButton guestBtn;

    public WelcomeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // centers and stretches the inner content panel
        setLayout(new GridBagLayout());

        // backgroundInit paints the shared background
        setOpaque(false);

        // inner pannel that holds the title, subtitle, and buttons vertically
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // welcome title
        JLabel title = new JLabel("Welcome to RoomBooking");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        // welcome subtitle
        JLabel subtitle = new JLabel("Start with sign up or sign in");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // login button
        loginBtn = new RoundedButton("LOG IN", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);

        // sign up button
        signUpBtn = new RoundedButton("SIGN UP", Colours.TURQUOISE, Colours.DARK_TEAL, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);

        // guest button
        guestBtn = new RoundedButton("CONTINUE AS GUEST", Color.decode("#0D8FA5"), Color.decode("#4A74D9"), Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);

        // Give all buttons equal sizing and alignment
        RoundedButton[] buttons = {loginBtn, signUpBtn, guestBtn};
        for (RoundedButton button : buttons) {
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 48)
            );
        }

        // login button action listener to navigate to the login screen
        loginBtn.addActionListener(e -> mainFrame.showScreen(new LoginScreen(mainFrame)));

        // sign up button action listener to navigate to the login screen
        signUpBtn.addActionListener(e -> mainFrame.showScreen(new AccountTypeScreen(mainFrame)));

        // temporary guest action
        guestBtn.addActionListener(event -> {
            System.out.println(
                    "Continue as Guest clicked - wire up guest flow here"
            );

            // Later:
            // mainFrame.showScreen(new GuestHomeScreen(mainFrame));
        });

        // add title and subtitl.
        inner.add(title);
        inner.add(Box.createVerticalStrut(8));
        inner.add(subtitle);

        // large space between the heading and buttons
        inner.add(Box.createVerticalStrut(190));

        // add buttons with spacing
        inner.add(loginBtn);
        inner.add(Box.createVerticalStrut(14));
        inner.add(signUpBtn);
        inner.add(Box.createVerticalStrut(14));
        inner.add(guestBtn);

        // make the inner panel fill the screen with padding
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(60, 36, 50, 36);

        add(inner, constraints);
    }

    /**
     * Plays the staggered entrance animation for the welcome buttons.
     */
    @Override
    public void playEntranceAnimation() {
        RoundedButton[] buttons = {loginBtn, signUpBtn, guestBtn};
        int staggerMs = 70;
        for (int index = 0; index < buttons.length; index++) {
            RoundedButton button = buttons[index];
            Timer delay = new Timer(staggerMs * index, e -> button.playEntranceAnimation());
            delay.setRepeats(false);
            delay.start();
        }
    }

}