package roombooking.view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;

import roombooking.model.Account;
import roombooking.model.ChiefEventCoordinator;

// displays the admin account generation form
public class AppGenerateAdminPanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private static final Color SUCCESS_COLOR = new Color(0x4A, 0xD9, 0x8A);
    private static final Color ERROR_COLOR = new Color(0xFF, 0x6B, 0x6B);

    private final ChiefEventCoordinator chiefCoordinator;
    private final Runnable onSuccess;

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel generatedIdLabel;
    private RoundedButton generateBtn;
    private JLabel statusLabel;
    private Timer statusTimer;

    public AppGenerateAdminPanel(ChiefEventCoordinator chiefCoordinator, Runnable onBack, Runnable onSuccess) {
        if (chiefCoordinator == null) {
            throw new IllegalArgumentException("Chief event coordinator is required");
        }

        this.chiefCoordinator = chiefCoordinator;
        this.onSuccess = onSuccess;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        add(buildHeader(onBack), BorderLayout.NORTH);
        add(buildFormCard(), BorderLayout.CENTER);
    }

    // builds the page header
    private JPanel buildHeader(Runnable onBack) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        RoundedButton backBtn = new RoundedButton(
                "\u2190 BACK",
                new Color(0x33, 0x3E, 0x55),
                new Color(0x33, 0x3E, 0x55),
                Colours.TEXT_MUTED,
                RoundedButton.ButtonStyle.SUBTLE_FILL
        );

        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        JLabel title = new JLabel("Generate Admin Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Create a new administrator account");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(backBtn);
        header.add(Box.createVerticalStrut(16));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        return header;
    }

    // builds the admin account form
    private JComponent buildFormCard() {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        card.setPreferredSize(new Dimension(420, 400));
        card.setMaximumSize(new Dimension(420, 400));

        usernameField = buildInputField();
        emailField = buildInputField();
        passwordField = buildPasswordField();

        card.add(buildFieldLabel("Username"));
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(16));

        card.add(buildFieldLabel("Email"));
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(16));

        card.add(buildFieldLabel("Password"));
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(16));

        JLabel idCaption = buildFieldLabel("Admin ID (auto-generated)");

        generatedIdLabel = new JLabel(chiefCoordinator.getNextAdminId());
        generatedIdLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        generatedIdLabel.setForeground(Colours.TEXT_LIGHT);
        generatedIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(idCaption);
        card.add(Box.createVerticalStrut(4));
        card.add(generatedIdLabel);
        card.add(Box.createVerticalStrut(20));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        card.add(statusLabel);
        card.add(Box.createVerticalStrut(8));

        generateBtn = new RoundedButton(
                "Generate Account",
                Colours.PURPLE,
                Colours.BLUE,
                Colours.TEXT_LIGHT,
                RoundedButton.ButtonStyle.GRADIENT_FILL
        );

        generateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        generateBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
        generateBtn.setEnabled(false);
        generateBtn.addActionListener(e -> submitNewAdmin());

        card.add(generateBtn);
        card.add(Box.createVerticalGlue());

        DocumentListener validityListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateButtonState();
            }
        };

        usernameField.getDocument().addDocumentListener(validityListener);
        emailField.getDocument().addDocumentListener(validityListener);
        passwordField.getDocument().addDocumentListener(validityListener);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);

        return wrapper;
    }

    // enables the button when every field contains text
    private void updateButtonState() {
        boolean allFilled = !usernameField.getText().isBlank() && !emailField.getText().isBlank() && passwordField.getPassword().length > 0;

        generateBtn.setEnabled(allFilled);
    }

    // sends the entered information to the chief coordinator
    private void submitNewAdmin() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        char[] passwordCharacters = passwordField.getPassword();
        String password = new String(passwordCharacters);

        try {
            Account account = chiefCoordinator.generateAdminAccount(username, email, password);

            generatedIdLabel.setText(
                    account.getRegisteredUser().getIDNumber()
            );

            showSuccess();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        } catch (Exception exception) {
            showError("Account could not be created. Please try again.");
            exception.printStackTrace();
        } finally {
            Arrays.fill(passwordCharacters, '\0');
        }
    }

    // displays the successful result
    private void showSuccess() {
        stopStatusTimer();

        statusLabel.setForeground(SUCCESS_COLOR);
        statusLabel.setText("Admin account created successfully!");
        setFormEnabled(false);

        statusTimer = new Timer(2500, e -> {
            if (onSuccess != null) {
                onSuccess.run();
            }
        });

        statusTimer.setRepeats(false);
        statusTimer.start();
    }

    // displays an error in the panel
    private void showError(String message) {
        stopStatusTimer();

        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setText(message);

        statusTimer = new Timer(3000, e -> statusLabel.setText(" "));
        statusTimer.setRepeats(false);
        statusTimer.start();
    }

    // stops the previous status timer
    private void stopStatusTimer() {
        if (statusTimer != null && statusTimer.isRunning()) {
            statusTimer.stop();
        }
    }

    // enables or disables the form
    private void setFormEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        passwordField.setEnabled(enabled);

        if (enabled) {
            updateButtonState();
        } else {
            generateBtn.setEnabled(false);
        }
    }

    private JTextField buildInputField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private JPasswordField buildPasswordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        return field;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(0x1B, 0x22, 0x33));
        field.setForeground(Colours.TEXT_LIGHT);
        field.setCaretColor(Colours.TEXT_LIGHT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    }

    private JLabel buildFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Colours.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}