package roombooking.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SingletonCSVDatabaseManager {

    private static SingletonCSVDatabaseManager instance;

    private SingletonCSVDatabaseManager() {
    }

    public static synchronized SingletonCSVDatabaseManager getInstance() {
        if (instance == null) {
            instance = new SingletonCSVDatabaseManager();
        }
        return instance;
    }

    public ArrayList<String[]> readCSV(String fileName) {
        ArrayList<String[]> rows = new ArrayList<>();
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
        	System.out.println("a");
            return rows;
        }
        try {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(path);
            for (String line : lines) {
                if (line.isEmpty()) {
                    continue;
                }
                rows.add(line.split(",", -1));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV: " + fileName, e);
        }
        return rows;
    }

    public void writeCSV(String fileName, ArrayList<String[]> rows) {
        Path path = Paths.get(fileName);
        try {
            ArrayList<String> lines = new ArrayList<>();
            for (String[] row : rows) {
                lines.add(String.join(",", row));
            }
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV: " + fileName, e);
        }
    }

    public void updateRow(String fileName, int idColumn, String idValue, String[] newRow) {
        ArrayList<String[]> rows = readCSV(fileName);
        boolean found = false;
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (idColumn < row.length && row[idColumn].equals(idValue)) {
                rows.set(i, newRow);
                found = true;
                break;
            }
        }
        if (found) {
            writeCSV(fileName, rows);
        }
    }

    public void deleteRow(String fileName, int idColumn, String idValue) {
        ArrayList<String[]> rows = readCSV(fileName);
        ArrayList<String[]> remaining = new ArrayList<>();
        for (String[] row : rows) {
            if (idColumn >= row.length || !row[idColumn].equals(idValue)) {
                remaining.add(row);
            }
        }
        writeCSV(fileName, remaining);
    }
}