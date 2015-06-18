/*
 * Copyright 2014 StormCloud Development Group
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.stormcloud_dev.stormcloud.object;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.alarm.AlarmManager;
import io.github.stormcloud_dev.stormcloud.event.EventHandler;
import io.github.stormcloud_dev.stormcloud.event.InvalidEventHandlerException;
import io.github.stormcloud_dev.stormcloud.event.game.StepEvent;
import io.github.stormcloud_dev.stormcloud.room.Room;

import java.awt.*;

import static java.util.logging.Level.SEVERE;

public class StormCloudObject {

    private StormCloud server;
    private Room room;
    private int x;
    private int y;
    private String name;
    private boolean locked;
    private String code;
    private double scaleX;
    private double scaleY;
    private Color colour;
    private double rotation;
    private boolean existent; // Whether the instance exists in a room
    private AlarmManager alarmManager;

    public StormCloudObject(StormCloud server, int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        this.server = server;
        this.x = x;
        this.y = y;
        this.name = name;
        this.locked = locked;
        this.code = code;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.colour = new Color((int) ((colour & 16711680) >> 16 | (colour & 65280) | (colour & 255) << 16));
        this.rotation = rotation;
        alarmManager = new AlarmManager(server);
        registerEvents();
    }

    public StormCloudObject(StormCloud server, int x, int y) {
        this(server, x, y, "", false, "", 1D, 1D, 4294967295L, 0D);
    }

    private void registerEvents() {
        try {
            getServer().getEventManager().addListener(this);
        } catch (InvalidEventHandlerException exception) {
            getServer().getLogger().log(SEVERE, "Failed to register events for object", exception);
        }
    }

    @EventHandler
    public void onStep(StepEvent event) {
        getAlarmManager().onStep();
    }

    public StormCloud getServer() {
        return server;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if (getRoom() != null) getRoom().removeObject(this);
        this.room = room;
        if (getRoom() != null) getRoom().addObject(this);
        setExistent(getRoom() != null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public Color getColour() {
        return colour;
    }

    public int getGMColour() {
        int rgb = getColour().getRGB();
        return (rgb & 16711680) >> 16 | (rgb & 65280) | (rgb & 255) << 16;
    }

    public void setGMColour(int gmColour) {
        this.colour = new Color((gmColour & 16711680) >> 16 | (gmColour & 65280) | (gmColour & 255) << 16);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean isExistent() {
        return existent;
    }

    public void setExistent(boolean existent) {
        this.existent = existent;
    }

    public AlarmManager getAlarmManager() {
        return alarmManager;
    }

    public void destroy() {
        setRoom(null);
    }

}
