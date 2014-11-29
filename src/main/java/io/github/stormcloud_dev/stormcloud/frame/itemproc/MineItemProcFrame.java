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

public class MineItemProcFrame extends ItemProcFrame {

    private short x;
    private short y;
    private short objectIndex;
    private short damage;
    private short tertiary;

    public MineItemProcFrame(short x, short y, short objectIndex, short damage, short tertiary) {
        super((short) 11);
        this.x = x;
        this.y = y;
        this.objectIndex = objectIndex;
        this.damage = damage;
        this.tertiary = tertiary;
    }

    @Override
    public int getLength() {
        return 13;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getObjectIndex() {
        return objectIndex;
    }

    public short getDamage() {
        return damage;
    }

    public short getTertiary() {
        return tertiary;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeShort(getX());
        buf.writeShort(getY());
        buf.writeShort(getObjectIndex());
        buf.writeShort(getDamage());
        buf.writeShort(getTertiary());
    }

}
