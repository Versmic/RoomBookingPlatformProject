package roombooking.AIassistant;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import roombooking.model.OccupancySensor;


public class OccupancySensorTest {


    private OccupancySensor sensor;



    @Before
    public void setUp() {

        sensor = new OccupancySensor();

    }




    @Test
    public void testDefaultOccupancyIsFalse() {


        assertFalse(
                sensor.detectOccupancy()
        );

    }





    @Test
    public void testSetOccupiedTrue() {


        sensor.setOccupied(true);


        assertTrue(
                sensor.detectOccupancy()
        );

    }





    @Test
    public void testSetOccupiedFalse() {


        sensor.setOccupied(true);

        sensor.setOccupied(false);


        assertFalse(
                sensor.detectOccupancy()
        );

    }





    @Test
    public void testOccupancyCanChangeMultipleTimes() {


        sensor.setOccupied(true);


        assertTrue(
                sensor.detectOccupancy()
        );



        sensor.setOccupied(false);


        assertFalse(
                sensor.detectOccupancy()
        );



        sensor.setOccupied(true);


        assertTrue(
                sensor.detectOccupancy()
        );

    }

}