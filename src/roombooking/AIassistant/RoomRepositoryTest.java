package roombooking.AIassistant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import roombooking.enums.RoomStatus;
import roombooking.model.Room;
import roombooking.repository.RoomRepository;

public class RoomRepositoryTest {

    private RoomRepository repository;
    private Room room;

    @BeforeEach
    public void setup() {
        repository = new RoomRepository();

        room = new Room(
                "TEST_ROOM_999",
                "Test Building",
                999,
                20,
                RoomStatus.AVAILABLE
        );

        repository.saveRoom(room);
    }


    @AfterEach
    public void cleanup() {
        repository.deleteRoom("TEST_ROOM_999");
    }


    @Test
    public void testSaveRoom() {

        Room found =
                repository.findRoomById("TEST_ROOM_999");

        assertNotNull(found);
        assertEquals(
                "Test Building",
                found.getBuildingName()
        );
    }


    @Test
    public void testFindRoomByIdNotFound() {

        Room result =
                repository.findRoomById("INVALID_ROOM");

        assertNull(result);
    }


    @Test
    public void testGetAllRoomsContainsRoom() {

        boolean exists =
                repository.getAllRooms()
                .stream()
                .anyMatch(r ->
                    r.getRoomId()
                    .equals("TEST_ROOM_999")
                );

        assertTrue(exists);
    }


    @Test
    public void testUpdateRoom() {

        Room updated =
                new Room(
                    "TEST_ROOM_999",
                    "Updated Building",
                    999,
                    50,
                    RoomStatus.MAINTENANCE
                );


        repository.updateRoom(updated);


        Room result =
                repository.findRoomById("TEST_ROOM_999");


        assertEquals(
                "Updated Building",
                result.getBuildingName()
        );

        assertEquals(
                RoomStatus.MAINTENANCE,
                result.getStatus()
        );
    }


    @Test
    public void testDeleteRoom() {

        repository.deleteRoom("TEST_ROOM_999");


        assertNull(
                repository.findRoomById("TEST_ROOM_999")
        );
    }
}