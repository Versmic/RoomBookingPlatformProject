package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import roombooking.enums.RoomStatus;
import roombooking.model.*;
import roombooking.repository.RoomRepository;


public class AdminTester {

	/*
	 * this test ensures that the Admin constructor
	 * stores the correct admin ID
	 */
	@Test
	public void testAdminId() {
		Admin admin = new Admin("testAdmin");
		assertEquals("testAdmin", admin.getIDNumber());
	}
	
	
	
	/*
	 * this test ensures that an Admin
	 * has an hourly rate of zero
	 */
	@Test
	public void testAdminHourlyRate() {
		Admin admin = new Admin("testAdmin");
		assertEquals(0.00, admin.getHRate(), 0.001);
	}
	

	/*
	 * this test ensures that enableRoom()
	 * changes the Room object status to AVAILABLE
	 */
	@Test
	public void testEnableRoomChangesRoomStatus() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.DISABLED);

		try {
			// store room into database
			roomRepo.saveRoom(room);
			// call enableRoom()
			admin.enableRoom(room);

			assertEquals(RoomStatus.AVAILABLE, room.getStatus());

		} 
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	/*
	 * this test ensures that enableRoom()
	 * updates the room in the database
	 */
	@Test
	public void testEnableRoomUpdatesDatabase() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.DISABLED);

		try {
			// store room into database
			roomRepo.saveRoom(room);
			// call enableRoom()
			admin.enableRoom(room);

			List<Room> rooms = roomRepo.getAllRooms();
			
			assertEquals(RoomStatus.AVAILABLE, rooms.get(rooms.size()-1).getStatus());

		} 
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	
	/*
	 * this test ensures that disableRoom()
	 * changes the Room object status to DISABLED
	 */
	@Test
	public void testDisableRoomChangesRoomStatus() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.DISABLED);

		try {
			// store room into database
			roomRepo.saveRoom(room);
			// call disableRoom()
			admin.disableRoom(room);

			assertEquals(RoomStatus.DISABLED, room.getStatus());

		} 
		
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	
	/*
	 * this test ensures that disableRoom()
	 * updates the room in the database
	 */
	@Test
	public void testDisableRoomUpdatesDatabase() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.AVAILABLE);

		try {
			// store room into database
			roomRepo.saveRoom(room);
			// call enableRoom()
			admin.disableRoom(room);

			List<Room> rooms = roomRepo.getAllRooms();
			
			assertEquals(RoomStatus.DISABLED, rooms.get(rooms.size()-1).getStatus());

		} 
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	
	
	
	/*
	 * this test ensures that maintainenceRoom()
	 * changes the Room object status to MAINTENANCE
	 */
	@Test
	public void testMaintenanceRoomChangesRoomStatus() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.AVAILABLE);

		try {
			// store room into database
			roomRepo.saveRoom(room);
			// call disableRoom()
			admin.maintenanceRoom(room);

			assertEquals(RoomStatus.MAINTENANCE, room.getStatus());

		} 
		
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	/*
	 * this test ensures that maintainenceRoom()
	 * updates the room in the database
	 */
	@Test
	public void testMaintenanceRoomUpdatesDatabase() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.AVAILABLE);

		try {
			// store room into database
			roomRepo.saveRoom(room);
			// call enableRoom()
			admin.maintenanceRoom(room);

			List<Room> rooms = roomRepo.getAllRooms();
			
			assertEquals(RoomStatus.MAINTENANCE, rooms.get(rooms.size()-1).getStatus());

		} 
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	/*
	 * this test ensures that a room under maintenance
	 * can be enabled again
	 */
	@Test
	public void testEnableRoomFromMaintenance() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.MAINTENANCE);

		try {
			roomRepo.saveRoom(room);

			admin.enableRoom(room);
			
			// test room object
			assertEquals(RoomStatus.AVAILABLE, room.getStatus());
			// test room object from database
			List<Room> rooms = roomRepo.getAllRooms();
			assertEquals(RoomStatus.AVAILABLE, rooms.get(rooms.size() - 1).getStatus());

		} 
		
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	/*
	 * this test ensures that a disabled room
	 * can be changed to maintenance status
	 */
	@Test
	public void testMaintenanceRoomFromDisabled() {
		Admin admin = new Admin("testAdmin");
		RoomRepository roomRepo = new RoomRepository();

		Room room = new Room("adminTestRoom", "test", 0, 0, RoomStatus.DISABLED);

		try {
			roomRepo.saveRoom(room);

			admin.maintenanceRoom(room);
			
			// test room object
			assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
			// test room object from database
			List<Room> rooms = roomRepo.getAllRooms();
			assertEquals(RoomStatus.MAINTENANCE, rooms.get(rooms.size() - 1).getStatus());

		} 
		
		finally {
			roomRepo.deleteRoom("adminTestRoom");
		}
	}
	
	
	
	
}
