package roombooking.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SingletonCSVDatabaseManager {

    // stores the single database manager instance
    private static SingletonCSVDatabaseManager instance;

    // prevent other classes from creating new instances
    private SingletonCSVDatabaseManager() {
    }

    // returns the shared database manager instance
    public static synchronized SingletonCSVDatabaseManager getInstance() {
        if (instance == null) {
            instance = new SingletonCSVDatabaseManager();
        }
        return instance;
    }

    // reads a csv file and returns each row as a string array
    public ArrayList<String[]> readCSV(String fileName) {
        // stores all rows read from the file
        ArrayList<String[]> rows = new ArrayList<>();

        // create path to the csv file
        Path path = Paths.get(fileName);

        // return empty list if the file dne
        if (!Files.exists(path)) {
            return rows;
        }

        try {
            // reads every line from the csv file
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(path);

            // converts each line into a string array
            for (String line : lines) {
                // skips empty lines
                if (line.isEmpty()) {
                    continue;
                }

                // splits the line using commas and keeps empty values
                rows.add(line.split(",", -1));
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to read csv " + fileName, e);
        }

        return rows;
    }

    // writes all rows into a csv file
    public void writeCSV(String fileName, List<String[]> rows) {
        // creates a path to the csv file
        Path path = Paths.get(fileName);

        try {
            // stores each csv row as one line of text
            ArrayList<String> lines = new ArrayList<>();

            // joins every row using commas
            for (String[] row : rows) {
                lines.add(String.join(",", row));
            }

            // writes all lines into the file
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("failed to write csv " + fileName, e);
        }
    }

    // replaces an existing row that matches the given id value
    public void updateRow(String fileName, int idColumn, String idValue, String[] newRow) {
        // reads all current rows from the file
        ArrayList<String[]> rows = readCSV(fileName);

        // tracks whether a matching row was found
        boolean found = false;

        // searches every row for the matching id
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // checks that the column exists and the value matches
            if (idColumn < row.length && row[idColumn].equals(idValue)) {
                // replaces the old row with the new row
                rows.set(i, newRow);
                found = true;
                break;
            }
        }

        // saves the updated rows only when a match was found
        if (found) {
            writeCSV(fileName, rows);
        }
    }

    // removes every row that matches the given id value
    public void deleteRow(String fileName, int idColumn, String idValue) {
        // reads all current rows from the file
        ArrayList<String[]> rows = readCSV(fileName);

        // stores rows that should remain in the file
        ArrayList<String[]> remaining = new ArrayList<>();

        // checks each row before keeping it
        for (String[] row : rows) {
            // keeps rows that do not match the value being deleted
            if (idColumn >= row.length || !row[idColumn].equals(idValue)) {
                remaining.add(row);
            }
        }

        // writes the remaining rows back into the file
        writeCSV(fileName, remaining);
    }
}