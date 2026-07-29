package roombooking.factory;

import roombooking.enums.AccountType;
import roombooking.model.Account;
import roombooking.model.Staff;
import roombooking.repository.AccountRepository;

public class StaffAccountFactory extends AccountFactory {

	@Override
	public Account createAccount(String username, String password, String email, String idNumber) {
		Staff staff = new Staff(idNumber);
		Account account = new Account(username, password, email, AccountType.STAFF, staff);
		
		AccountRepository accountRepo = new AccountRepository();
		accountRepo.saveAccount(account);
		
		return account;
	}

}
