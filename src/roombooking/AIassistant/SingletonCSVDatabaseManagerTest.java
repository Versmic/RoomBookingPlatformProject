package roombooking.AIassistant;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.*;

import roombooking.repository.SingletonCSVDatabaseManager;


public class SingletonCSVDatabaseManagerTest {


    private SingletonCSVDatabaseManager db;


    @BeforeEach
    public void setup(){

        db =
          SingletonCSVDatabaseManager.getInstance();

    }



    @Test
    public void testSingletonInstance(){

        SingletonCSVDatabaseManager second =
                SingletonCSVDatabaseManager.getInstance();


        assertSame(
                db,
                second
        );
    }



    @Test
    public void testWriteAndReadCSV(){


        String file =
                "src/roombooking/database/test.csv";


        ArrayList<String[]> rows =
                new ArrayList<>();


        rows.add(
            new String[]{
                "1",
                "hello"
            }
        );


        db.writeCSV(file, rows);


        ArrayList<String[]> result =
                db.readCSV(file);


        assertEquals(
                1,
                result.size()
        );


        assertEquals(
                "hello",
                result.get(0)[1]
        );
    }



    @Test
    public void testReadMissingFile(){

        ArrayList<String[]> result =
                db.readCSV(
                    "does_not_exist.csv"
                );


        assertTrue(
                result.isEmpty()
        );
    }
}