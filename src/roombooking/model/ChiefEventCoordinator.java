package roombooking.model;

import roombooking.enums.AccountType;
import roombooking.enums.RoomStatus;
import roombooking.repository.AccountRepository;
import roombooking.repository.RoomRepository;

// the only user allowed to generate admin accounts
public class ChiefEventCoordinator extends RegisteredUser {

    private final String chiefId;
    private final AccountRepository accountRepository;

    public ChiefEventCoordinator(String chiefId) {
        this.chiefId = chiefId;
        this.accountRepository = new AccountRepository();
    }

    // validates, creates and saves a new admin account
    public Account generateAdminAccount(String username, String email, String password) {
        validateAccountInformation(username, email, password);

        String cleanedUsername = username.trim();
        String cleanedEmail = email.trim();
        String adminId = accountRepository.generateNextAdminId();

        Admin admin = new Admin(adminId);

        Account account = new Account(
                cleanedUsername,
                password,
                cleanedEmail,
                AccountType.ADMIN,
                admin
        );

        accountRepository.saveAccount(account);
        return account;
    }

    // returns the next admin id for displaying in the panel
    public String getNextAdminId() {
        return accountRepository.generateNextAdminId();
    }

    // validates all account information
    private void validateAccountInformation(String username, String email, String password) {
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Please complete every field.");
        }

        if (accountRepository.usernameExists(username)) {
            throw new IllegalArgumentException("That username is already taken.");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }

        if (accountRepository.emailExists(email.trim())) {
            throw new IllegalArgumentException("That email is already in use.");
        }

        if (!isStrongPassword(password)) {
            throw new IllegalArgumentException(
                    "Password must have 8 characters, uppercase, lowercase, number and symbol."
            );
        }
    }

    // checks basic email structure
    private boolean isValidEmail(String email) {
        return (email.contains("@") && email.contains(".") && !email.contains(" "));
    }

    // checks password requirements
    private boolean isStrongPassword(String password) {
        boolean hasSymbol = password.matches(".*[^A-Za-z0-9].*");
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean validLength = password.length() >= 8;

        return (hasSymbol && hasUppercase && hasLowercase && hasNumber && validLength);
    }

    @Override
    public double getHRate() {
        return 0;
    }

    @Override
    public String getIDNumber() {
        return chiefId;
    }
    
    public void enableRoom(Room room) {
		room.setStatus(RoomStatus.AVAILABLE);
		RoomRepository roomRepo = new RoomRepository();
		roomRepo.updateRoom(room);
	}
    
    public void disableRoom(Room room) {
    	room.setStatus(RoomStatus.DISABLED);
    	RoomRepository roomRepo = new RoomRepository();
		roomRepo.updateRoom(room);
    }
    
    public void maintainenceRoom(Room room) {
    	room.setStatus(RoomStatus.MAINTENANCE);
    	RoomRepository roomRepo = new RoomRepository();
		roomRepo.updateRoom(room);
    }
}