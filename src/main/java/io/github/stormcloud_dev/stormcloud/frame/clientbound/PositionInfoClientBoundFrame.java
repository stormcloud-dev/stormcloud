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

public class PositionInfoClientBoundFrame extends ClientBoundFrame {

    private double x;
    private double y;
    private byte left;
    private byte right;
    private byte jump;
    private byte jumpHeld;
    private byte up;
    private byte down;

    public PositionInfoClientBoundFrame(double objectIndex, double multiplayerId, double x, double y, byte left, byte right, byte jump, byte jumpHeld, byte up, byte down) {
        super((byte) 0, objectIndex, multiplayerId);
        this.x = x;
        this.y = y;
        this.left = left;
        this.right = right;
        this.jump = jump;
        this.jumpHeld = jumpHeld;
        this.up = up;
        this.down = down;
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

    public byte getLeft() {
        return left;
    }

    public byte getRight() {
        return right;
    }

    public byte getJump() {
        return jump;
    }

    public byte getJumpHeld() {
        return jumpHeld;
    }

    public byte getUp() {
        return up;
    }

    public byte getDown() {
        return down;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeByte(getLeft());
        buf.writeByte(getRight());
        buf.writeByte(getJump());
        buf.writeByte(getJumpHeld());
        buf.writeByte(getUp());
        buf.writeByte(getDown());
    }

}
