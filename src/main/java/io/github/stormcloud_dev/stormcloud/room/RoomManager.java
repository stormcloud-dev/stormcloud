package io.github.stormcloud_dev.stormcloud.room;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static io.github.stormcloud_dev.stormcloud.room.Room.load;
import static java.lang.String.format;

public class RoomManager {

    private StormCloud server;
    private Map<String, Room> rooms;

    public RoomManager(StormCloud server) {
        this.server = server;
        rooms = new ConcurrentHashMap<>();
    }

    public void addRoom(Room room) {
        rooms.put(room.getName(), room);
    }

    public void loadRooms() {
        for (String roomName : new String[] {
                "r1_1_1",
                "r1_1_2",
                "r1_1_3",
                "r1_2_1",
                "r1_2_2",
                "r1_2_3",
                "r2_1_1",
                "r2_1_2",
                "r2_2_1",
                "r2_2_2",
                "r3_1_1",
                "r3_1_2",
                "r3_2_1",
                "r3_2_2",
                "r4_1_1",
                "r4_1_2",
                "r4_2_1",
                "r4_2_2",
                "r5_1_1",
                "r5_1_2",
                "r6_1_1",
                "room40",
                "room43",
                "room48",
                "room68",
                "rPigbeach"
        }) {
            try {
                addRoom(load(roomName));
                server.getLogger().info(format("Loaded room: %s", roomName));
            } catch (ParserConfigurationException | IOException | XPathExpressionException | SAXException exception) {
                server.getLogger().log(Level.WARNING, format("Failed to load room: %s", roomName), exception);
                exception.printStackTrace();
            }
        }
    }

    public Room getRoom(String name) {
        return rooms.get(name);
    }

    public Room getRoomById(int id) {
        switch (id) {
            case 16: break; // End scene
            case 18: break;
            case 19: break;
            case 20: break;
            case 21: break;
            case 22: break;
            case 23: break;
            case 24: break;
            case 25: break;
            case 26: break;
            case 27: break;
            case 28: break;
            case 29: break;
            case 30: break;
            case 31: break;
            case 32: break;
            case 33: break;
            case 34: break;
            case 35: break;
            case 36: break;
            case 37: break;
            case 38: break;
            case 40: break; // Lobby
            case 41: break; // Boss room
        }
        return null;
    }

    public Collection<Room> getRooms() {
        return rooms.values();
    }

}
