package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Room;
import roombooking.repository.AccountRepository;
import roombooking.repository.RoomRepository;

public class RoomRepositoryTester {
	
	/*
	 * this test ensures that saveRoom() saves a room to our database
	 * and saves the correct information
	 */
	@Test
	public void saveRoom() {
		
		RoomRepository roomRepo = new RoomRepository();
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		int initialRoomsAmount = roomRepo.getAllRooms().size();
		
		try {
			roomRepo.saveRoom(room);
			List<Room> currentRooms = roomRepo.getAllRooms();		
			assertEquals(initialRoomsAmount + 1, currentRooms.size());
			assertEquals("testRoom", currentRooms.get(currentRooms.size() - 1).getRoomId());
			assertEquals("testRoomName", currentRooms.get(currentRooms.size() - 1).getBuildingName());
			assertEquals(0, currentRooms.get(currentRooms.size() - 1).getRoomNumber());
			assertEquals(0, currentRooms.get(currentRooms.size() - 1).getCapacity());
			assertEquals(RoomStatus.AVAILABLE, currentRooms.get(currentRooms.size() - 1).getStatus());
		}
		
		finally {
			roomRepo.deleteRoom(room.getRoomId());
		}
		
	}
	
	/*
	 * this test ensures that saving a null room
	 * does not add anything to the room database
	 */
	@Test
	public void saveNullRoom() {
		RoomRepository roomRepo = new RoomRepository();
		Room room = null;
		int initialRoomsAmount = roomRepo.getAllRooms().size();
		roomRepo.saveRoom(room);
		assertEquals(initialRoomsAmount, roomRepo.getAllRooms().size());
	
	}
	
	
	
	/*
	 * this test ensures that saving a duplicate room
	 * throws an IllegalArgumentException
	 */
	@Test
	(expected = IllegalArgumentException.class)
	public void saveDuplicateRoom() {
		RoomRepository roomRepo = new RoomRepository();
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		try {
			roomRepo.saveRoom(room);
			roomRepo.saveRoom(room);
		}
		finally {
			roomRepo.deleteRoom(room.getRoomId());
		}
	
	}
	
	
	
	
	/*
	 * this test ensures that updating a room
	 * properly updates in the database
	 */
	@Test
	public void updateRoom() {
		RoomRepository roomRepo = new RoomRepository();
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		try {
			roomRepo.saveRoom(room);
			room = new Room("testRoom", "testRoomNameUpdate", 1, 1, RoomStatus.MAINTENANCE);
			roomRepo.updateRoom(room);
			List<Room> currentRooms = roomRepo.getAllRooms();
			assertEquals("testRoom", currentRooms.get(currentRooms.size() - 1).getRoomId());
			assertEquals("testRoomNameUpdate", currentRooms.get(currentRooms.size() - 1).getBuildingName());
			assertEquals(1, currentRooms.get(currentRooms.size() - 1).getRoomNumber());
			assertEquals(1, currentRooms.get(currentRooms.size() - 1).getCapacity());
			assertEquals(RoomStatus.MAINTENANCE, currentRooms.get(currentRooms.size() - 1).getStatus());
		}
		finally {
			roomRepo.deleteRoom(room.getRoomId());
		}
	
	}
	
	
	/*
	 * this test ensures that deleteRoom() correctly
	 * deletes the proper room from our database
	 */
	@Test
	public void deleteRoom() {
		
		RoomRepository roomRepo = new RoomRepository();
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		int initialRoomsAmount = roomRepo.getAllRooms().size();
		try {
			roomRepo.saveRoom(room);
			roomRepo.deleteRoom(room.getRoomId());
			List<Room> currentRooms = roomRepo.getAllRooms();		
			assertEquals(initialRoomsAmount, currentRooms.size());
			assertEquals(null, roomRepo.findRoomById(room.getRoomId()));
		}
		finally {
			roomRepo.deleteRoom(room.getRoomId());
		}
	
	}
	
	
	/*
	 * this test ensures that findRoom() correctly
	 * finds the proper room from our database
	 */
	@Test
	public void findRoom() {
		RoomRepository roomRepo = new RoomRepository();
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		
		try {
			roomRepo.saveRoom(room);
			assertEquals(roomRepo.findRoomById(room.getRoomId()).getBuildingName(), room.getBuildingName());
			assertEquals(roomRepo.findRoomById(room.getRoomId()).getRoomNumber(), room.getRoomNumber());
			assertEquals(roomRepo.findRoomById(room.getRoomId()).getCapacity(), room.getCapacity());
			assertEquals(roomRepo.findRoomById(room.getRoomId()).getStatus(), room.getStatus());
			
		}
		finally {
			roomRepo.deleteRoom(room.getRoomId());
		}

	}
	
	
	
	/*
	 * this test ensures that findRoom() correctly
	 * return null when we pass a null id
	 */
	@Test
	public void findNullRoom() {
		RoomRepository roomRepo = new RoomRepository();
		assertEquals(null, roomRepo.findRoomById(null));

	}
	
	
	
	
	/*
	 * this test ensures that findRoom() correctly
	 * return null when we pass an id that DNE
	 */
	@Test
	public void findDNERoom() {
		RoomRepository roomRepo = new RoomRepository();
		assertEquals(null, roomRepo.findRoomById("!dawifma0292@#!"));

	}
	
	
	
	
	/*
	 * this test ensures that getAllRoom() correctly
	 * returns a list of all rooms from our database
	 */
	@Test
	public void getAllRoom() {
		RoomRepository roomRepo = new RoomRepository();
		Room room = new Room("testRoom", "testRoomName", 0, 0, RoomStatus.AVAILABLE);
		int initialRoomsAmount = roomRepo.getAllRooms().size();
		
		try {
			roomRepo.saveRoom(room);
			assertEquals(initialRoomsAmount + 1, roomRepo.getAllRooms().size());
			
		}
		finally {
			roomRepo.deleteRoom(room.getRoomId());
		}

	}
	
	
	
	
	
	
	
	
	

}
