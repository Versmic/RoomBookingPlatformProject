package roombooking.repository;

import java.util.ArrayList;

import roombooking.model.Account;
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

    private static final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    public static void saveAccount(Account account) {
        ArrayList<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(account));
        db.writeCSV(FILE_NAME, rows);
    }

    public static void updateAccount(Account account) {
        db.updateRow(FILE_NAME, USERNAME_COLUMN, String.valueOf(account.getUserName()), toRow(account));
    }

    public static void deleteUser(String userName) {
        db.deleteRow(FILE_NAME, USERNAME_COLUMN, String.valueOf(userName));
    }

    public static Account findAccountByUserName(String userName) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > USERNAME_COLUMN && row[USERNAME_COLUMN].equals(String.valueOf(userName))) {
                return toAccount(row);
            }
        }
        return null;
    }
    
  
    public static boolean login(String username, String password) {
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

    public static ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            accounts.add(toAccount(row));
        }
        return accounts;
    }

	public static boolean emailExists(String email) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > EMAIL_COLUMN && row[EMAIL_COLUMN].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private static String[] toRow(Account account) {
        return new String[] {
                String.valueOf(account.getUserName()),
                account.getEmail(),
                account.getRegisteredUser().getIDNumber()
        };
    }
    
    private static Account toAccount(String[] row) {
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
        default:
            throw new IllegalArgumentException("Unknown account type: " + accountType);
    }
    	
		return new Account(row[0], row[1], row[2], accountType, user);
	}
}