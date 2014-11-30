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

public class KeyMonsterServerBoundFrame extends ServerBoundFrame {

    private double x;
    private double y;
    private byte zAction;
    private byte xAction;
    private byte cAction;
    private byte vAction;
    private short imageXScale;

    public KeyMonsterServerBoundFrame(double x, double y, byte zAction, byte xAction, byte cAction, byte vAction, short imageXScale) {
        super((byte) 11);
        this.x = x;
        this.y = y;
        this.zAction = zAction;
        this.xAction = xAction;
        this.cAction = cAction;
        this.vAction = vAction;
        this.imageXScale = imageXScale;
    }

    @Override
    public int getLength() {
        return 39;
    }

    public double getX() {
        return x;
    }

    public double getY() {
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

    public short getImageXScale() {
        return imageXScale;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeByte(getZAction());
        buf.writeByte(getXAction());
        buf.writeByte(getCAction());
        buf.writeByte(getVAction());
        buf.writeShort(getImageXScale());
    }
}
