package roombooking.factory;

import roombooking.enums.AccountType;
import roombooking.model.Account;
import roombooking.model.Student;
import roombooking.repository.AccountRepository;

public class StudentAccountFactory extends AccountFactory {

	@Override
	public Account createAccount(String username, String email, String password, String idNumber) {
		Student student = new Student(idNumber);
		Account account = new Account(username, password, email, AccountType.STUDENT, student);
		
		AccountRepository accountRepo = new AccountRepository();
		accountRepo.saveAccount(account);
		
		return account;
	}

}
