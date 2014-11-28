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
package io.github.stormcloud_dev.stormcloud.frame.serverbound;

import io.netty.buffer.ByteBuf;

public class SpawnClassicBossServerBoundFrame extends ServerBoundFrame {

    private double x;
    private double y;
    private int sprite;
    private int childIndex;
    private double soundSpawn;
    private byte elite;
    private short eliteTier;
    private short pointValue;
    private short imageXScale;
    private double imageBlend;

    public SpawnClassicBossServerBoundFrame(double x, double y, int sprite, int childIndex, double soundSpawn, byte elite, short eliteTier, short pointValue, short imageXScale, double imageBlend) {
        super((byte) 23);
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.childIndex = childIndex;
        this.soundSpawn = soundSpawn;
        this.elite = elite;
        this.eliteTier = eliteTier;
        this.pointValue = pointValue;
        this.imageXScale = imageXScale;
        this.imageBlend = imageBlend;
    }

    @Override
    public int getLength() {
        return 64;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getSprite() {
        return sprite;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public double getSoundSpawn() {
        return soundSpawn;
    }

    public byte getElite() {
        return elite;
    }

    public short getEliteTier() {
        return eliteTier;
    }

    public short getPointValue() {
        return pointValue;
    }

    public short getImageXScale() {
        return imageXScale;
    }

    public double getImageBlend() {
        return imageBlend;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeInt(getSprite());
        buf.writeInt(getChildIndex());
        buf.writeDouble(getSoundSpawn());
        buf.writeByte(getElite());
        buf.writeShort(getEliteTier());
        buf.writeShort(getPointValue());
        buf.writeShort(getImageXScale());
        buf.writeDouble(getImageBlend());
    }
}
