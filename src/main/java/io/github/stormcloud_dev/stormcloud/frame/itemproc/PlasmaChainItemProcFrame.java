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

public class PlasmaChainItemProcFrame extends ItemProcFrame {

    private short mId;
    private double objectIndex;
    private short targetMId;
    private short damageCoefficient;

    public PlasmaChainItemProcFrame(short mId, double objectIndex, short targetMId, short damageCoefficient) {
        super((short) 6);
        this.mId = mId;
        this.objectIndex = objectIndex;
        this.targetMId = targetMId;
        this.damageCoefficient = damageCoefficient;
    }

    @Override
    public int getLength() {
        return 17;
    }

    public short getMId() {
        return mId;
    }

    public double getObjectIndex() {
        return objectIndex;
    }

    public short getTargetMId() {
        return targetMId;
    }

    public short getDamageCoefficient() {
        return damageCoefficient;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeShort(getMId());
        buf.writeDouble(getObjectIndex());
        buf.writeShort(getTargetMId());
        buf.writeShort(getDamageCoefficient());
    }

}
