package roombooking.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.model.*;
import roombooking.repository.AccountRepository;


public class ChiefEventCoordinatorTester {
	
	/*
	 * this test checks that once we create
	 * a chief object, the passed ID number 
	 * is properly set
	 */
	@Test
	public void testChiefId() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		assertEquals("cheifTest", chief.getIDNumber());
	}
	
	/*
	 * this test checks that the chiefs
	 * hourly rate is 0
	 */
	@Test
	public void testChiefHourlyRate() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		assertEquals(0.00, chief.getHRate(), 0.001);
	}
	
	/*
	 * this method checks that generating
	 * an admin account with valid information properly
	 * creates an admin object
	 */
	@Test
	public void testGenerateValidAdminAccount() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");
		AccountRepository accountRepo = new AccountRepository();

		Account account = null;

		try {
			account = chief.generateAdminAccount("testadmin", "testadmin@test.com", "Password1@");

			assertEquals("testadmin", account.getUserName());
			assertEquals("testadmin@test.com", account.getEmail());
			assertEquals("Password1@", account.getPassword());
			assertEquals(AccountType.ADMIN, account.getAccountType());
			assertEquals(true, account.getRegisteredUser() instanceof Admin);

		} 
		
		finally {
			accountRepo.deleteUser(account.getUserName());
			
		}
	}
	
	
	/*
	 * this method checks that generating
	 * an admin account with valid information properly
	 * stores the admin in the database
	 */
	@Test
	public void testGenerateAdminAccountDatabase() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");
		AccountRepository accountRepo = new AccountRepository();

		Account account = null;

		try {
			account = chief.generateAdminAccount("testadmin", "testadmin@test.com", "Password1@");
			
			List<Account> accounts = accountRepo.getAllAccounts();

			assertEquals("testadmin", accounts.get(accounts.size() - 1).getUserName());
			assertEquals("testadmin@test.com", accounts.get(accounts.size() - 1).getEmail());
			assertEquals("Password1@", accounts.get(accounts.size() - 1).getPassword());
			assertEquals(AccountType.ADMIN, accounts.get(accounts.size() - 1).getAccountType());
			assertEquals(true, accounts.get(accounts.size() - 1).getRegisteredUser() instanceof Admin);
		} 
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	/*
	 * this test ensures that when a blank username is passed
	 * we throw an illegal argument exception. This would then be caught
	 * by the caller and display an appropriate error message on the gui.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAdminRejectsBlankUsername() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		chief.generateAdminAccount("","admin@email.com","Password1!");
	}
	
	/*
	 * same as above but with blank email
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAdminRejectsBlankEmail() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		chief.generateAdminAccount("testadmin", "","Password1!");
	}
	
	
	/*
	 * same as above but with blank password
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAdminRejectsBlankPassword() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		chief.generateAdminAccount("testadmin", "admin@email.com", "");
	}
	
	/*
	 * this test ensures that when an invalid email is passed
	 * we throw an illegal argument exception. This would then be caught
	 * by the caller and display an appropriate error message on the gui.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAdminRejectsInvalidEmail() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		chief.generateAdminAccount("testadmin", "invalidemail", "Password1!");
	}
	
	
	/*
	 * same as above but with password
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAdminRejectsInvalidPassword() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("cheifTest");

		chief.generateAdminAccount("testadmin", "admin@email.com", "invalidpassword");
	}
	
	
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateAdminRejectsDuplicateUsername() {
		ChiefEventCoordinator chief = new ChiefEventCoordinator("C001");
		AccountRepository accountRepo = new AccountRepository();

		Account account1 = null;
		Account account2 = null;

		try {
			account1 = chief.generateAdminAccount("testadmin", "testadmin@test1.com", "Password1@");
			account2 = chief.generateAdminAccount("testadmin", "testadmin@test2.com", "Password2@");
	

		} 
		finally {
			accountRepo.deleteUser(account1.getUserName());
		}
	}

}
