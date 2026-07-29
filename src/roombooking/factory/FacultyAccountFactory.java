package roombooking.factory;

import roombooking.model.Faculty;
import roombooking.repository.AccountRepository;
import roombooking.enums.AccountType;
import roombooking.model.Account;

public class FacultyAccountFactory extends AccountFactory {

	@Override
	public Account createAccount(String username, String email, String password, String idNumber) {
		Faculty faculty = new Faculty(idNumber);
		Account account = new Account(username, password, email, AccountType.FACULTY, faculty);
		
		AccountRepository accountRepo = new AccountRepository();
		accountRepo.saveAccount(account);
		
		return account;
	}
	
}
