package users;

import java.util.List;

//unregistered users who want to browse available rooms and want to become a registered user 
public class GuestUser extends User {

 private int guestID;

 // getter methods 
 public GuestUser(int guestID) {
     this.guestID = guestID;
 }

 public int getGuestID() {
     return guestID;
 }


 // register the user as a new RegisteredUser 
 public RegisteredUser register(String accountType, String name, String email,
                                 String password, String idNumber) {
     AccountFactory factory = resolveFactory(accountType);
     return factory.createAccount(name, email, password, idNumber);
 }

 
 // select the account type 
 private AccountFactory resolveFactory(String accountType) {
	 
     if (accountType == null) {
         throw new IllegalArgumentException("accountType must not be null");
     }
     
     switch (accountType.toLowerCase()) {
         case "student":
             return new StudentAccountFactory();
             
         case "faculty":
             return new FacultyAccountFactory();
             
         case "staff":
             return new StaffAccountFactory();
             
         case "partner":
             return new PartnerAccountFactory();
             
         default:
             throw new IllegalArgumentException("Unknown account type: " + accountType);
     }
 }

 @Override
 public List<Room> searchAvailableRooms() {
     throw new UnsupportedOperationException("Wire GuestUser to a RoomManagementController");
 }
}