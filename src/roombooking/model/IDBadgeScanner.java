package roombooking.model;

public class IDBadgeScanner {

    private int scannerID;
    private String lastScannedID;

    public IDBadgeScanner(int scannerID) {
        this.scannerID = scannerID;
    }

    public String scanBadge(String scannedID) {
        lastScannedID = scannedID;
        return lastScannedID;
    }

    public int getScannerID() {
        return scannerID;
    }

    public String getLastScannedID() {
        return lastScannedID;
    }
}