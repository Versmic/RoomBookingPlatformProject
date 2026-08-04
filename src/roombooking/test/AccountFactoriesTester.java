package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.factory.*;
import roombooking.model.*;
import roombooking.repository.*;



public class AccountFactoriesTester {
	
	/*
	 * This test makes sure that FacultyFactory createAccount method
	 * creates, stores, and returns accurately. For this test we are
	 * disregarding valid data until later tests.
	 */
	@Test
	public void testCreateFacultyAccount() {
		FacultyAccountFactory factory = new FacultyAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();

		Account account = null;

		try {
			// create, store, and return account
			account = factory.createAccount("testfaculty", "password", "faculty@test.com", "testFaculty");
			
			// test if account object has correct parameters and is of type faculty
			assertEquals("testfaculty", account.getUserName());
			assertEquals("password", account.getPassword());
			assertEquals("faculty@test.com", account.getEmail());
			assertEquals("testFaculty", account.getRegisteredUser().getIDNumber());
			assertEquals(AccountType.FACULTY, account.getAccountType());
			assertEquals(true, account.getRegisteredUser() instanceof Faculty);
			// get all accounts stored in the database
			List<Account> accounts = accountRepo.getAllAccounts();
			// compare the last account stored to the information we expect
			assertEquals("testfaculty", accounts.get(accounts.size() - 1).getUserName());
			assertEquals("password", accounts.get(accounts.size() - 1).getPassword());
			assertEquals("faculty@test.com", accounts.get(accounts.size() - 1).getEmail());
			assertEquals(AccountType.FACULTY, accounts.get(accounts.size() - 1).getAccountType());

		} 
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * This test makes sure that StudentFactory createAccount method
	 * creates, stores, and returns accurately. For this test we are
	 * disregarding valid data until later tests.
	 */
	@Test
	public void testCreateStudentAccount() {
		StudentAccountFactory factory = new StudentAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();

		Account account = null;

		try {
			// create, store, and return account
			account = factory.createAccount("teststudent", "password", "student@test.com", "testStudent");
			
			// test if account object has correct parameters and is of type student
			assertEquals("teststudent", account.getUserName());
			assertEquals("password", account.getPassword());
			assertEquals("student@test.com", account.getEmail());
			assertEquals("testStudent", account.getRegisteredUser().getIDNumber());
			assertEquals(AccountType.STUDENT, account.getAccountType());
			assertEquals(true, account.getRegisteredUser() instanceof Student);
			// get all accounts stored in the database
			List<Account> accounts = accountRepo.getAllAccounts();
			// compare the last account stored to the information we expect
			assertEquals("teststudent", accounts.get(accounts.size() - 1).getUserName());
			assertEquals("password", accounts.get(accounts.size() - 1).getPassword());
			assertEquals("student@test.com", accounts.get(accounts.size() - 1).getEmail());
			assertEquals(AccountType.STUDENT, accounts.get(accounts.size() - 1).getAccountType());
		} 
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * This test makes sure that StaffFactory createAccount method
	 * creates, stores, and returns accurately. For this test we are
	 * disregarding valid data until later tests.
	 */
	@Test
	public void testCreateStaffAccount() {
		StaffAccountFactory factory = new StaffAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();

		Account account = null;

		try {
			// create, store, and return account
			account = factory.createAccount("teststaff", "password", "staff@test.com", "testStaff");
			
			// test if account object has correct parameters and is of type staff
			assertEquals("teststaff", account.getUserName());
			assertEquals("password", account.getPassword());
			assertEquals("staff@test.com", account.getEmail());
			assertEquals("testStaff", account.getRegisteredUser().getIDNumber());
			assertEquals(AccountType.STAFF, account.getAccountType());		
			assertEquals(true, account.getRegisteredUser() instanceof Staff);
			// get all accounts stored in the database
			List<Account> accounts = accountRepo.getAllAccounts();
			// compare the last account stored to the information we expect
			assertEquals("teststaff", accounts.get(accounts.size() - 1).getUserName());
			assertEquals("password", accounts.get(accounts.size() - 1).getPassword());
			assertEquals("staff@test.com", accounts.get(accounts.size() - 1).getEmail());
			assertEquals(AccountType.STAFF, accounts.get(accounts.size() - 1).getAccountType());
		} 
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * This test makes sure that PartnerFactory createAccount method
	 * creates, stores, and returns accurately. For this test we are
	 * disregarding valid data until later tests.
	 */
	@Test
	public void testCreatePartnerAccount() {
		PartnerAccountFactory factory = new PartnerAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();

		Account account = null;

		try {
			// create, store, and return account
			account = factory.createAccount("testpartner", "password", "partner@test.com", "testPartner");
			
			// test if account object has correct parameters and is of type staff
			assertEquals("testpartner", account.getUserName());
			assertEquals("password", account.getPassword());
			assertEquals("partner@test.com", account.getEmail());
			assertEquals("testPartner", account.getRegisteredUser().getIDNumber());
			assertEquals(AccountType.PARTNER, account.getAccountType());
			assertEquals(true, account.getRegisteredUser() instanceof Partner);
			// get all accounts stored in the database
			List<Account> accounts = accountRepo.getAllAccounts();
			// compare the last account stored to the information we expect
			assertEquals("testpartner", accounts.get(accounts.size() - 1).getUserName());
			assertEquals("password", accounts.get(accounts.size() - 1).getPassword());
			assertEquals("partner@test.com", accounts.get(accounts.size() - 1).getEmail());
			assertEquals(AccountType.PARTNER, accounts.get(accounts.size() - 1).getAccountType());
		} 
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	/*
	 * this test ensures that CheckForUniqueEmail
	 * returns true when an email already exists.
	 * For this test we are disregarding valid data until later tests.
	 * This method is put in place before any account 
	 * can be created in our login LoginPanel
	 */
	@Test
	public void testCheckForUniqueEmail() {
		StudentAccountFactory factory = new StudentAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();
		
		Account account = null;
		try {
			// create, store and return account
			account = factory.createAccount("teststudent", "password", "student@test.com", "testStudent");
			// run CheckForUniqueEmail(), if it is true then the email already exists
			// otherwise it returns false
			assertEquals(true, factory.CheckForUniqueEmail("student@test.com"));
			assertEquals(false, factory.CheckForUniqueEmail("!awdoindslwad!@gmaiIs.com"));
		}
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	
	/*
	 * this test ensures that CheckForStrongPassword()
	 * correctly returns false when password are invalid
	 */
	@Test
	public void testCheckNonStrongPassword() {
		StudentAccountFactory factory = new StudentAccountFactory();
		
		// password not > 8 characters
		assertEquals(false, factory.CheckForStrongPassword("ad@#A2")); 
		// password doesnt contain capital letter
		assertEquals(false, factory.CheckForStrongPassword("awd@#23232"));
		// password doesnt contain lowercase
		assertEquals(false, factory.CheckForStrongPassword("AWDDWAD23@#"));
		// password doesnt contain numbers
		assertEquals(false, factory.CheckForStrongPassword("awdADW@#%"));
		// password doesnt contain symbols
		assertEquals(false, factory.CheckForStrongPassword("awdADW1232"));
	}
	
	
	/*
	 * this test ensures that CheckForUniqueUsername
	 * returns true when a username already exists.
	 * For this test we are disregarding valid data until later tests.
	 * This method is put in place before any account 
	 * can be created in our login LoginPanel
	 */
	@Test
	public void testCheckForUniqueUsername() {
		StudentAccountFactory factory = new StudentAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();
		
		Account account = null;
		try {
			// create, store and return account
			account = factory.createAccount("teststudent", "password", "student@test.com", "testStudent");
			// run CheckForUniqueUsername(), if it is true then the username already exists
			// otherwise it returns false
			assertEquals(true, factory.CheckForUniqueUsername("teststudent"));
			assertEquals(false, factory.CheckForUniqueUsername("!awdoindslwad!"));
		}
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * this test ensures that checkForUniqueID
	 * returns true when an ID already exists.
	 * For this test we are disregarding valid data until later tests.
	 * This method is put in place before any account 
	 * can be created in our login LoginPanel
	 */
	@Test
	public void testCheckForUniqueID() {
		StudentAccountFactory factory = new StudentAccountFactory();
		// account repo so we can access account database
		AccountRepository accountRepo = new AccountRepository();
		
		Account account = null;
		try {
			// create, store and return account
			account = factory.createAccount("teststudent", "password", "student@test.com", "testStudent");
			// run checkForUniqueID(), if it is true then the ID already exists
			// otherwise it returns false
			assertEquals(true, factory.checkForUniqueID("testStudent"));
			assertEquals(false, factory.checkForUniqueID("!awdoindslwad!"));
		}
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	/*
	 * this test ensures that CheckForStrongPassword
	 * returns true when a password with 
	 * at least 1 lowercase
	 * at least 1 uppercase
	 * at least 1 number
	 * at least 1 symbol
	 * and greater or equal to 8 characters
	 */
	@Test
	public void testCheckForStrongPassword() {
		StudentAccountFactory factory = new StudentAccountFactory();
		// password is valid
		assertEquals(true, factory.CheckForStrongPassword("awdADW1232@"));	
	}
	
	
	/*
	 * this test ensures that CheckForValidEmail
	 * returns true when a an email is passed with
	 * an @ symbol
	 * a period
	 * and no spaces
	 */
	@Test
	public void testCheckForValidEmail() {
		StudentAccountFactory factory = new StudentAccountFactory();
		// valid email contains an @ symbol, a period, and no spaces
		assertEquals(true, factory.CheckForValidEmail("student@test.com"));
	}

	
	
	/*
	 * this test ensures that CheckForValidEmail
	 * returns false when an invalid email is passed
	 */
	@Test
	public void testCheckForInvalidEmail() {
		StudentAccountFactory factory = new StudentAccountFactory();
		
		// email does not contain an @ symbol
		assertEquals(false, factory.CheckForValidEmail("studenttest.com"));
		
		// email does not contain a period
		assertEquals(false, factory.CheckForValidEmail("student@testcom"));
		
		// email contains a space
		assertEquals(false, factory.CheckForValidEmail("student @test.com"));
	}
}
