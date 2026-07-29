package roombooking.test;

import static org.junit.Assert.assertEquals;
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
			account = factory.createAccount("testfaculty", "password", "faculty@test.com", "testF001");
			
			// test if account object has correct parameters and is of type faculty
			assertEquals("testfaculty", account.getUserName());
			assertEquals("password", account.getPassword());
			assertEquals("faculty@test.com", account.getEmail());
			assertEquals(AccountType.FACULTY, account.getAccountType());

		} finally {
			if (account != null) {
				accountRepo.deleteUser(account.getUserName());
			}
		}
	}

}
