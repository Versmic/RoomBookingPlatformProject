package roombooking.command;

import users.RoomStatus;
import roombooking.service.RoomService;
import roombooking.observer.NotificationService;

public class DisableRoomCommand implements Command {
    private int roomId;
    private RoomService roomService;
    private RoomStatus previousStatus;
    private boolean executed = false;
    
    public DisableRoomCommand(int roomId, RoomService roomService) {
        this.roomId = roomId;
        this.roomService = roomService;
    }
    
    @Override
    public boolean execute() {
        if (!roomService.roomExistsById(roomId)) {
            return false;
        }
        
        if (roomService.hasActiveBookings(roomId)) {
            return false;
        }
        
        previousStatus = roomService.getRoomStatus(roomId);
        executed = roomService.disableRoom(roomId);
        
        if (executed) {
            NotificationService.getInstance().sendNotification(
                "Room disabled: " + roomId
            );
        }
        return executed;
    }
    
    @Override
    public boolean undo() {
        if (previousStatus == RoomStatus.AVAILABLE || previousStatus == RoomStatus.OCCUPIED) {
            return roomService.setRoomStatus(roomId, previousStatus);
        }
        return roomService.enableRoom(roomId);
    }
    
    @Override
    public String getCommandType() {
        return "DISABLE_ROOM";
    }
    
    public boolean isExecuted() {
        return executed;
    }
}
