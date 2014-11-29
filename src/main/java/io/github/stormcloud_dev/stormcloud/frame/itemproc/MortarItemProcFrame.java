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
import io.netty.channel.ChannelHandlerContext;

public class MortarItemProcFrame extends ItemProcFrame {

    private double x;
    private double y;
    private double damage;
    private short direction;

    public MortarItemProcFrame(double x, double y, double damage, short direction) {
        super((short) 4);
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.direction = direction;
    }

    @Override
    public int getLength() {
        return 29;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDamage() {
        return damage;
    }

    public short getDirection() {
        return direction;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeDouble(getDamage());
        buf.writeShort(getDirection());
    }

}
