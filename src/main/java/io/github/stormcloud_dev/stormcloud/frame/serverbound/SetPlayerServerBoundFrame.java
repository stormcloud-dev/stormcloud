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

import java.nio.ByteOrder;

public class SetPlayerServerBoundFrame extends ServerBoundFrame {

    private double unknown;
    private double unknown2;
    private String version;

    public SetPlayerServerBoundFrame(double unknown, double unknown2, String version) {
        super((byte) 2);
        this.unknown = unknown;
        this.unknown2 = unknown2;
        this.version = version;
    }

    @Override
    public int getLength() {
        return 40;
    }

    public double getUnknown() {
        return unknown;
    }

    public double getUnknown2() {
        return unknown2;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf); //For some reason the object index has to be 15, so we don't send the player info
        buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(15.0); //Object Index?
        buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(0.0); //Multiplayer ID?
        buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(3.0); //Don't know?
        buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(40.0); //Don't know?
        for (byte b : getVersion().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }

}
