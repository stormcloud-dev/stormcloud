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

public class UpdateDiffServerBoundFrame extends ServerBoundFrame {

    private byte diffLevel;
    private byte forceItemSharing;
    private byte honorActive;
    private byte kinActive;
    private byte distortionActive;
    private byte spiteActive;
    private byte glassActive;
    private byte enigmaActive;
    private byte sacrificeActive;
    private byte commandActive;
    private byte spiritActive;
    private byte originActive;

    public UpdateDiffServerBoundFrame(byte diffLevel, byte forceItemSharing, byte honorActive, byte kinActive, byte distortionActive, byte spiteActive, byte glassActive, byte enigmaActive, byte sacrificeActive, byte commandActive, byte spiritActive, byte originActive) {
        super((byte) 39);
        this.diffLevel = diffLevel;
        this.forceItemSharing = forceItemSharing;
        this.honorActive = honorActive;
        this.kinActive = kinActive;
        this.distortionActive = distortionActive;
        this.spiteActive = spiteActive;
        this.glassActive = glassActive;
        this.enigmaActive = enigmaActive;
        this.sacrificeActive = sacrificeActive;
        this.commandActive = commandActive;
        this.spiritActive = spiritActive;
        this.originActive = originActive;
    }

    @Override
    public int getLength() {
        return 29;
    }

    public byte getDiffLevel() {
        return diffLevel;
    }

    public byte getForceItemSharing() {
        return forceItemSharing;
    }

    public byte getHonorActive() {
        return honorActive;
    }

    public byte getKinActive() {
        return kinActive;
    }

    public byte getDistortionActive() {
        return distortionActive;
    }

    public byte getSpiteActive() {
        return spiteActive;
    }

    public byte getGlassActive() {
        return glassActive;
    }

    public byte getEnigmaActive() {
        return enigmaActive;
    }

    public byte getSacrificeActive() {
        return sacrificeActive;
    }

    public byte getCommandActive() {
        return commandActive;
    }

    public byte getSpiritActive() {
        return spiritActive;
    }

    public byte getOriginActive() {
        return originActive;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        //buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(0.0); //Unknown Double
        //buf.order(ByteOrder.LITTLE_ENDIAN).writeDouble(0.0); //Uknown Double
        buf.writeByte(getDiffLevel());
        buf.writeByte(getForceItemSharing());
        buf.writeByte(getHonorActive());
        buf.writeByte(getKinActive());
        buf.writeByte(getDistortionActive());
        buf.writeByte(getSpiteActive());
        buf.writeByte(getGlassActive());
        buf.writeByte(getEnigmaActive());
        buf.writeByte(getSacrificeActive());
        buf.writeByte(getCommandActive());
        buf.writeByte(getSpiritActive());
        buf.writeByte(getOriginActive());
    }

}
