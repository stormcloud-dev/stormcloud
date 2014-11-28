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

public class SpawnDroneClientBoundFrame extends ClientBoundFrame {

    private short x;
    private short y;
    private short oId;
    private short mId;
    private short masterMId;
    private short xx;
    private short yy;

    public SpawnDroneClientBoundFrame(double objectIndex, double multiplayerId, short x, short y, short oId, short mId, short masterMId, short xx, short yy) {
        super((byte) 31, objectIndex, multiplayerId);
        this.x = x;
        this.y = y;
        this.oId = oId;
        this.mId = mId;
        this.masterMId = masterMId;
        this.xx = xx;
        this.yy = yy;
    }

    @Override
    public int getLength() {
        return 31;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public short getOId() {
        return oId;
    }

    public short getMId() {
        return mId;
    }

    public short getMasterMId() {
        return masterMId;
    }

    public short getXx() {
        return xx;
    }

    public short getYy() {
        return yy;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeShort(getX());
        buf.writeShort(getY());
        buf.writeShort(getOId());
        buf.writeShort(getMId());
        buf.writeShort(getMasterMId());
        buf.writeShort(getXx());
        buf.writeShort(getYy());
    }
}
