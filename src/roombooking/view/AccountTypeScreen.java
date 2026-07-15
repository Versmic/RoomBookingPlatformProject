package roombooking.view;

import javax.swing.*;

import roombooking.enums.AccountType;

import java.awt.*;

/**
 * Panel class that displays after clicking sign up on WelcomeScreen.
 * Allows the user to choose which type of account they want to create.
 * This screen is displayed over the permanent BackgroundInit panel.
 */
public class AccountTypeScreen extends JPanel implements AnimatedScreen {

    // main frame reference used for screen navigation.
    private final MainFrame mainFrame;

    // buttons are fields because they are animated when the screen appears
    private final RoundedButton studentBtn;
    private final RoundedButton staffBtn;
    private final RoundedButton facultyBtn;
    private final RoundedButton partnerBtn;
    private final RoundedButton backBtn;

    public AccountTypeScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // centers the content panel inside the screen
        setLayout(new GridBagLayout());

        // keeps BackgroundInit visible behind this screen
        setOpaque(false);

        // holds all text and buttons vertically
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // title text
        JLabel title = new JLabel("Choose Account Type");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // subtitle text
        JLabel subtitle = new JLabel("Select the type of account you would like to create");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Colours.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // student account button
        studentBtn = new RoundedButton("STUDENT", Colours.PURPLE, Colours.PURPLE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.SUBTLE_FILL);

        // staff account button
        staffBtn = new RoundedButton("STAFF", Colours.BLUE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.SUBTLE_FILL);

        // faculty account button
        facultyBtn = new RoundedButton("FACULTY", Colours.TURQUOISE, Colours.TURQUOISE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.SUBTLE_FILL);

        // partner account button
        partnerBtn = new RoundedButton("PARTNER", Colours.DARK_TEAL, Colours.DARK_TEAL, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.SUBTLE_FILL);

        // back button returns to the welcome screen
        backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55),Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);

        // give the account buttons equal sizing and alignment.
        RoundedButton[] accountButtons = {studentBtn, staffBtn, facultyBtn, partnerBtn};

        for (RoundedButton button : accountButtons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(348, 48));
            button.setPreferredSize(new Dimension(348, 48));
        }

        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(348, 36));
        backBtn.setPreferredSize(new Dimension(348, 36));

        // Temporary actions until the signup screens are created.
        studentBtn.addActionListener(e -> {
            mainFrame.showScreen(new SignUpScreen(mainFrame, AccountType.STUDENT));
        });

        staffBtn.addActionListener(e -> {
            mainFrame.showScreen(new SignUpScreen(mainFrame, AccountType.STAFF));
        });
        
        facultyBtn.addActionListener(e -> {
            mainFrame.showScreen(new SignUpScreen(mainFrame, AccountType.FACULTY));
        });
     
        partnerBtn.addActionListener(e -> {
            mainFrame.showScreen(new SignUpScreen(mainFrame, AccountType.PARTNER));
        });

        
        backBtn.addActionListener(e -> mainFrame.showScreen(new WelcomeScreen(mainFrame)));

        // add screen content vertically
        inner.add(title);
        inner.add(Box.createVerticalStrut(8));
        inner.add(subtitle);

        inner.add(Box.createVerticalStrut(85));

        inner.add(studentBtn);
        inner.add(Box.createVerticalStrut(14));

        inner.add(staffBtn);
        inner.add(Box.createVerticalStrut(14));

        inner.add(facultyBtn);
        inner.add(Box.createVerticalStrut(14));

        inner.add(partnerBtn);
        inner.add(Box.createVerticalStrut(24));

        inner.add(backBtn);

        // Centers the inner panel while allowing a consistent form width.
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;

        add(inner, constraints);
    }

    /**
     * Animates each button into view one after another.
     * BackgroundInit handles the whole-screen fade and scale transition.
     */
    @Override
    public void playEntranceAnimation() {
        JComponent[] sequence = {
                studentBtn,
                staffBtn,
                facultyBtn,
                partnerBtn,
                backBtn
        };

        int staggerMs = 65;

        for (int index = 0; index < sequence.length; index++) {
            JComponent component = sequence[index];

            Timer delay = new Timer(
                    staggerMs * index,
                    event -> {
                        if (
                                component
                                instanceof RoundedButton button
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