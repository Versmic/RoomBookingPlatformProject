package roombooking.command;

import users.Room;
import users.RoomStatus;
import roombooking.service.RoomService;
import roombooking.observer.NotificationService;

public class AddRoomCommand implements Command {
    private int roomNumber;
    private int capacity;
    private String building;
    private RoomService roomService;
    private Room createdRoom;
    private boolean executed = false;
    
    public AddRoomCommand(int roomNumber, int capacity, String building, RoomService roomService) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.building = building;
        this.roomService = roomService;
    }
    
    @Override
    public boolean execute() {
        if (roomService.roomExists(roomNumber)) {
            return false;
        }
        
        if (capacity <= 0) {
            return false;
        }
        
        if (building == null || building.trim().isEmpty()) {
            return false;
        }
        
        int roomId = roomService.generateRoomId();
        createdRoom = new Room(roomId, roomNumber, capacity, building, RoomStatus.AVAILABLE);
        executed = roomService.addRoom(createdRoom);
        
        if (executed) {
            NotificationService.getInstance().sendNotification(
                "Room added: " + roomNumber
            );
        }
        return executed;
    }
    
    @Override
    public boolean undo() {
        if (createdRoom == null) {
            return false;
        }
        return roomService.removeRoom(createdRoom.getRoomID());
    }
    
    @Override
    public String getCommandType() {
        return "ADD_ROOM";
    }
    
    public boolean isExecuted() {
        return executed;
    }
    
    public Room getCreatedRoom() {
        return createdRoom;
    }
}
