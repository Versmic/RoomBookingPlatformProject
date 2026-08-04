package roombooking.test;

import org.junit.Test;

import roombooking.enums.AccountType;
import roombooking.model.Account;
import roombooking.model.Student;
import roombooking.repository.AccountRepository;

import static org.junit.Assert.assertEquals;

import java.util.List;

public class AccountRepositoryTester {
	
	/*
	 * this test ensures that saveAccount() saves an account
	 * to our database and saves the correct information
	 */
	@Test
	public void saveAccount() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		 
		int initialAccountsAmount = accountRepo.getAllAccounts().size();
		
		try {
			accountRepo.saveAccount(account);
			
			List<Account> currentAccounts = accountRepo.getAllAccounts();
			Account savedAccount = accountRepo.findAccountByUserName(account.getUserName());
			
			assertEquals(initialAccountsAmount + 1, currentAccounts.size());
			assertEquals("testuser", savedAccount.getUserName());
			assertEquals("password", savedAccount.getPassword());
			assertEquals("test@test.com", savedAccount.getEmail());
			assertEquals(AccountType.STUDENT, savedAccount.getAccountType());
			assertEquals("testStudentId", savedAccount.getRegisteredUser().getIDNumber());
		}
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	
	
	/*
	 * this test ensures that saving a null account
	 * does not add anything to the account database
	 */
	@Test
	public void saveNullAccount() {
		
		AccountRepository accountRepo = new AccountRepository();
		Account account = null;
		
		int initialAccountsAmount = accountRepo.getAllAccounts().size();
		
		accountRepo.saveAccount(account);
		
		assertEquals(initialAccountsAmount, accountRepo.getAllAccounts().size());
	}
	
	
	
	
	/*
	 * this test ensures that updateAccount()
	 * properly updates an account in the database
	 */
	@Test
	public void updateAccount() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		try {
			accountRepo.saveAccount(account);
			
			Student updatedStudent = new Student("updatedStudentId");
			account = new Account("testuser", "updatedPassword", "updated@test.com", AccountType.STUDENT, updatedStudent);
			
			accountRepo.updateAccount(account);
			
			Account updatedAccount = accountRepo.findAccountByUserName(account.getUserName());
			
			assertEquals("testuser", updatedAccount.getUserName());
			assertEquals("updatedPassword", updatedAccount.getPassword());
			assertEquals("updated@test.com", updatedAccount.getEmail());
			assertEquals(AccountType.STUDENT, updatedAccount.getAccountType());
			assertEquals("updatedStudentId", updatedAccount.getRegisteredUser().getIDNumber());
		}
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	
	/*
	 * this test ensures that deleteUser() correctly
	 * deletes the proper account from our database
	 */
	@Test
	public void deleteAccount() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		int initialAccountsAmount = accountRepo.getAllAccounts().size();
		
		accountRepo.saveAccount(account);
		accountRepo.deleteUser(account.getUserName());
		
		List<Account> currentAccounts = accountRepo.getAllAccounts();
		
		assertEquals(initialAccountsAmount, currentAccounts.size());
		assertEquals(null, accountRepo.findAccountByUserName(account.getUserName()));
	}
	
	
	/*
	 * this test ensures that findAccountByUserName()
	 * correctly finds the proper account from our database
	 */
	@Test
	public void findAccount() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		try {
			accountRepo.saveAccount(account);
			
			Account foundAccount = accountRepo.findAccountByUserName(account.getUserName());
			
			assertEquals(account.getUserName(), foundAccount.getUserName());
			assertEquals(account.getPassword(), foundAccount.getPassword());
			assertEquals(account.getEmail(), foundAccount.getEmail());
			assertEquals(account.getAccountType(), foundAccount.getAccountType());
			assertEquals(account.getRegisteredUser().getIDNumber(), foundAccount.getRegisteredUser().getIDNumber());
		}
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * this test ensures that findAccountByUserName()
	 * correctly returns null when the username DNE
	 */
	@Test
	public void findDNEAccount() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		assertEquals(null, accountRepo.findAccountByUserName("!dawifma0292@#!"));
	}
	
	
	
	/*
	 * this test ensures that getAllAccounts() correctly
	 * returns a list of all accounts from our database
	 */
	@Test
	public void getAllAccounts() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		int initialAccountsAmount = accountRepo.getAllAccounts().size();
		
		try {
			accountRepo.saveAccount(account);
			
			assertEquals(initialAccountsAmount + 1, accountRepo.getAllAccounts().size());
		}
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * this test ensures that emailExists() returns true
	 * when an account has the provided email
	 */
	@Test
	public void emailExists() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		try {
			accountRepo.saveAccount(account);
			
			assertEquals(true, accountRepo.emailExists("test@test.com"));
		}
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}
	
	
	/*
	 * this test ensures that emailExists() returns false
	 * when the provided email does not exist
	 */
	@Test
	public void emailDNE() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		assertEquals(false, accountRepo.emailExists("emaildne@test.com"));
	}
	
	
	
	/*
	 * this test ensures that usernameExists() returns true
	 * when an account has the provided username
	 */
	@Test
	public void usernameExists() {
		
		AccountRepository accountRepo = new AccountRepository();
		
		Student student = new Student("testStudentId");
		Account account = new Account("testuser", "password", "test@test.com", AccountType.STUDENT, student);
		
		try {
			accountRepo.saveAccount(account);
			
			assertEquals(true, accountRepo.usernameExists("testuser"));
		}
		
		finally {
			accountRepo.deleteUser(account.getUserName());
		}
	}


}
