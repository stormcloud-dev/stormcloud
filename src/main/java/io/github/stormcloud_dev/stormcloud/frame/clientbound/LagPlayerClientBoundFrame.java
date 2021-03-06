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
import io.netty.channel.ChannelHandlerContext;

public class LagPlayerClientBoundFrame extends ClientBoundFrame {

    private String playerName;

    public LagPlayerClientBoundFrame(double objectIndex, double multiplayerId, String playerName) {
        super((byte) 16, objectIndex, multiplayerId);
        this.playerName = playerName;
    }

    @Override
    public int getLength() {
        return 18 + getPlayerName().getBytes().length;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        for (byte b : getPlayerName().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }

}
