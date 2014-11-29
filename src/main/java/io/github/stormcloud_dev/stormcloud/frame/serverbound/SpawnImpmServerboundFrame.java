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

public class SpawnImpmServerboundFrame extends ServerBoundFrame {

    private short x;
    private short y;
    private short parentId;
    private byte unknown1;
    private byte unknown2;

    public SpawnImpmServerboundFrame(short x, short y, short parentId, byte unknown1, byte unknown2) {
        super((byte) 35);
        this.x = x;
        this.y = y;
        this.parentId = parentId;
        this.unknown1 = unknown1;
        this.unknown2 = unknown2;
    }

    @Override
    public int getLength() {
        return 25;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getParentId() {
        return parentId;
    }

    public byte getUnknown1() {
        return unknown1;
    }

    public byte getUnknown2() {
        return unknown2;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeShort(getX());
        buf.writeShort(getY());
        buf.writeShort(getParentId());
        buf.writeByte(getUnknown1());
        buf.writeByte(getUnknown2());
    }

}
