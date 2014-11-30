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

public class SpawnItemServerBoundFrame extends ServerBoundFrame {

    private double x;
    private double y;
    private int numberDropped;

    public SpawnItemServerBoundFrame(double x, double y, int numberDropped) {
        super((byte) 20);
        this.x = x;
        this.y = y;
        this.numberDropped = numberDropped;
    }

    @Override
    public int getLength() {
        return 37;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getNumberDropped() {
        return numberDropped;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeInt(getNumberDropped());
    }

}
