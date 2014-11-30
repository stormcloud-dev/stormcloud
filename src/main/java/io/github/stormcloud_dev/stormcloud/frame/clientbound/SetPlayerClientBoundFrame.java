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

    private double playerId;
    private double roomId;
    private String version;

    public SetPlayerClientBoundFrame(double objectIndex, double multiplayerId, double playerId, double roomId, String version) {
        super((byte) 2, objectIndex, multiplayerId);
        this.playerId = playerId;
        this.roomId = roomId;
        this.version = version;
    }

    @Override
    public int getLength() {
        return 40;
    }

    public double getPlayerId() {
        return playerId;
    }

    public double getRoomId() {
        return roomId;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);

        buf.writeDouble(getPlayerId());
        buf.writeDouble(getRoomId());
        for (byte b : getVersion().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }

}
