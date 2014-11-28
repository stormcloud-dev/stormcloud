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

public class DebuffsItemProcFrame extends ItemProcFrame {

    private double objectId;
    private short mId;
    private short buffType;
    private short duration;

    public DebuffsItemProcFrame(double objectId, short mId, short buffType, short duration) {
        super((short) 3);
        this.objectId = objectId;
        this.mId = mId;
        this.buffType = buffType;
        this.duration = duration;
    }

    @Override
    public int getLength() {
        return 0;
    }

    public double getObjectId() {
        return objectId;
    }

    public short getmId() {
        return mId;
    }

    public short getBuffType() {
        return buffType;
    }

    public short getDuration() {
        return duration;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getObjectId());
        buf.writeShort(getmId());
        buf.writeShort(getBuffType());
        buf.writeShort(getDuration());
    }

}
