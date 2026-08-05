package roombooking.AIassistant;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import roombooking.model.IDBadgeScanner;


public class IDBadgeScannerTest {


    private IDBadgeScanner scanner;



    @Before
    public void setUp() {

        scanner = new IDBadgeScanner(101);

    }



    @Test
    public void testScannerIDConstructor() {


        assertEquals(
                101,
                scanner.getScannerID()
        );

    }



    @Test
    public void testScanBadgeReturnsScannedID() {


        String result =
                scanner.scanBadge("STUDENT001");


        assertEquals(
                "STUDENT001",
                result
        );

    }



    @Test
    public void testLastScannedIDIsStored() {


        scanner.scanBadge("STUDENT001");


        assertEquals(
                "STUDENT001",
                scanner.getLastScannedID()
        );

    }




    @Test
    public void testMultipleBadgeScansUpdateLastID() {


        scanner.scanBadge("STUDENT001");

        scanner.scanBadge("STAFF002");


        assertEquals(
                "STAFF002",
                scanner.getLastScannedID()
        );

    }




    @Test
    public void testScanNullBadge() {


        String result =
                scanner.scanBadge(null);


        assertNull(result);


        assertNull(
                scanner.getLastScannedID()
        );

    }




    @Test
    public void testDifferentScannerIDs() {


        IDBadgeScanner secondScanner =
                new IDBadgeScanner(999);


        assertEquals(
                999,
                secondScanner.getScannerID()
        );

    }

}