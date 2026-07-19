package roombooking.repository;

import java.util.ArrayList;
import java.util.List;

import roombooking.enums.RoomStatus;
import roombooking.model.Room;

public class RoomRepository {

    private static final String FILE_NAME = "src/roombooking/database/rooms.csv";

    private static final int ROOM_ID_COLUMN = 0;
    private static final int BUILDING_NAME_COLUMN = 1;
    private static final int ROOM_NUMBER_COLUMN = 2;
    private static final int CAPACITY_COLUMN = 3;
    private static final int STATUS_COLUMN = 4;
    private static final int REQUIRED_COLUMNS = 5;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    // saves a new room
    public void saveRoom(Room room) {
        if (room == null) {
            return;
        }

        if (findRoomById(room.getRoomId()) != null) {
            throw new IllegalArgumentException("room id already exists");
        }

        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(room));
        db.writeCSV(FILE_NAME, rows);
    }

    // updates a room using its room id
    public void updateRoom(Room room) {
        if (room == null) {
            return;
        }

        db.updateRow(FILE_NAME, ROOM_ID_COLUMN, room.getRoomId(), toRow(room));
    }

    // deletes a room using its room id
    public void deleteRoom(String roomId) {
        if (roomId == null || roomId.isBlank()) {
            return;
        }

        db.deleteRow(FILE_NAME, ROOM_ID_COLUMN, roomId.trim());
    }

    // finds a room using its room id
    public Room findRoomById(String roomId) {
        if (roomId == null || roomId.isBlank()) {
            return null;
        }

        String searchedId = roomId.trim();

        for (String[] row : db.readCSV(FILE_NAME)) {
            if (!isValidRoomRow(row)) {
                continue;
            }

            if (row[ROOM_ID_COLUMN].trim().equalsIgnoreCase(searchedId)) {
                return toRoom(row);
            }
        }

        return null;
    }

    // returns every valid room
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();

        for (String[] row : db.readCSV(FILE_NAME)) {
            if (isValidRoomRow(row)) {
                rooms.add(toRoom(row));
            }
        }

        return rooms;
    }

    // converts a room into a csv row
    private String[] toRow(Room room) {
        return new String[] {
                room.getRoomId(),
                room.getBuildingName(),
                String.valueOf(room.getRoomNumber()),
                String.valueOf(room.getCapacity()),
                room.getStatus().name()
        };
    }

    // converts a csv row into a room
    private Room toRoom(String[] row) {
        String roomId = row[ROOM_ID_COLUMN].trim();
        String buildingName = row[BUILDING_NAME_COLUMN].trim();
        int roomNumber = Integer.parseInt(row[ROOM_NUMBER_COLUMN].trim());
        int capacity = Integer.parseInt(row[CAPACITY_COLUMN].trim());
        RoomStatus status = RoomStatus.valueOf(row[STATUS_COLUMN].trim().toUpperCase());

        return new Room(roomId, buildingName, roomNumber, capacity, status);
    }

    // checks that the row contains valid room data
    private boolean isValidRoomRow(String[] row) {
        if (row == null || row.length < REQUIRED_COLUMNS) {
            return false;
        }

        if (row[ROOM_ID_COLUMN].trim().equalsIgnoreCase("roomId")) {
            return false;
        }

        try {
            Integer.parseInt(row[ROOM_NUMBER_COLUMN].trim());
            Integer.parseInt(row[CAPACITY_COLUMN].trim());
            RoomStatus.valueOf(row[STATUS_COLUMN].trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}