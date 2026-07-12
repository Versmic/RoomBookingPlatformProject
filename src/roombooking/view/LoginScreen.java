package roombooking.view;

import javax.swing.*;
import java.awt.*;

/**
 * Login form displayed over the permanent BackgroundInit panel.
 */
public class LoginScreen extends JPanel implements AnimatedScreen {

    private final MainFrame mainFrame;

    private final WelcomeScreen.RoundedButton backBtn;
    private final RoundedField emailField;
    private final RoundedField passwordField;
    private final WelcomeScreen.RoundedButton loginBtn;

    public LoginScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // Centers the inner login panel on the screen.
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Holds all login components vertically.
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // Give the form a consistent width instead of stretching it
        // across the entire window.
        inner.setPreferredSize(new Dimension(348, 470));
        inner.setMaximumSize(new Dimension(348, 470));

        // Back button.
        backBtn = new WelcomeScreen.RoundedButton(
                "\u2190 BACK",
                new Color(0x33, 0x3E, 0x55),
                new Color(0x33, 0x3E, 0x55),
                Colours.TEXT_MUTED,
                WelcomeScreen.ButtonStyle.SUBTLE_FILL
        );

        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));

        backBtn.addActionListener(
                event -> mainFrame.showScreen(new WelcomeScreen(mainFrame))
        );

        // Login title.
        JLabel title = new JLabel("Login to your account");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtitle.
        JLabel subtitle = new JLabel("Enter your login information");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Email field.
        RoundedField.PlaceholderTextField emailInput =
                new RoundedField.PlaceholderTextField("Email");

        emailField = new RoundedField(emailInput);
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(348, 52));
        emailField.setPreferredSize(new Dimension(348, 52));

        // Password field.
        RoundedField.PlaceholderPasswordField passwordInput =
                new RoundedField.PlaceholderPasswordField("Password");

        passwordField = new RoundedField(passwordInput);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(348, 52));
        passwordField.setPreferredSize(new Dimension(348, 52));

        // Login button.
        loginBtn = new WelcomeScreen.RoundedButton(
                "LOGIN",
                Colours.PURPLE,
                Colours.BLUE,
                Colours.TEXT_LIGHT,
                WelcomeScreen.ButtonStyle.GRADIENT_FILL
        );

        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(348, 50));
        loginBtn.setPreferredSize(new Dimension(348, 50));

        loginBtn.addActionListener(event -> {
            String email = emailInput.getText();
            char[] password = passwordInput.getPassword();

            System.out.println(
                    "Login clicked - email: " + email
                            + ", password length: " + password.length
            );
        });

        // Add components vertically.
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

        // Center the entire inner panel without stretching it.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;

        add(inner, constraints);
    }

    @Override
    public void playEntranceAnimation() {
        backBtn.playEntranceAnimation();

        JComponent[] sequence = {
                emailField,
                passwordField,
                loginBtn
        };

        int staggerMs = 70;

        for (int index = 0; index < sequence.length; index++) {
            JComponent component = sequence[index];

            Timer delay = new Timer(
                    staggerMs * (index + 1),
                    event -> {
                        if (component instanceof RoundedField roundedField) {
                            roundedField.playEntranceAnimation();
                        } else if (
                                component instanceof WelcomeScreen.RoundedButton button
                        ) {
                            button.playEntranceAnimation();
                        }
                    }
            );

            delay.setRepeats(false);
            delay.start();
        }
    }
}