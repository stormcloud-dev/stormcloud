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

public class ChatSystemClientBoundFrame extends ClientBoundFrame {

    private byte isItemChat;
    private String text;

    public ChatSystemClientBoundFrame(double objectIndex, double multiplayerId, byte isItemChat, String text) {
        super((byte) 26, objectIndex, multiplayerId);
        this.isItemChat = isItemChat;
        this.text = text;
    }

    @Override
    public int getLength() {
        return 19 + getText().getBytes().length;
    }

    public byte getIsItemChat() {
        return isItemChat;
    }

    public String getText() {
        return text;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeByte(getIsItemChat());
        for (byte b : getText().getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }
}
