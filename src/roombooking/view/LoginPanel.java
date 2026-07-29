package roombooking.view;

import javax.swing.*;

import roombooking.repository.AccountRepository;
import roombooking.view.RoundedButton.ButtonStyle;
import roombooking.view.RoundedField.PlaceholderPasswordField;
import roombooking.view.RoundedField.PlaceholderTextField;

import java.awt.*;
import java.util.Arrays;

/**
 * Login form displayed over the permanent BackgroundInit panel.
 */
public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    
    private final AccountRepository accountRepo;

    private final RoundedButton backBtn;
    private final RoundedField userNameField;
    private final RoundedField passwordField;
    private final RoundedButton loginBtn;
    private final JLabel errorLabel;
    private final JPanel inner;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        this.accountRepo = new AccountRepository();
        // centers the inner login panel on the screen
        setLayout(new GridBagLayout());
        setOpaque(false);

        // holds all login components vertically and set size
        inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setPreferredSize(new Dimension(348, 500));
        inner.setMaximumSize(new Dimension(348, 500));

        // back button.
        backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));

        backBtn.addActionListener(e -> mainFrame.showPanel(new WelcomePanel(mainFrame)));

        // title text
        JLabel title = new JLabel("Login to your account");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // subtitle text
        JLabel subtitle = new JLabel("Enter your login information");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // user name field
        RoundedField.PlaceholderTextField userNameInput = new RoundedField.PlaceholderTextField("Username");

        userNameField = new RoundedField(userNameInput);
        userNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userNameField.setMaximumSize(new Dimension(348, 52));
        userNameField.setPreferredSize(new Dimension(348, 52));

        // password field.
        RoundedField.PlaceholderPasswordField passwordInput = new RoundedField.PlaceholderPasswordField("Password");
        passwordField = new RoundedField(passwordInput);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(348, 52));
        passwordField.setPreferredSize(new Dimension(348, 52));

        // error message - empty and reserved-space by default so the layout
        // does not jump around the moment an error appears
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(0xFF, 0x6B, 0x6B)); // dedicated error red, kept separate from Colours.CORAL
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setMaximumSize(new Dimension(348, 18));
        errorLabel.setPreferredSize(new Dimension(348, 18));

        // login button.
        loginBtn = new RoundedButton("LOGIN", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(348, 50));
        loginBtn.setPreferredSize(new Dimension(348, 50));

        loginBtn.addActionListener(e -> {
            String username = userNameInput.getText();
            char[] passwordChars = passwordInput.getPassword();
            String password = new String(passwordChars);

            if (accountRepo.login(username, password) == true) {
                Arrays.fill(passwordChars, '0'); // clear password out of memory once used
                mainFrame.startMainAppShellFrame(username);
            } 
            else {
                Arrays.fill(passwordChars, '0');
                showLoginError("Incorrect username or password");
                passwordInput.setText("");
                passwordInput.requestFocusInWindow();
            }
        });

        // add components vertically
        inner.add(backBtn);
        inner.add(Box.createVerticalStrut(30));
        inner.add(title);
        inner.add(Box.createVerticalStrut(40));
        inner.add(subtitle);
        inner.add(Box.createVerticalStrut(20));
        inner.add(userNameField);
        inner.add(Box.createVerticalStrut(16));
        inner.add(passwordField);
        inner.add(Box.createVerticalStrut(8));
        inner.add(errorLabel);
        inner.add(Box.createVerticalStrut(20));
        inner.add(loginBtn);

        // center the entire inner panel without stretching it
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        add(inner, constraints);
    }

    // shows an error message under the password field 
    private void showLoginError(String message) {
        errorLabel.setText(message);
    }
}