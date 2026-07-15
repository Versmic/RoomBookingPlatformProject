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
        backgroundInitFrame.showInitialScreen(new WelcomeScreen(this)); // 
        setVisible(true);
    }

    // has transitions between the initial screens
    public void showScreen(JPanel screen) {
        backgroundInitFrame.transitionTo(screen);
    }
    
    public void startMainAppShellFrame() {
    	//create a appshellframe similar to background init
    	//this will be the main Frame and everything inside it will be panels
    	
    	
    	
    }
    
}