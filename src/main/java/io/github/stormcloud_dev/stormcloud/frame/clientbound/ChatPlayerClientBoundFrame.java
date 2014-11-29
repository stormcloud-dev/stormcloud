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

public class ChatPlayerClientBoundFrame extends ClientBoundFrame {

    private String text;

    public ChatPlayerClientBoundFrame(double objectIndex, double multiplayerId, String text) {
        super((byte) 4, objectIndex, multiplayerId);
        this.text = text;
    }

    @Override
    public int getLength() {
        return 18 + getText().getBytes().length;
    }

    public String getText() {
        return text;
    }

    @Override
    public void writeData(ByteBuf buf, ChannelHandlerContext ctx) {
        super.writeData(buf, ctx);
        for (byte b : getText().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }
}
