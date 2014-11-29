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

import io.github.stormcloud_dev.stormcloud.CrewMember;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class UpdatePlayerClientBoundFrame extends ClientBoundFrame {

    private int clazz;
    private double x;
    private double y;
    private String name;

    public UpdatePlayerClientBoundFrame(double objectIndex, double multiplayerId, int clazz, double x, double y, String name) {
        super((byte) 5, objectIndex, multiplayerId);
        this.clazz = clazz;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public UpdatePlayerClientBoundFrame(double objectIndex, double multiplayerId, CrewMember clazz, double x, double y, String name) {
        super((byte) 5, objectIndex, multiplayerId);
        this.clazz = clazz.getId();
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public int getLength() {
        return 38 + getName().getBytes().length;
    }

    public int getClazz() {
        return clazz;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        buf.writeInt(getClazz());
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        for (byte b : getName().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }
}
