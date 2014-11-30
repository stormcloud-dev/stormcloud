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
import io.netty.channel.ChannelHandlerContext;

public class CreateSlimeServerBoundFrame extends ServerBoundFrame {

    private short x;
    private short y;
    private float size;
    private short maxHP;
    private short damage;
    private short expWorth;
    private byte elite;
    private short eliteTier2;
    private double imageBlend;

    public CreateSlimeServerBoundFrame(short x, short y, float size, short maxHP, short damage, short expWorth, byte elite, short eliteTier2, double imageBlend) {
        super((byte) 44);
        this.x = x;
        this.y = y;
        this.size = size;
        this.maxHP = maxHP;
        this.damage = damage;
        this.expWorth = expWorth;
        this.elite = elite;
        this.eliteTier2 = eliteTier2;
        this.imageBlend = imageBlend;
    }

    @Override
    public int getLength() {
        return 42;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public float getSize() {
        return size;
    }

    public short getMaxHP() {
        return maxHP;
    }

    public short getDamage() {
        return damage;
    }

    public short getExpWorth() {
        return expWorth;
    }

    public byte getElite() {
        return elite;
    }

    public short getEliteTier2() {
        return eliteTier2;
    }

    public double getImageBlend() {
        return imageBlend;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeShort(getX());
        buf.writeShort(getY());
        buf.writeFloat(getSize());
        buf.writeShort(getMaxHP());
        buf.writeShort(getDamage());
        buf.writeShort(getExpWorth());
        buf.writeByte(getElite());
        buf.writeShort(getEliteTier2());
        buf.writeDouble(getImageBlend());
    }

}
