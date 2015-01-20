/*
 *   Copyright 2014 StormCloud Development Group
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

import io.github.stormcloud_dev.stormcloud.CrewMember;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.ClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.room.Room;
import io.netty.channel.Channel;

import static java.lang.Math.max;
import static java.lang.System.currentTimeMillis;

public class Player extends StormCloudObject {

    private Channel channel;

    private double mId, objectIndex;

    private String name;
    private String login;
    private CrewMember clazz;
    private long zCooldown;
    private long xCooldown;
    private long cCooldown;
    private long vCooldown;
    private long useItemCooldown;
    private long lastPing;
    private boolean ready;

    private Room room;

    public Player(double mId, double objectIndex, String login) {
        super(0, 0);
        this.mId = mId;
        this.objectIndex = objectIndex;
        this.name = "Player";
        this.login = login;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void sendFrame(ClientBoundFrame frame) {
        getChannel().writeAndFlush(frame);
    }

    public double getMId() {
        return mId;
    }

    public void setMId(double mId) {
        this.mId = mId;
    }

    public double getObjectIndex() {
        return objectIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getClazz() { //We have to return an int to match the frame (and we can return -1)
        return (clazz != null?clazz.getId():-1);
    }

    public void setClazz(CrewMember clazz) {
        this.clazz = clazz;
    }

    public void setClazz(int clazz) {
        this.clazz = (clazz != -1?CrewMember.values()[clazz]:null);
    }

    public long getZCooldown() {
        return max(zCooldown - currentTimeMillis(), 0);
    }

    public void setZCooldown(long millis) {
        zCooldown = currentTimeMillis() + millis;
    }

    public long getXCooldown() {
        return max(xCooldown - currentTimeMillis(), 0);
    }

    public void setXCooldown(long millis) {
        xCooldown = currentTimeMillis() + millis;
    }

    public long getCCooldown() {
        return max(cCooldown - currentTimeMillis(), 0);
    }

    public void setCCooldown(long millis) {
        cCooldown = currentTimeMillis() + millis;
    }

    public long getVCooldown() {
        return max(vCooldown - currentTimeMillis(), 0);
    }

    public void setVCooldown(long millis) {
        vCooldown = currentTimeMillis() + millis;
    }

    public long getUseItemCooldown() {
        return max(useItemCooldown - currentTimeMillis(), 0);
    }

    public void setUseItemCooldown(long millis) {
        useItemCooldown = currentTimeMillis() + millis;
    }

    public long getPing() {
        return max(currentTimeMillis() - lastPing, 0);
    }

    public void setPing(long millis) {
        lastPing = currentTimeMillis() + millis;
    }

    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        getRoom().removeObject(this);
        this.room = room;
        getRoom().addObject(this);
    }

}
