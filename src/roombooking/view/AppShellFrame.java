package roombooking.view;

import javax.swing.*;
import java.awt.*;

// the main app window shown after login
public class AppShellFrame extends JFrame {

    public AppShellFrame(String userDisplayName) {
        String displayName = (userDisplayName == null || userDisplayName.isBlank()) ? "User" : userDisplayName;

        setTitle("RoomBookingPlatform - Dashboard");
        setSize(1200, 760);
        setMinimumSize(new Dimension(980, 620));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        // sign out closes this window and drops the user back at the login flow
        Runnable onSignOut = () -> {
            dispose();
            new MainFrame();
        };

        root.add(new AppSideBarPanel(displayName, onSignOut), BorderLayout.WEST);
        root.add(new AppDashBoardPanel(), BorderLayout.CENTER);

        setVisible(true);
    }
}