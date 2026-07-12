package roombooking.main;

import javax.swing.SwingUtilities;

import roombooking.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
