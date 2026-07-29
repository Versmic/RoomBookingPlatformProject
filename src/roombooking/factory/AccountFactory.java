package roombooking.factory;

import roombooking.model.Account;
import roombooking.model.RegisteredUser;
import roombooking.repository.AccountRepository;

public abstract class AccountFactory {
	
	public abstract Account createAccount(String username, String email, String password, String idNumber);
	
	public boolean CheckForStrongPassword(String password) {
		
		boolean symbol = password.matches(".*[^a-zA-Z0-9].*");
        boolean capital = password.matches(".*[A-Z].*");
        boolean lowercase = password.matches(".*[a-z].*");
        boolean number = password.matches(".*[0-9].*");
        boolean length = password.length() >= 8;
        
        return (symbol && capital && length && lowercase && number);
	}
	
	public boolean CheckForValidEmail(String email) {
		return (email.contains("@") && email.contains(".") && !email.contains(" "));
	}
	
	public boolean CheckForUniqueEmail(String email) {
		AccountRepository accountRepo = new AccountRepository();
		return (accountRepo.emailExists(email));
	}
	
	public boolean CheckForUniqueUsername(String username) {
		AccountRepository accountRepo = new AccountRepository();
		return accountRepo.usernameExists(username);
	}

	public boolean checkForUniqueID(String idNumber) {
		AccountRepository accountRepo = new AccountRepository();
		return accountRepo.idNumberExists(idNumber);
	}
}
