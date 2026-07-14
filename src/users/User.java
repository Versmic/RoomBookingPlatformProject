package users;

import java.util.List;

//abstract base class
public abstract class User {
	
	// search for available rooms and return a list of the currently available ones 
	public abstract List<Room> searchAvailableRooms();
	
}