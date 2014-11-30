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

public class SpawnClassicServerBoundFrame extends ServerBoundFrame {

    private short cardChoice;
    private double x;
    private double y;
    private byte elite;
    private short eliteTier;
    private short pointValue;
    private short imageXScale;

    public SpawnClassicServerBoundFrame(short cardChoice, double x, double y, byte elite, short eliteTier, short pointValue, short imageXScale) {
        super((byte) 15);
        this.cardChoice = cardChoice;
        this.x = x;
        this.y = y;
        this.elite = elite;
        this.eliteTier = eliteTier;
        this.pointValue = pointValue;
        this.imageXScale = imageXScale;
    }

    @Override
    public int getLength() {
        return 42;
    }

    public short getCardChoice() {
        return cardChoice;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeShort(getCardChoice());
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeByte(getElite());
        buf.writeShort(getEliteTier());
        buf.writeShort(getPointValue());
        buf.writeShort(getImageXScale());
    }
}
