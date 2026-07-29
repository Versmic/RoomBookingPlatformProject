package roombooking.repository;

import java.util.ArrayList;

import roombooking.model.Account;
import roombooking.model.Admin;
import roombooking.model.ChiefEventCoordinator;
import roombooking.model.Faculty;
import roombooking.model.Partner;
import roombooking.model.RegisteredUser;
import roombooking.model.Staff;
import roombooking.model.Student;
import roombooking.enums.AccountType;

public class AccountRepository {

    private static final String FILE_NAME = "src/roombooking/database/accounts.csv";
    private static final int USERNAME_COLUMN = 0;
    private static final int PASSWORD_COLUMN = 1;
    private static final int EMAIL_COLUMN = 2;
    private static final int ACCOUNT_TYPE_COLUMN = 3;
    private static final int ID_COLUMN = 4;
    

    private static final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    public void saveAccount(Account account) {
        ArrayList<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(account));
        db.writeCSV(FILE_NAME, rows);
    }

    public void updateAccount(Account account) {
        db.updateRow(FILE_NAME, USERNAME_COLUMN, String.valueOf(account.getUserName()), toRow(account));
    }

    public void deleteUser(String userName) {
        db.deleteRow(FILE_NAME, USERNAME_COLUMN, String.valueOf(userName));
    }

    public Account findAccountByUserName(String userName) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > USERNAME_COLUMN && row[USERNAME_COLUMN].equals(String.valueOf(userName))) {
                return toAccount(row);
            }
        }
        return null;
    }
    
  
    public boolean login(String username, String password) {
        String enteredUsername = username.trim();

        for (String[] row : db.readCSV(FILE_NAME)) {
            // Make sure both username and password columns exist
            if (row.length <= PASSWORD_COLUMN) {
                continue;
            }

            String savedUsername = row[USERNAME_COLUMN].trim();
            String savedPassword = row[PASSWORD_COLUMN];
            if (savedUsername.equalsIgnoreCase(enteredUsername)
                    && savedPassword.equals(password)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            accounts.add(toAccount(row));
        }
        return accounts;
    }

	public boolean emailExists(String email) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > EMAIL_COLUMN && row[EMAIL_COLUMN].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private String[] toRow(Account account) {
        return new String[] {
                String.valueOf(account.getUserName()),
                account.getPassword(),
                account.getEmail(),
                account.getAccountType().toString(),
                account.getRegisteredUser().getIDNumber()
        };
    }
    

    private Account toAccount(String[] row) {
    	// userName, password, email, accountType, IDnumber
    	AccountType accountType = AccountType.valueOf(row[3].trim().toUpperCase());
    	RegisteredUser user;
    	switch (accountType) {
        case FACULTY:
            user = new Faculty(row[4]);
            break;
        case PARTNER:
            user = new Partner(row[4]);
            break;
        case STAFF:
            user = new Staff(row[4]);
            break;
        case STUDENT:
            user = new Student(row[4]);
            break;
        case ADMIN:
            user = new Admin(row[4]);
            break;
        case CHEIF:
        	user = new ChiefEventCoordinator(row[4]);
            break;
        default:
            throw new IllegalArgumentException("Unknown account type: " + accountType);
    }
    	
		return new Account(row[0], row[1], row[2], accountType, user);
	}

    public String generateNextAdminId() {
        int highest = 0;
 
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length <= ID_COLUMN) {
                continue;
            }
            if (!row[ACCOUNT_TYPE_COLUMN].trim().equalsIgnoreCase(AccountType.ADMIN.name())) {
                continue;
            }
            try {
                int idValue = Integer.parseInt(row[ID_COLUMN].trim());
                if (idValue > highest) {
                    highest = idValue;
                }
            } catch (NumberFormatException ignored) {
                // any admin row with a non-numeric ID is skipped rather than blowing up the whole scan
            }
        }
 
        return String.valueOf(highest + 1);
    }
    
    public boolean usernameExists(String username) {
    
        String enteredUsername = username.trim();

        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > USERNAME_COLUMN && row[USERNAME_COLUMN].trim().equalsIgnoreCase(enteredUsername)) {
                return true;
            }
        }

        return false;
    }

	public boolean idNumberExists(String idNumber) {
	    for (Account account : getAllAccounts()) {
	        if (account.getRegisteredUser().getIDNumber().equalsIgnoreCase(idNumber)) return true;
	    }

	    return false;
	}
}