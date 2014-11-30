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
package io.github.stormcloud_dev.stormcloud.frame.itemproc;

import io.netty.buffer.ByteBuf;

public class HornItemProcFrame extends ItemProcFrame {

    private double x;
    private double y;
    private byte direction;
    private double damage;

    public HornItemProcFrame(short itemProcId, double x, double y, byte direction, double damage) {
        super(itemProcId);
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.damage = damage;
    }

    @Override
    public int getLength() {
        return 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public byte getDirection() {
        return direction;
    }

    public double getDamage() {
        return damage;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeByte(getDirection());
        buf.writeDouble(getDamage());
    }

}
