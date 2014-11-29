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

public class NPCTargetClientBoundFrame extends ClientBoundFrame {

    private double x;
    private double y;
    private int targetParentMId;
    private short direction;
    private byte forceSpeedUp;

    public NPCTargetClientBoundFrame(double objectIndex, double multiplayerId, double x, double y, int targetParentMId, short direction, byte forceSpeedUp) {
        super((byte) 10, objectIndex, multiplayerId);
        this.x = x;
        this.y = y;
        this.targetParentMId = targetParentMId;
        this.direction = direction;
        this.forceSpeedUp = forceSpeedUp;
    }

    @Override
    public int getLength() {
        return 40;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTargetParentMId() {
        return targetParentMId;
    }

    public short getDirection() {
        return direction;
    }

    public byte getForceSpeedUp() {
        return forceSpeedUp;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeInt(getTargetParentMId());
        buf.writeShort(getDirection());
        buf.writeByte(getForceSpeedUp());
    }

}
