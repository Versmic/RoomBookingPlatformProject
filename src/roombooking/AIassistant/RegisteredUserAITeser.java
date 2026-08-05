package roombooking.AIassistant;

import static org.junit.Assert.*;

import org.junit.Test;

import roombooking.enums.RoomStatus;
import roombooking.model.Account;
import roombooking.model.Admin;
import roombooking.model.ChiefEventCoordinator;
import roombooking.model.Faculty;
import roombooking.model.Partner;
import roombooking.model.RegisteredUser;
import roombooking.model.Room;
import roombooking.model.Staff;
import roombooking.model.Student;
import roombooking.repository.AccountRepository;


public class RegisteredUserAITeser {


    // ==========================
    // Student Tests
    // ==========================

    @Test
    public void testStudentConstructor() {

        Student student = new Student("S12345");

        assertEquals("S12345", student.getIDNumber());
        assertEquals(20, student.getHRate(), 0.0);
    }



    // ==========================
    // Faculty Tests
    // ==========================

    @Test
    public void testFacultyConstructor() {

        Faculty faculty = new Faculty("F12345");

        assertEquals("F12345", faculty.getIDNumber());
        assertEquals(30, faculty.getHRate(), 0.0);
    }



    // ==========================
    // Staff Tests
    // ==========================

    @Test
    public void testStaffConstructor() {

        Staff staff = new Staff("ST12345");

        assertEquals("ST12345", staff.getIDNumber());
        assertEquals(40, staff.getHRate(), 0.0);
    }



    // ==========================
    // Partner Tests
    // ==========================

    @Test
    public void testPartnerConstructor() {

        Partner partner = new Partner("ORG123");

        assertEquals("ORG123", partner.getIDNumber());
        assertEquals(50, partner.getHRate(), 0.0);
    }



    // ==========================
    // Admin Tests
    // ==========================

    @Test
    public void testAdminConstructor() {

        Admin admin = new Admin("A001");

        assertEquals("A001", admin.getIDNumber());
        assertEquals(0, admin.getHRate(), 0.0);
    }



    @Test
    public void testAdminEnableRoom() {

        Admin admin = new Admin("A001");

        Room room = new Room(
                "R001",
                "Main Building",
                101,
                50,
                RoomStatus.DISABLED
        );


        admin.enableRoom(room);

        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
    }



    @Test
    public void testAdminDisableRoom() {

        Admin admin = new Admin("A001");

        Room room = new Room(
                "R001",
                "Main Building",
                101,
                50,
                RoomStatus.AVAILABLE
        );


        admin.disableRoom(room);

        assertEquals(RoomStatus.DISABLED, room.getStatus());
    }



    @Test
    public void testAdminMaintenanceRoom() {

        Admin admin = new Admin("A001");

        Room room = new Room(
                "R001",
                "Main Building",
                101,
                50,
                RoomStatus.AVAILABLE
        );


        admin.maintenanceRoom(room);

        assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
    }



    // ==========================
    // Chief Event Coordinator Tests
    // ==========================

    @Test
    public void testChiefConstructor() {

        ChiefEventCoordinator chief =
                new ChiefEventCoordinator("C001");


        assertEquals("C001", chief.getIDNumber());
        assertEquals(0, chief.getHRate(), 0.0);
    }



    @Test
    public void testChiefEnableRoom() {

        ChiefEventCoordinator chief =
                new ChiefEventCoordinator("C001");


        Room room = new Room(
                "R001",
                "Main Building",
                101,
                50,
                RoomStatus.DISABLED
        );


        chief.enableRoom(room);


        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
    }



    @Test
    public void testChiefDisableRoom() {

        ChiefEventCoordinator chief =
                new ChiefEventCoordinator("C001");


        Room room = new Room(
                "R001",
                "Main Building",
                101,
                50,
                RoomStatus.AVAILABLE
        );


        chief.disableRoom(room);


        assertEquals(RoomStatus.DISABLED, room.getStatus());
    }



    @Test
    public void testChiefMaintenanceRoom() {

        ChiefEventCoordinator chief =
                new ChiefEventCoordinator("C001");


        Room room = new Room(
                "R001",
                "Main Building",
                101,
                50,
                RoomStatus.AVAILABLE
        );


        chief.maintainenceRoom(room);


        assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
    }



    // ==========================
    // Inheritance Tests
    // ==========================

    @Test
    public void testUsersAreRegisteredUsers() {

        Student student = new Student("S1");
        Faculty faculty = new Faculty("F1");
        Staff staff = new Staff("ST1");
        Partner partner = new Partner("P1");
        Admin admin = new Admin("A1");
        ChiefEventCoordinator chief =
                new ChiefEventCoordinator("C1");


        assertTrue(student instanceof RegisteredUser);
        assertTrue(faculty instanceof RegisteredUser);
        assertTrue(staff instanceof RegisteredUser);
        assertTrue(partner instanceof RegisteredUser);
        assertTrue(admin instanceof RegisteredUser);
        assertTrue(chief instanceof RegisteredUser);
    }

    
 // ==========================
 // Chief Generate Admin Account Tests
 // ==========================


 @Test
 public void testChiefGenerateAdminAccountSuccessfully() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     Account account =
             chief.generateAdminAccount(
                     "testAdmin001",
                     "admin001@test.com",
                     "Password1!"
             );


     assertNotNull(account);

     assertEquals(
             "testAdmin001",
             account.getUserName()
     );


     assertEquals(
             "admin001@test.com",
             account.getEmail()
     );


     assertEquals(
             roombooking.enums.AccountType.ADMIN,
             account.getAccountType()
     );


     // cleanup
     AccountRepository repo =
             new AccountRepository();

     repo.deleteUser("testAdmin001");
 }



 @Test
 public void testChiefGenerateAdminAccountEmptyUsername() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     assertThrows(
             IllegalArgumentException.class,
             () -> {
                 chief.generateAdminAccount(
                         "",
                         "admin@test.com",
                         "Password1!"
                 );
             }
     );
 }



 @Test
 public void testChiefGenerateAdminAccountEmptyEmail() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     assertThrows(
             IllegalArgumentException.class,
             () -> {
                 chief.generateAdminAccount(
                         "adminEmptyEmail",
                         "",
                         "Password1!"
                 );
             }
     );
 }



 @Test
 public void testChiefRejectsInvalidEmail() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     assertThrows(
             IllegalArgumentException.class,
             () -> {
                 chief.generateAdminAccount(
                         "invalidEmailAdmin",
                         "invalidemail",
                         "Password1!"
                 );
             }
     );
 }



 @Test
 public void testChiefRejectsWeakPassword() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     assertThrows(
             IllegalArgumentException.class,
             () -> {
                 chief.generateAdminAccount(
                         "weakPasswordAdmin",
                         "weak@test.com",
                         "password"
                 );
             }
     );
 }



 @Test
 public void testChiefRejectsDuplicateUsername() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     Account first =
             chief.generateAdminAccount(
                     "duplicateAdmin",
                     "first@test.com",
                     "Password1!"
             );


     assertNotNull(first);



     assertThrows(
             IllegalArgumentException.class,
             () -> {

                 chief.generateAdminAccount(
                         "duplicateAdmin",
                         "second@test.com",
                         "Password1!"
                 );

             }
     );


     AccountRepository repo =
             new AccountRepository();


     repo.deleteUser("duplicateAdmin");
 }



 @Test
 public void testChiefRejectsDuplicateEmail() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     Account first =
             chief.generateAdminAccount(
                     "emailAdmin1",
                     "sameemail@test.com",
                     "Password1!"
             );


     assertNotNull(first);



     assertThrows(
             IllegalArgumentException.class,
             () -> {

                 chief.generateAdminAccount(
                         "emailAdmin2",
                         "sameemail@test.com",
                         "Password2!"
                 );

             }
     );


     AccountRepository repo =
             new AccountRepository();


     repo.deleteUser("emailAdmin1");
 }



 @Test
 public void testChiefGetNextAdminId() {

     ChiefEventCoordinator chief =
             new ChiefEventCoordinator("C001");


     String nextId =
             chief.getNextAdminId();


     assertNotNull(nextId);


     assertTrue(
             Integer.parseInt(nextId) > 0
     );
 }
}