package roombooking.view;

import javax.swing.*;
import java.awt.*;

/**
 * The application's single JFrame.
 * BackGroundInit remains mounted permanently and swaps only transparent
 * foreground screens above the shared background.
 */
public class MainFrame extends JFrame {

    private final BackgroundInit backgroundInit;

    public MainFrame() {
        setTitle("RoomBookingPlatform");
        setSize(420, 600);
        setMinimumSize(new Dimension(420, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // use backgroundinit as the main panel
        backgroundInit = new BackgroundInit(); 
        setContentPane(backgroundInit);
        // display welcome screen infront of backgroundinit
        backgroundInit.showInitialScreen(new WelcomeScreen(this)); // 
        setVisible(true);
    }

    /** Uses a fade-and-scale content transition; the background stays fixed. */
    public void showScreen(JPanel screen) {
        backgroundInit.transitionTo(screen);
    }
    
    
}