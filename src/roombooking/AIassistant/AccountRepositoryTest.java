package roombooking.AIassistant;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.model.Account;
import roombooking.model.Admin;
import roombooking.model.Student;
import roombooking.repository.AccountRepository;



public class AccountRepositoryTest {


    private AccountRepository repository;

    private Account studentAccount;



    @Before
    public void setUp() {


        repository = new AccountRepository();


        studentAccount =
                new Account(
                        "teststudent",
                        "Password1!",
                        "student@test.com",
                        AccountType.STUDENT,
                        new Student("ST001")
                );

    }




    @Test
    public void testSaveAccount() {


        repository.saveAccount(
                studentAccount
        );


        Account result =
                repository.findAccountByUserName(
                        "teststudent"
                );


        assertNotNull(result);


        assertEquals(
                "teststudent",
                result.getUserName()
        );

    }





    @Test
    public void testFindAccountByUsernameNotFound() {


        Account result =
                repository.findAccountByUserName(
                        "doesnotexist"
                );


        assertNull(result);

    }





    @Test
    public void testLoginSuccessful() {


        repository.saveAccount(
                studentAccount
        );


        boolean result =
                repository.login(
                        "teststudent",
                        "Password1!"
                );


        assertTrue(result);

    }





    @Test
    public void testLoginWrongPassword() {


        repository.saveAccount(
                studentAccount
        );


        boolean result =
                repository.login(
                        "teststudent",
                        "WrongPassword"
                );


        assertFalse(result);

    }





    @Test
    public void testEmailExists() {


        repository.saveAccount(
                studentAccount
        );


        assertTrue(
                repository.emailExists(
                        "student@test.com"
                )
        );

    }





    @Test
    public void testEmailDoesNotExist() {


        assertFalse(
                repository.emailExists(
                        "missing@test.com"
                )
        );

    }





    @Test
    public void testUsernameExists() {


        repository.saveAccount(
                studentAccount
        );


        assertTrue(
                repository.usernameExists(
                        "teststudent"
                )
        );

    }





    @Test
    public void testUsernameDoesNotExist() {


        assertFalse(
                repository.usernameExists(
                        "unknownuser"
                )
        );

    }





    @Test
    public void testIDNumberExists() {


        repository.saveAccount(
                studentAccount
        );


        assertTrue(
                repository.idNumberExists(
                        "ST001"
                )
        );

    }





    @Test
    public void testIDNumberDoesNotExist() {


        assertFalse(
                repository.idNumberExists(
                        "UNKNOWN001"
                )
        );

    }





    @Test
    public void testGetAllAccounts() {


        repository.saveAccount(
                studentAccount
        );


        ArrayList<Account> accounts =
                repository.getAllAccounts();



        assertNotNull(accounts);


        assertTrue(
                accounts.size() > 0
        );

    }





    @Test
    public void testUpdateAccount() {


        repository.saveAccount(
                studentAccount
        );


        Account updated =
                new Account(
                        "teststudent",
                        "NewPassword1!",
                        "newemail@test.com",
                        AccountType.STUDENT,
                        new Student("ST001")
                );


        repository.updateAccount(
                updated
        );



        Account result =
                repository.findAccountByUserName(
                        "teststudent"
                );



        assertEquals(
                "newemail@test.com",
                result.getEmail()
        );

    }





    @Test
    public void testDeleteAccount() {


        repository.saveAccount(
                studentAccount
        );


        repository.deleteUser(
                "teststudent"
        );



        Account result =
                repository.findAccountByUserName(
                        "teststudent"
                );


        assertNull(result);

    }





    @Test
    public void testGenerateNextAdminId() {


        Account admin =
                new Account(
                        "adminUser",
                        "Admin123!",
                        "admin@test.com",
                        AccountType.ADMIN,
                        new Admin("100")
                );


        repository.saveAccount(
                admin
        );


        String nextId =
                repository.generateNextAdminId();



        assertEquals(
                "101",
                nextId
        );

    }


}