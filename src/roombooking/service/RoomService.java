package roombooking.service;

import users.Room;
import users.RoomStatus;
import users.RoomRepository;

public class RoomService {
    private RoomRepository roomRepo;
    private int idCounter = 1;
    
    public RoomService() {
        this.roomRepo = new RoomRepository();
        for (Room room : roomRepo.getAllRooms()) {
            if (room.getRoomID() >= idCounter) {
                idCounter = room.getRoomID() + 1;
            }
        }
    }
    
    public boolean roomExists(int roomNumber) {
        for (Room room : roomRepo.getAllRooms()) {
            if (room.getRoomNumber() == roomNumber) {
                return true;
            }
        }
        return false;
    }
    
    public boolean roomExistsById(int roomId) {
        return roomRepo.findRoomByID(roomId) != null;
    }
    
    public int generateRoomId() {
        return idCounter++;
    }
    
    public boolean addRoom(Room room) {
        roomRepo.saveRoom(room);
        return true;
    }
    
    public boolean removeRoom(int roomId) {
        roomRepo.deleteRoom(roomId);
        return true;
    }
    
    public RoomStatus getRoomStatus(int roomId) {
        Room room = roomRepo.findRoomByID(roomId);
        if (room == null) return null;
        return room.getStatus();
    }
    
    public boolean enableRoom(int roomId) {
        return setRoomStatus(roomId, RoomStatus.AVAILABLE);
    }
    
    public boolean disableRoom(int roomId) {
        return setRoomStatus(roomId, RoomStatus.UNAVAILABLE);
    }
    
    public boolean setRoomStatus(int roomId, RoomStatus status) {
        Room room = roomRepo.findRoomByID(roomId);
        if (room == null) return false;
        room.setStatus(status);
        roomRepo.updateRoom(room);
        return true;
    }
    
    public boolean setMaintenance(int roomId, boolean maintenance) {
        Room room = roomRepo.findRoomByID(roomId);
        if (room == null) return false;
        if (maintenance) {
            room.setStatus(RoomStatus.UNAVAILABLE);
        } else {
            room.setStatus(RoomStatus.AVAILABLE);
        }
        roomRepo.updateRoom(room);
        return true;
    }
    
    public boolean hasActiveBookings(int roomId) {
        return false;
    }
}
