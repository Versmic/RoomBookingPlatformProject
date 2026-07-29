package roombooking.view;

import javax.swing.*;

import java.awt.*;

/**
 * The application's single JFrame.
 * BackGroundInit remains mounted permanently and swaps only transparent
 * foreground screens above the shared background.
 */
public class MainFrame extends JFrame {

    private final BackgroundInitFrame backgroundInitFrame;

    public MainFrame() {
        setTitle("RoomBookingPlatform");
        setSize(420, 600);
        setMinimumSize(new Dimension(420, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // use backgroundinit as the main panel
        backgroundInitFrame = new BackgroundInitFrame(); 
        setContentPane(backgroundInitFrame);
        // display welcome screen infront of backgroundinit
        backgroundInitFrame.showInitialScreen(new WelcomePanel(this));
        setVisible(true);
    }

    // transitions between the initial screens
    public void showPanel(JPanel panel) {
    	backgroundInitFrame.transitionTo(panel);
    }
    
    // create the our main app shell frame
    public void startMainAppShellFrame(String username) {
    	dispose();
        new AppShellFrame(username);
	
    }
    
}