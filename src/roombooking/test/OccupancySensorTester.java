package roombooking.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import roombooking.model.OccupancySensor;

public class OccupancySensorTester {
	
	/*
	 * test if our sensor can detect no occupancy
	 */
	@Test
	public void testDetectNoOccupancy() {
		OccupancySensor sensor = new OccupancySensor();
		assertEquals(false, sensor.detectOccupancy());
	}
	
	/*
	 * test if our sensor can detect occupancy
	 */
	@Test
	public void testDetectOccupancy() {
		OccupancySensor sensor = new OccupancySensor();
		sensor.setOccupied(true);
		assertEquals(true, sensor.detectOccupancy());
	}
}
