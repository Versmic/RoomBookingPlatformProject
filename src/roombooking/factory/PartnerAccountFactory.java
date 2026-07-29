package roombooking.factory;

import roombooking.enums.AccountType;
import roombooking.model.Account;
import roombooking.model.Partner;
import roombooking.repository.AccountRepository;

public class PartnerAccountFactory extends AccountFactory {

	@Override
	public Account createAccount(String username, String email, String password, String idNumber) {
		Partner partner = new Partner(idNumber);
		Account account = new Account(username, password, email, AccountType.PARTNER, partner);
		
		AccountRepository accountRepo = new AccountRepository();
		accountRepo.saveAccount(account);
		
		return account;
	}

}
