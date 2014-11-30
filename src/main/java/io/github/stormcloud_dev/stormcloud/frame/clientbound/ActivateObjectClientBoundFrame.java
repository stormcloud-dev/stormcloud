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

public class ActivateObjectClientBoundFrame extends ClientBoundFrame {

    private double playerX;
    private double playerY;
    private short objectActivated;
    private short objectMId;
    private byte activeState;
    private byte success;

    public ActivateObjectClientBoundFrame(double objectIndex, double multiplayerId, double playerX, double playerY, short objectActivated, short objectMId, byte activeState, byte success) {
        super((byte) 19, objectIndex, multiplayerId);
        this.playerX = playerX;
        this.playerY = playerY;
        this.objectActivated = objectActivated;
        this.objectMId = objectMId;
        this.activeState = activeState;
        this.success = success;
    }

    @Override
    public int getLength() {
        return 39;
    }

    public double getPlayerX() {
        return playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public short getObjectActivated() {
        return objectActivated;
    }

    public short getObjectMId() {
        return objectMId;
    }

    public byte getActiveState() {
        return activeState;
    }

    public byte getSuccess() {
        return success;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getPlayerX());
        buf.writeDouble(getPlayerY());
        buf.writeShort(getObjectActivated());
        buf.writeShort(getObjectMId());
        buf.writeByte(getActiveState());
        buf.writeByte(getSuccess());
    }

}
