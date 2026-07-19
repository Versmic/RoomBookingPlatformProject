package roombooking.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import roombooking.enums.AccountType;
import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Admin;
import roombooking.model.ChiefEventCoordinator;
import roombooking.model.Room;
import roombooking.repository.RoomRepository;

// allows admins and the chief event coordinator to manage rooms
public class AppAdminRoomManagementPanel extends JPanel {

    private static final Color CARD_BORDER = new Color(255, 255, 255, 18);
    private final RoomRepository roomRepository = new RoomRepository();
    private final Account account;

    public AppAdminRoomManagementPanel(Account account, Runnable onBack) {
    	this.account = account;
    	
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        add(buildHeader(onBack), BorderLayout.NORTH);
        add(buildRoomList(), BorderLayout.CENTER);
    }

    // builds the top section
    private JPanel buildHeader(Runnable onBack) {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        RoundedButton backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, RoundedButton.ButtonStyle.SUBTLE_FILL);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(90, 34));
        backBtn.setPreferredSize(new Dimension(90, 34));
        backBtn.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });

        JLabel title = new JLabel("Room Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Colours.TEXT_LIGHT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Enable, disable or close rooms for maintenance");
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

    // builds the scrollable room list
    private JComponent buildRoomList() {
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        List<Room> rooms = roomRepository.getAllRooms();

        if (rooms.isEmpty()) {
            JLabel empty = new JLabel("No rooms were found");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
            empty.setForeground(Colours.TEXT_MUTED);
            list.add(empty);
        } else {
            for (int i = 0; i < rooms.size(); i++) {
                list.add(buildRoomCard(rooms.get(i)));
                if (i < rooms.size() - 1) list.add(Box.createVerticalStrut(12));
            }
        }

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        return scrollPane;
    }

    // builds one room management card
    private JPanel buildRoomCard(Room room) {
        JPanel card = new JPanel(new BorderLayout(16, 0));
        card.setOpaque(true);
        card.setBackground(new Color(20, 22, 30));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CARD_BORDER, 1, true), BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JPanel textBox = new JPanel();
        textBox.setOpaque(false);
        textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(room.getBuildingName() + " - Room " + room.getRoomNumber());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        nameLabel.setForeground(Colours.TEXT_LIGHT);

        JLabel detailsLabel = new JLabel("Room ID: " + room.getRoomId() + "   |   Capacity: " + room.getCapacity() + "   |   Status: " + room.getStatus());
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detailsLabel.setForeground(Colours.TEXT_MUTED);

        textBox.add(nameLabel);
        textBox.add(Box.createVerticalStrut(5));
        textBox.add(detailsLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 3));
        buttonPanel.setOpaque(false);

        RoundedButton enableRoomBtn = buildButton("Enable Room", 125);
        RoundedButton disableRoomBtn = buildButton("Disable Room", 125);
        RoundedButton maintenanceBtn = buildButton("Close Room for Maintenance", 195);

       
 
    
        
        enableRoomBtn.addActionListener(e -> {
            if(this.account.getAccountType() == AccountType.ADMIN) {
            	Admin admin = (Admin) account.getRegisteredUser();
            	admin.enableRoom(room);
            }
            else {
            	ChiefEventCoordinator cheif = (ChiefEventCoordinator) account.getRegisteredUser();
            	cheif.enableRoom(room);
            }
            updateRoomStatus(room, RoomStatus.AVAILABLE, detailsLabel);
        });

        disableRoomBtn.addActionListener(e -> {
            if(this.account.getAccountType() == AccountType.ADMIN) {
            	Admin admin = (Admin) account.getRegisteredUser();
            	admin.disableRoom(room);
            }
            else {
            	ChiefEventCoordinator cheif = (ChiefEventCoordinator) account.getRegisteredUser();
            	cheif.disableRoom(room);
            }
            updateRoomStatus(room, RoomStatus.DISABLED, detailsLabel);
        });

        maintenanceBtn.addActionListener(e -> {
        	if(this.account.getAccountType() == AccountType.ADMIN) {
            	Admin admin = (Admin) account.getRegisteredUser();
            	admin.maintainenceRoom(room);
            }
            else {
            	ChiefEventCoordinator cheif = (ChiefEventCoordinator) account.getRegisteredUser();
            	cheif.maintainenceRoom(room);
            }
            updateRoomStatus(room, RoomStatus.MAINTENANCE, detailsLabel);
        });

        buttonPanel.add(enableRoomBtn);
        buttonPanel.add(disableRoomBtn);
        buttonPanel.add(maintenanceBtn);

        card.add(textBox, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private RoundedButton buildButton(String text, int width) {
        RoundedButton button = new RoundedButton(text, Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, RoundedButton.ButtonStyle.GRADIENT_FILL);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setPreferredSize(new Dimension(width, 36));
        return button;
    }
    
    private void updateRoomStatus(Room room, RoomStatus status, JLabel detailsLabel) {
        room.setStatus(status);
        roomRepository.updateRoom(room);
        detailsLabel.setText("Room ID: " + room.getRoomId() + "   |   Capacity: " + room.getCapacity() + "   |   Status: " + room.getStatus());
        revalidate();
        repaint();
    }
}