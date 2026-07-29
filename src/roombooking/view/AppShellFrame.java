package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import roombooking.enums.AccountType;
import roombooking.model.Account;
import roombooking.model.Booking;
import roombooking.model.Room;
import roombooking.repository.AccountRepository;
import roombooking.model.ChiefEventCoordinator;

import static roombooking.view.DashboardWidgets.*;

// the main app window shown after login
public class AppShellFrame extends JFrame {

    private final JPanel root;
    private JComponent currentContent;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private final AccountRepository accountRepo;
    private final Account loggedInAccount;

    private final String userName;

    public AppShellFrame(String userName) {
        this.userName = userName;

        accountRepo = new AccountRepository();
        loggedInAccount = accountRepo.findAccountByUserName(userName);

        // only CHEIF accounts get the "Generate Admin Account" nav item
        boolean isChief = loggedInAccount != null && loggedInAccount.getAccountType() == AccountType.CHEIF;

        // CHEIF and ADMIN accounts get the room management nav item
        boolean canManageRooms = loggedInAccount != null && (loggedInAccount.getAccountType() == AccountType.CHEIF || loggedInAccount.getAccountType() == AccountType.ADMIN);

        setTitle("RoomBookingPlatform - Dashboard");
        setSize(1200, 760);
        setMinimumSize(new Dimension(980, 620));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // shared app background
        root = new GlowBackgroundPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);

        // returns to the login screen
        Runnable onSignOut = () -> {
            dispose();
            new MainFrame();
        };

        root.add(new AppSideBarPanel(userName, loggedInAccount.getEmail(), isChief, canManageRooms, onSignOut, this::showDashboard, this::showBookingTimeSelection, this::showMyBookings, this::showGenerateAdmin, this::showRoomManagement), BorderLayout.WEST);

        showDashboard();
        setVisible(true);
    }

    // shows the dashboard
    private void showDashboard() {
        transitionTo(new AppDashBoardPanel());
    }

    // shows the users current bookings
    private void showMyBookings() {
        transitionTo(new AppBookingsBrowserPanel(loggedInAccount, this::showDashboard, this::showExtendBooking));
    }

    // shows the extend booking screen
    private void showExtendBooking(Booking booking) {
        transitionTo(new AppExtendBookingPanel(booking, this::showMyBookings, this::showMyBookings));
    }

    // shows the generate admin account screen to cheif account only
    private void showGenerateAdmin() {
        if (!(loggedInAccount.getRegisteredUser() instanceof ChiefEventCoordinator chiefCoordinator)) return;
        transitionTo(new AppGenerateAdminPanel(chiefCoordinator, this::showDashboard, this::showDashboard));
    }

    // shows room management to cheif and admin accounts only
    private void showRoomManagement() {
        AccountType accountType = loggedInAccount.getAccountType();
        if (accountType != AccountType.CHEIF && accountType != AccountType.ADMIN) return;
        transitionTo(new AppAdminRoomManagementPanel(this.loggedInAccount, this::showDashboard));
    }
    
    

    // starts the booking time selection - screen 1, no start time to validate against yet
    private void showBookingTimeSelection() {
        startDateTime = null;
        endDateTime = null;

        transitionTo(new AppSelectBookingTimePanel(false, null, this::onStartTimeChosen));
    }

    // stores the selected start date and time, then moves to screen 2
    private void onStartTimeChosen(LocalDate date, LocalTime time) {
        startDateTime = LocalDateTime.of(date, time);
        transitionTo(new AppSelectBookingTimePanel(true, startDateTime, this::onEndTimeChosen));
    }

    // stores the selected end date and time
    private void onEndTimeChosen(LocalDate endDate, LocalTime endTime) {
        endDateTime = LocalDateTime.of(endDate, endTime);
        showRoomBrowser();
    }

    // shows rooms for the selected time period
    private void showRoomBrowser() {
        transitionTo(new AppRoomBrowserPanel(startDateTime, endDateTime, this::showBookingTimeSelection, this::showPayDeposit));
    }

    // shows the deposit payment screen
    private void showPayDeposit(Room room) {
        transitionTo(new AppPayDepositPanel(loggedInAccount, room, startDateTime, endDateTime, this::showRoomBrowser, this::showDashboard, this::transitionTo));
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    // replaces the center panel
    private void transitionTo(JComponent newContent) {
        if (currentContent != null) root.remove(currentContent);

        currentContent = newContent;
        root.add(currentContent, BorderLayout.CENTER);

        root.revalidate();
        root.repaint();
    }
}