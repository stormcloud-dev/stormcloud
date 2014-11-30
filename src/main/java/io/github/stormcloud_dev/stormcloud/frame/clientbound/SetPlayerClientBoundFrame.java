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

public class SetPlayerClientBoundFrame extends ClientBoundFrame {

    private double unknown1;
    private double unknown2;
    private String version;

    public SetPlayerClientBoundFrame(double objectIndex, double multiplayerId, double unknown1, double unknown2, String version) {
        super((byte) 2, objectIndex, multiplayerId);
        this.unknown1 = unknown1;
        this.unknown2 = unknown2;
        this.version = version;
    }

    @Override
    public int getLength() {
        return 40;
    }

    public double getUnknown1() {
        return unknown1;
    }

    public double getUnknown2() {
        return unknown2;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
//        buf.writeDouble(getUnknown1());
//        buf.writeDouble(getUnknown2());
//        for (byte b : getVersion().getBytes()) {
//            buf.writeByte(b);
//        }
//        buf.writeByte(0);
        buf.writeDouble(3.0); //Don't know?
        buf.writeDouble(40.0); //Don't know?
        for (byte b : getVersion().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }

}
