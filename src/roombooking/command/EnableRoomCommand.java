package roombooking.command;

import users.RoomStatus;
import roombooking.service.RoomService;
import roombooking.observer.NotificationService;

public class EnableRoomCommand implements Command {
    private int roomId;
    private RoomService roomService;
    private RoomStatus previousStatus;
    private boolean executed = false;
    
    public EnableRoomCommand(int roomId, RoomService roomService) {
        this.roomId = roomId;
        this.roomService = roomService;
    }
    
    @Override
    public boolean execute() {
        if (!roomService.roomExistsById(roomId)) {
            return false;
        }
        
        previousStatus = roomService.getRoomStatus(roomId);
        
        executed = roomService.enableRoom(roomId);
        if (executed) {
            NotificationService.getInstance().sendNotification(
                "Room enabled: " + roomId
            );
        }
        return executed;
    }
    
    @Override
    public boolean undo() {
        if (previousStatus == RoomStatus.AVAILABLE || previousStatus == RoomStatus.OCCUPIED) {
            return roomService.setRoomStatus(roomId, previousStatus);
        }
        return roomService.disableRoom(roomId);
    }
    
    @Override
    public String getCommandType() {
        return "ENABLE_ROOM";
    }
    
    public boolean isExecuted() {
        return executed;
    }
}
