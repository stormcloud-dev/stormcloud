package io.github.stormcloud_dev.stormcloud;

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

import static java.lang.Math.max;
import static java.lang.System.currentTimeMillis;

public class Player {

    private double MId, objectIndex;

    private String name;
    private String login;
    private CrewMember clazz;
    private long zCooldown;
    private long xCooldown;
    private long cCooldown;
    private long vCooldown;
    private long lastPing;
    private double PosX;
    private double PosY;
    private boolean ready;

    public Player(double MId, double objectIndex, String login) {
        this.MId = MId;
        this.objectIndex = objectIndex;
        this.name = "Player";
        this.login = login;
        this.PosX = 0.0;
        this.PosY = 0.0;
    }

    public double getMId() {
        return MId;
    }

    public void setMId(double MId) {
        this.MId = MId;
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

    public double getPosY() {
        return PosY;
    }

    public void setPosY(double PosY) {
        this.PosY = PosY;
    }

    public double getPosX() {
        return PosX;
    }

    public void setPosX(double PosX) {
        this.PosX = PosX;
    }


}
