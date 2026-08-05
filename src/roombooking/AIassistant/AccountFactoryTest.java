package roombooking.AIassistant;

import static org.junit.Assert.*;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.factory.FacultyAccountFactory;
import roombooking.factory.PartnerAccountFactory;
import roombooking.factory.StaffAccountFactory;
import roombooking.factory.AccountFactory; 
import roombooking.factory.StudentAccountFactory;
import roombooking.model.Account;
import roombooking.model.Faculty;
import roombooking.model.Partner;
import roombooking.model.Staff;
import roombooking.model.Student;
import roombooking.repository.AccountRepository;


public class AccountFactoryTest {


    // ==========================
    // Student Factory Tests
    // ==========================


    @Test
    public void testStudentFactoryCreatesStudentAccount() {

        StudentAccountFactory factory =
                new StudentAccountFactory();


        Account account =
                factory.createAccount(
                        "studentTest001",
                        "Password1!",
                        "student001@test.com",
                        "S001"
                );


        assertNotNull(account);

        assertEquals(
                AccountType.STUDENT,
                account.getAccountType()
        );


        assertTrue(
                account.getRegisteredUser()
                instanceof Student
        );


        assertEquals(
                "S001",
                account.getRegisteredUser()
                .getIDNumber()
        );


        cleanup("studentTest001");
    }



    // ==========================
    // Faculty Factory Tests
    // ==========================


    @Test
    public void testFacultyFactoryCreatesFacultyAccount() {


        FacultyAccountFactory factory =
                new FacultyAccountFactory();



        Account account =
                factory.createAccount(
                        "facultyTest001",
                        "Password1!",
                        "faculty001@test.com",
                        "F001"
                );



        assertNotNull(account);


        assertEquals(
                AccountType.FACULTY,
                account.getAccountType()
        );


        assertTrue(
                account.getRegisteredUser()
                instanceof Faculty
        );


        assertEquals(
                "F001",
                account.getRegisteredUser()
                .getIDNumber()
        );


        cleanup("facultyTest001");
    }




    // ==========================
    // Staff Factory Tests
    // ==========================


    @Test
    public void testStaffFactoryCreatesStaffAccount() {


        StaffAccountFactory factory =
                new StaffAccountFactory();



        Account account =
                factory.createAccount(
                        "staffTest001",
                        "Password1!",
                        "staff001@test.com",
                        "ST001"
                );



        assertNotNull(account);


        assertEquals(
                AccountType.STAFF,
                account.getAccountType()
        );


        assertTrue(
                account.getRegisteredUser()
                instanceof Staff
        );


        assertEquals(
                "ST001",
                account.getRegisteredUser()
                .getIDNumber()
        );


        cleanup("staffTest001");
    }





    // ==========================
    // Partner Factory Tests
    // ==========================


    @Test
    public void testPartnerFactoryCreatesPartnerAccount() {


        PartnerAccountFactory factory =
                new PartnerAccountFactory();



        Account account =
                factory.createAccount(
                        "partnerTest001",
                        "Password1!",
                        "partner001@test.com",
                        "P001"
                );



        assertNotNull(account);



        assertEquals(
                AccountType.PARTNER,
                account.getAccountType()
        );



        assertTrue(
                account.getRegisteredUser()
                instanceof Partner
        );



        assertEquals(
                "P001",
                account.getRegisteredUser()
                .getIDNumber()
        );



        cleanup("partnerTest001");
    }





    // ==========================
    // Repository Save Tests
    // ==========================


    @Test
    public void testStudentFactorySavesAccount() {


        StudentAccountFactory factory =
                new StudentAccountFactory();



        factory.createAccount(
                "savedStudent001",
                "Password1!",
                "savedstudent@test.com",
                "S999"
        );



        AccountRepository repo =
                new AccountRepository();



        Account result =
                repo.findAccountByUserName(
                        "savedStudent001"
                );



        assertNotNull(result);


        assertEquals(
                AccountType.STUDENT,
                result.getAccountType()
        );


        cleanup("savedStudent001");
    }





    // ==========================
    // Factory Rate Tests
    // ==========================


    @Test
    public void testStudentHourlyRateFromFactory() {


        StudentAccountFactory factory =
                new StudentAccountFactory();


        Account account =
                factory.createAccount(
                        "rateStudent001",
                        "Password1!",
                        "rateStudent@test.com",
                        "S555"
                );



        assertEquals(
                20,
                account.getRegisteredUser()
                .getHRate(),
                0.0
        );


        cleanup("rateStudent001");
    }



    @Test
    public void testFacultyHourlyRateFromFactory() {


        FacultyAccountFactory factory =
                new FacultyAccountFactory();


        Account account =
                factory.createAccount(
                        "rateFaculty001",
                        "Password1!",
                        "rateFaculty@test.com",
                        "F555"
                );


        assertEquals(
                30,
                account.getRegisteredUser()
                .getHRate(),
                0.0
        );


        cleanup("rateFaculty001");
    }




    @Test
    public void testStaffHourlyRateFromFactory() {


        StaffAccountFactory factory =
                new StaffAccountFactory();


        Account account =
                factory.createAccount(
                        "rateStaff001",
                        "Password1!",
                        "rateStaff@test.com",
                        "ST555"
                );


        assertEquals(
                40,
                account.getRegisteredUser()
                .getHRate(),
                0.0
        );


        cleanup("rateStaff001");
    }





    @Test
    public void testPartnerHourlyRateFromFactory() {


        PartnerAccountFactory factory =
                new PartnerAccountFactory();


        Account account =
                factory.createAccount(
                        "ratePartner001",
                        "Password1!",
                        "ratePartner@test.com",
                        "P555"
                );


        assertEquals(
                50,
                account.getRegisteredUser()
                .getHRate(),
                0.0
        );


        cleanup("ratePartner001");
    }





    private void cleanup(String username){

        AccountRepository repo =
                new AccountRepository();

        repo.deleteUser(username);
    }

 // ==========================
 // AccountFactory Validation Tests
 // ==========================


 @Test
 public void testStrongPasswordValid() {

     AccountFactory factory =
             new StudentAccountFactory();


     boolean result =
             factory.CheckForStrongPassword(
                     "Password1!"
             );


     assertTrue(result);
 }



 @Test
 public void testStrongPasswordMissingSymbol() {

     AccountFactory factory =
             new StudentAccountFactory();


     boolean result =
             factory.CheckForStrongPassword(
                     "Password123"
             );


     assertFalse(result);
 }



 @Test
 public void testStrongPasswordMissingUppercase() {

     AccountFactory factory =
             new StudentAccountFactory();


     boolean result =
             factory.CheckForStrongPassword(
                     "password1!"
             );


     assertFalse(result);
 }



 @Test
 public void testStrongPasswordMissingNumber() {

     AccountFactory factory =
             new StudentAccountFactory();


     boolean result =
             factory.CheckForStrongPassword(
                     "Password!"
             );


     assertFalse(result);
 }



 @Test
 public void testStrongPasswordTooShort() {

     AccountFactory factory =
             new StudentAccountFactory();


     boolean result =
             factory.CheckForStrongPassword(
                     "Pa1!"
             );


     assertFalse(result);
 }





 // ==========================
 // Email Validation Tests
 // ==========================


 @Test
 public void testValidEmail() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertTrue(
             factory.CheckForValidEmail(
                     "student@test.com"
             )
     );
 }



 @Test
 public void testInvalidEmailMissingAtSymbol() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertFalse(
             factory.CheckForValidEmail(
                     "studenttest.com"
             )
     );
 }



 @Test
 public void testInvalidEmailMissingDot() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertFalse(
             factory.CheckForValidEmail(
                     "student@test"
             )
     );
 }



 @Test
 public void testInvalidEmailContainsSpace() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertFalse(
             factory.CheckForValidEmail(
                     "student @test.com"
             )
     );
 }





 // ==========================
 // Unique Username Tests
 // ==========================


 @Test
 public void testUsernameDoesNotExist() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertFalse(
             factory.CheckForUniqueUsername(
                     "newUniqueStudent999"
             )
     );
 }



 @Test
 public void testUsernameExists() {


     StudentAccountFactory studentFactory =
             new StudentAccountFactory();


     studentFactory.createAccount(
             "existingStudent999",
             "Password1!",
             "existing999@test.com",
             "S999"
     );


     AccountFactory factory =
             new StudentAccountFactory();



     assertTrue(
             factory.CheckForUniqueUsername(
                     "existingStudent999"
             )
     );


     cleanup("existingStudent999");
 }





 // ==========================
 // Unique Email Tests
 // ==========================


 @Test
 public void testEmailDoesNotExist() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertFalse(
             factory.CheckForUniqueEmail(
                     "newemail999@test.com"
             )
     );
 }



 @Test
 public void testEmailExists() {


     StudentAccountFactory studentFactory =
             new StudentAccountFactory();



     studentFactory.createAccount(
             "emailCheckStudent999",
             "Password1!",
             "exists999@test.com",
             "S888"
     );



     AccountFactory factory =
             new StudentAccountFactory();



     assertTrue(
             factory.CheckForUniqueEmail(
                     "exists999@test.com"
             )
     );


     cleanup("emailCheckStudent999");
 }





 // ==========================
 // Unique ID Tests
 // ==========================


 @Test
 public void testIDDoesNotExist() {

     AccountFactory factory =
             new StudentAccountFactory();


     assertFalse(
             factory.checkForUniqueID(
                     "NEWID999"
             )
     );
 }



 @Test
 public void testIDExists() {


     StudentAccountFactory studentFactory =
             new StudentAccountFactory();



     studentFactory.createAccount(
             "idCheckStudent999",
             "Password1!",
             "idcheck999@test.com",
             "EXISTING999"
     );



     AccountFactory factory =
             new StudentAccountFactory();



     assertTrue(
             factory.checkForUniqueID(
                     "EXISTING999"
             )
     );


     cleanup("idCheckStudent999");
 }
}