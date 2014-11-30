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

public class Enemy {

    private double MId, objectIndex;

    private short hp;
    private double PosX;
    private double PosY;

    public Enemy(double MId, double objectIndex, double PosX, double PosY) {
        this.MId = MId;
        this.objectIndex = objectIndex;
        this.PosX = PosX;
        this.PosY = PosY;
    }

    public double getMId() {
        return MId;
    }

    public double getObjectIndex() {
        return objectIndex;
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

    public short getHp() {
        return hp;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

}
