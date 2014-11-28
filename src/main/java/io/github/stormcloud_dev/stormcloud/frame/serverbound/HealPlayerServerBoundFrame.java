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

public class HealPlayerServerBoundFrame extends ServerBoundFrame {

    private double unknown1;
    private double unknown2;
    private double unknown3;
    private double unknown4;

    public HealPlayerServerBoundFrame(double unknown1, double unknown2, double unknown3, double unknown4) {
        super((byte) 7);
        this.unknown1 = unknown1;
        this.unknown2 = unknown2;
        this.unknown3 = unknown3;
        this.unknown4 = unknown4;
    }

    @Override
    public int getLength() {
        return 49;
    }

    public double getUnknown1() {
        return unknown1;
    }

    public double getUnknown2() {
        return unknown2;
    }

    public double getUnknown3() {
        return unknown3;
    }

    public double getUnknown4() {
        return unknown4;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getUnknown1());
        buf.writeDouble(getUnknown2());
        buf.writeDouble(getUnknown3());
        buf.writeDouble(getUnknown4());
    }

}
