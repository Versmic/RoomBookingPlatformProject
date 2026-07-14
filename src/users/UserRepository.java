package users;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static final String FILE_NAME = "users.csv";
    private static final int ID_COLUMN = 0;
    private static final int EMAIL_COLUMN = 3;

    private final SingletonCSVDatabaseManager db = SingletonCSVDatabaseManager.getInstance();

    public void saveUser(RegisteredUser user) {
        List<String[]> rows = db.readCSV(FILE_NAME);
        rows.add(toRow(user));
        db.writeCSV(FILE_NAME, rows);
    }

    public void updateUser(RegisteredUser user) {
        db.updateRow(FILE_NAME, ID_COLUMN, String.valueOf(user.getUserID()), toRow(user));
    }

    public void deleteUser(int userID) {
        db.deleteRow(FILE_NAME, ID_COLUMN, String.valueOf(userID));
    }

    public RegisteredUser findUserByID(int userID) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > ID_COLUMN && row[ID_COLUMN].equals(String.valueOf(userID))) {
                return toUser(row);
            }
        }
        return null;
    }

    public List<RegisteredUser> getAllUsers() {
        List<RegisteredUser> users = new ArrayList<>();
        for (String[] row : db.readCSV(FILE_NAME)) {
            users.add(toUser(row));
        }
        return users;
    }

    public boolean emailExists(String email) {
        for (String[] row : db.readCSV(FILE_NAME)) {
            if (row.length > EMAIL_COLUMN && row[EMAIL_COLUMN].equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    private String[] toRow(RegisteredUser user) {
        return new String[] {
                String.valueOf(user.getUserID()),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getAccount().getEmail(),
                user.getClass().getSimpleName()
        };
    }

    private RegisteredUser toUser(String[] row) {
        throw new UnsupportedOperationException(
                "toUser() needs the concrete RegisteredUser subclasses before it can be implemented");
    }
}