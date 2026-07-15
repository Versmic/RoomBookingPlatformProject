package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import roombooking.enums.AccountType;

/**
 * Account creation form displayed over the permanent BackgroundInit panel
 */
public class SignUpScreen extends JPanel implements AnimatedScreen {

    // Main frame reference used for screen navigation
    private final MainFrame mainFrame;

    // Input components
    private final RoundedField usernameField;
    private final RoundedField emailField;
    private final RoundedField idNumberField;
    private final RoundedField passwordField;

    // Screen buttons
    private final RoundedButton backBtn;
    private final RoundedButton createAccountBtn;

    public SignUpScreen(MainFrame mainFrame, AccountType acctype) {
        this.mainFrame = mainFrame;

        // Centers the account creation form
        setLayout(new GridBagLayout());

        // Keeps BackgroundInit visible
        setOpaque(false);

        // Holds all screen components vertically
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setPreferredSize(new Dimension(348, 520));
        inner.setMaximumSize(new Dimension(348, 520));

        // Back button
        backBtn = new RoundedButton(
                "\u2190 BACK",
                new Color(0x33, 0x3E, 0x55),
                new Color(0x33, 0x3E, 0x55),
                Colours.TEXT_MUTED,
                RoundedButton.ButtonStyle.SUBTLE_FILL
        );

        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(100, 34));
        backBtn.setPreferredSize(new Dimension(100, 34));

        backBtn.addActionListener(event ->
                mainFrame.showScreen(new AccountTypeScreen(mainFrame))
        );

        // title
        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // subtitle
        JLabel subtitle = new JLabel("Enter your account information");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        // user name input
        RoundedField.PlaceholderTextField usernameInput = new RoundedField.PlaceholderTextField("Username");
        usernameField = createRoundedField(usernameInput);

        // email input
        RoundedField.PlaceholderTextField emailInput = new RoundedField.PlaceholderTextField("Email");
        emailField = createRoundedField(emailInput);

        
        // ID number input
        RoundedField.PlaceholderTextField idNumberInput = new RoundedField.PlaceholderTextField(getIDField(acctype));
        idNumberField = createRoundedField(idNumberInput);

        // password input
        RoundedField.PlaceholderPasswordField passwordInput =
                new RoundedField.PlaceholderPasswordField("Password");

        passwordField = createRoundedField(passwordInput);

        // create account button
        createAccountBtn = new RoundedButton(
                "CREATE ACCOUNT",
                Colours.TURQUOISE,
                Colours.DARK_TEAL,
                Colours.TEXT_LIGHT,
                RoundedButton.ButtonStyle.GRADIENT_FILL
        );

        createAccountBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountBtn.setMaximumSize(new Dimension(348, 50));
        createAccountBtn.setPreferredSize(new Dimension(348, 50));

        // Temporary account creation action
        createAccountBtn.addActionListener(e -> {
            String username = usernameInput.getText().trim();
            String email = emailInput.getText().trim();
            String idNumber = idNumberInput.getText().trim();
            char[] password = passwordInput.getPassword();

            if (username.isEmpty() || email.isEmpty() || idNumber.isEmpty() || password.length == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please complete every field",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                Arrays.fill(password, '\0');
                return;
            }

            System.out.println("Create account clicked");
            System.out.println("Username: " + username);
            System.out.println("Email: " + email);
            System.out.println("ID Number: " + idNumber);
            System.out.println("Password length: " + password.length);

            // Clear the password from memory after it is used
            Arrays.fill(password, '\0');

            // Later connect this to an account controller
            // accountController.createAccount(name, email, idNumber, password)
        });

        // Add screen components
        inner.add(backBtn);
        inner.add(Box.createVerticalStrut(24));
        inner.add(title);
        inner.add(Box.createVerticalStrut(8));
        inner.add(subtitle);
        inner.add(Box.createVerticalStrut(32));
        inner.add(usernameField);
        inner.add(Box.createVerticalStrut(14));
        inner.add(emailField);
        inner.add(Box.createVerticalStrut(14));
        inner.add(idNumberField);
        inner.add(Box.createVerticalStrut(14));
        inner.add(passwordField);
        inner.add(Box.createVerticalStrut(28));
        inner.add(createAccountBtn);

        // Centers the inner panel without stretching it
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.CENTER;

        add(inner, constraints);
    }
    
    //gets the string of the id field display (student#, faculty#...)
    private String getIDField(AccountType acctype) {
    	String label;

        switch (acctype) {
            case STUDENT:
                label = "Student Number";
                break;
            case FACULTY:
                label = "Faculty Number";
                break;
            case STAFF:
                label = "Staff Number";
                break;
            case PARTNER:
                label = "Organization ID";
                break;
            default:
                throw new IllegalArgumentException("Unknown account type: " + acctype);
        }

        return label;

    }

    // Creates a consistently sized rounded input field
    private RoundedField createRoundedField(JTextField input) {
        RoundedField field = new RoundedField(input);

        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setMaximumSize(new Dimension(348, 52));
        field.setPreferredSize(new Dimension(348, 52));

        return field;
    }

    // Plays the staggered field and button entrance animation
    @Override
    public void playEntranceAnimation() {
        backBtn.playEntranceAnimation();

        JComponent[] sequence = {
                usernameField,
                emailField,
                idNumberField,
                passwordField,
                createAccountBtn
        };

        int staggerMs = 65;

        for (int index = 0; index < sequence.length; index++) {
            JComponent component = sequence[index];

            Timer delay = new Timer(staggerMs * (index + 1), event -> {
                if (component instanceof RoundedField roundedField) {
                    roundedField.playEntranceAnimation();
                } else if (component instanceof RoundedButton roundedButton) {
                    roundedButton.playEntranceAnimation();
                }
            });

            delay.setRepeats(false);
            delay.start();
        }
    }
}