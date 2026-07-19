package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import roombooking.enums.AccountType;
import roombooking.view.RoundedButton.ButtonStyle;
import roombooking.view.RoundedField.PlaceholderPasswordField;
import roombooking.view.RoundedField.PlaceholderTextField;
import roombooking.factory.*;

/**
 * Account creation form displayed over the permanent BackgroundInit panel
 */
public class SignUpPanel extends JPanel {

    // main frame reference used for screen navigation
    private final MainFrame mainFrame;

    // input components
    private final RoundedField usernameField;
    private final RoundedField emailField;
    private final RoundedField idNumberField;
    private final RoundedField passwordField;
    
    // error text
    private final JLabel errorLabel;

    // screen buttons
    private final RoundedButton backBtn;
    private final RoundedButton createAccountBtn;
    
    private final AccountType accountType;

    public SignUpPanel(MainFrame mainFrame, AccountType accountType) {
        this.mainFrame = mainFrame;
        
        this.accountType = accountType;

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
                mainFrame.showPanel(new AccountTypePanel(mainFrame))
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
        RoundedField.PlaceholderTextField idNumberInput = new RoundedField.PlaceholderTextField(getIDField());
        idNumberField = createRoundedField(idNumberInput);

        // password input
        RoundedField.PlaceholderPasswordField passwordInput = new RoundedField.PlaceholderPasswordField("Password");
        passwordField = createRoundedField(passwordInput);

        // error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        errorLabel.setForeground(new Color(0xFF, 0x6B, 0x6B));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setMaximumSize(new Dimension(348, 18));
        
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
            
            AccountFactory accountFactory = getAccountFactory();
            if (username.isEmpty() || email.isEmpty() || idNumber.isEmpty() || password.length == 0) {
            	setErrorLabel("Please enter all the fields.");
                Arrays.fill(password, '\0');
                return;
            }
            
            else if(!accountFactory.CheckForStrongPassword(new String(password))){
            	setErrorLabel("Password: 8 characters, uppercase, lowercase, and symbol.");
            	return;
            }
            
            else if(!accountFactory.CheckForValidEmail(email)) {
            	setErrorLabel("Enter a valid email.");
            	return;
            }
            
            else if(accountFactory.CheckForUniqueEmail(email)) {
            	setErrorLabel("Email was taken.");
            	return;
            }
            
            else if(accountFactory.CheckForUniqueUsername(username)) {
            	setErrorLabel("Username was taken.");
            	return;
            }
            
            else if (accountFactory.checkForUniqueID(idNumber)) {
                setErrorLabel("ID number was already used.");
                return;
            }
            // create account and store in database
            accountFactory.createAccount(username, new String(password), email, idNumber);
            // clear password from memory
            Arrays.fill(password, '\0');
            // start mainapp
            this.mainFrame.startMainAppShellFrame(username);
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
        inner.add(Box.createVerticalStrut(12));
        inner.add(errorLabel);
        inner.add(Box.createVerticalStrut(10));
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
    private String getIDField() {
    	String label;

        switch (this.accountType) {
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
                throw new IllegalArgumentException("Unknown account type: " + this.accountType);
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
    
    private void setErrorLabel(String errorMsg) {
    	errorLabel.setText(errorMsg);
    	 Timer timer = new Timer(3000, e -> errorLabel.setText(""));
    	 timer.setRepeats(false);
    	 timer.start();
    }
    
    private AccountFactory getAccountFactory() {
    	AccountFactory accountFactory;
    	switch (this.accountType) {
        	case STUDENT:
        		accountFactory = new StudentAccountFactory();
        		break;
        	case FACULTY:
        		accountFactory = new FacultyAccountFactory();
        		break;
        	case STAFF:
        		accountFactory = new StaffAccountFactory();
        		break;
        	case PARTNER:
        		accountFactory = new PartnerAccountFactory();
        		break;
        	default:
        		throw new IllegalArgumentException("Unknown account type: " + this.accountType);	
    	}
    	
    	return accountFactory;
    }

}