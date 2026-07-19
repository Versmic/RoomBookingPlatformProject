package roombooking.model;

import roombooking.enums.RoomStatus;
import roombooking.repository.RoomRepository;

public class Admin extends RegisteredUser{
	String adminID;
	
	public Admin(String adminID) {
		this.adminID = adminID;
	}
	
    public void enableRoom(Room room) {
		room.setStatus(RoomStatus.AVAILABLE);
		RoomRepository roomRepo = new RoomRepository();
		roomRepo.updateRoom(room);
	}
    
    public void disableRoom(Room room) {
    	room.setStatus(RoomStatus.DISABLED);
    	RoomRepository roomRepo = new RoomRepository();
		roomRepo.updateRoom(room);
    }
    
    public void maintainenceRoom(Room room) {
    	room.setStatus(RoomStatus.MAINTENANCE);
    	RoomRepository roomRepo = new RoomRepository();
		roomRepo.updateRoom(room);
    }
	
	@Override
	public double getHRate() {
		return 0;
	}

	@Override
	public String getIDNumber() {
		return this.adminID;
	}

}
