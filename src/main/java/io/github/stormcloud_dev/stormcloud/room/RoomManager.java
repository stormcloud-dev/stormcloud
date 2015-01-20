package io.github.stormcloud_dev.stormcloud.room;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
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

}
