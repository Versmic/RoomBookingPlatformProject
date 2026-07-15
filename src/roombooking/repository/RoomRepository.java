package roombooking.repository;

import java.util.ArrayList;
import java.util.List;

import roombooking.model.Room;
import users.RoomStatus;

public class RoomRepository {

    private static final String FILE_NAME = "rooms.csv";
    private static final int ID_COLUMN = 0;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    public void saveRoom(Room room) {
        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(room));
        db.writeCSV(FILE_NAME, rows);
    }

    public void updateRoom(Room room) {
        db.updateRow(FILE_NAME, ID_COLUMN, String.valueOf(room.getRoomID()), toRow(room));
    }

    public void deleteRoom(int roomID) {
        db.deleteRow(FILE_NAME, ID_COLUMN, String.valueOf(roomID));
    }

    public Room findRoomByID(int roomID) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > ID_COLUMN && row[ID_COLUMN].equals(String.valueOf(roomID))) {
                return toRoom(row);
            }
        }
        return null;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            rooms.add(toRoom(row));
        }
        return rooms;
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room room : getAllRooms()) {
            if (room.getStatus() == RoomStatus.AVAILABLE) {
                available.add(room);
            }
        }
        return available;
    }

    private String[] toRow(Room room) {
        return new String[] {
                String.valueOf(room.getRoomID()),
                String.valueOf(room.getRoomNumber()),
                String.valueOf(room.getCapacity()),
                room.getBuilding(),
                room.getStatus().name()
        };
    }

    private Room toRoom(String[] row) {
        int roomID = Integer.parseInt(row[0]);
        int roomNumber = Integer.parseInt(row[1]);
        int capacity = Integer.parseInt(row[2]);
        String building = row[3];
        RoomStatus status = RoomStatus.valueOf(row[4]);
        return new Room(roomID, roomNumber, capacity, building, status);
    }
}
