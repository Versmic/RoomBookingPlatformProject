package roombooking.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import roombooking.model.IDBadgeScanner;

public class IDBadgeScannerTester {
	
	/*
	 * test if the scanner stores the correct scanner id
	 */
	@Test
	public void testScannerID() {
		IDBadgeScanner scanner = new IDBadgeScanner(1);
		assertEquals(1, scanner.getScannerID());
	}
	
	/*
	 * test if the scanner returns the scanned badge id
	 */
	@Test
	public void testScanBadge() {
		IDBadgeScanner scanner = new IDBadgeScanner(1);
		assertEquals("123456789", scanner.scanBadge("123456789"));
	}
	
	/*
	 * test if the scanner stores the last scanned badge id
	 */
	@Test
	public void testLastScannedID() {
		IDBadgeScanner scanner = new IDBadgeScanner(1);
		scanner.scanBadge("123456789");
		assertEquals("123456789", scanner.getLastScannedID());
	}
	
	/*
	 * test if the last scanned id is null before scanning
	 */
	@Test
	public void testNoBadgeScanned() {
		IDBadgeScanner scanner = new IDBadgeScanner(1);
		assertEquals(null, scanner.getLastScannedID());
	}
}