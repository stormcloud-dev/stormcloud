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
package io.github.stormcloud_dev.stormcloud.frame.clientbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class KeyPlayerClientBoundFrame extends ClientBoundFrame {

    private short x;
    private short y;
    private byte zAction;
    private byte xAction;
    private byte cAction;
    private byte vAction;
    private short itemUsed;
    private byte unknown;

    public KeyPlayerClientBoundFrame(double objectIndex, double multiplayerId, short x, short y, byte zAction, byte xAction, byte cAction, byte vAction, short itemUsed, byte unknown) {
        super((byte) 6, objectIndex, multiplayerId);
        this.x = x;
        this.y = y;
        this.zAction = zAction;
        this.xAction = xAction;
        this.cAction = cAction;
        this.vAction = vAction;
        this.itemUsed = itemUsed;
        this.unknown = unknown;
    }

    @Override
    public int getLength() {
        return 28;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public byte getZAction() {
        return zAction;
    }

    public byte getXAction() {
        return xAction;
    }

    public byte getCAction() {
        return cAction;
    }

    public byte getVAction() {
        return vAction;
    }

    public short getItemUsed() {
        return itemUsed;
    }

    public byte getUnknown() {
        return unknown;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeShort(getX());
        buf.writeShort(getY());
        buf.writeByte(getZAction());
        buf.writeByte(getXAction());
        buf.writeByte(getCAction());
        buf.writeByte(getVAction());
        buf.writeShort(getItemUsed());
        buf.writeByte(getUnknown());
    }
}
