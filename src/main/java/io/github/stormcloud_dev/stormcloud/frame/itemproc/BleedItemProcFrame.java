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

public class BleedItemProcFrame extends ItemProcFrame {

    private double targetIndex;
    private short targetMId;
    private short damage;

    public BleedItemProcFrame(double targetIndex, short targetMId, short damage) {
        super((short) 8);
        this.targetIndex = targetIndex;
        this.targetMId = targetMId;
        this.damage = damage;
    }

    @Override
    public int getLength() {
        return 15;
    }

    public double getTargetIndex() {
        return targetIndex;
    }

    public short getTargetMId() {
        return targetMId;
    }

    public short getDamage() {
        return damage;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeDouble(getTargetIndex());
        buf.writeShort(getTargetMId());
        buf.writeShort(getDamage());
    }
}
