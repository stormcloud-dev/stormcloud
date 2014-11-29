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
package io.github.stormcloud_dev.stormcloud.frame;

import io.github.stormcloud_dev.stormcloud.Player;
import io.github.stormcloud_dev.stormcloud.StormCloudHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteOrder;

public abstract class Frame {

    private byte id;

    public Frame(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public abstract int getLength();

    public void writeData(ByteBuf buf) {
        frameInformation(buf);
    }

    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        frameInformation(buf);
        playerInformation(buf, ctx);
    }

    private void frameInformation(ByteBuf buf) {
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(getLength());
        buf.writeByte(getId());
    }

    private void playerInformation(ByteBuf buf, ChannelHandlerContext ctx) {

        Player player = ctx.channel().attr(StormCloudHandler.PLAYER).get();

        buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(player.getObjectIndex());
        buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(player.getMId());

    }
}
