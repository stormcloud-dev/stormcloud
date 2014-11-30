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
package io.github.stormcloud_dev.stormcloud.seralization;

import io.github.stormcloud_dev.stormcloud.frame.Frame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RORObjectEncoder extends MessageToByteEncoder<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buf) throws Exception {
        buf.writeBytes(new byte[] {-34, -64, -83, -34, 12, 0, 0, 0}); // GM:Studio header

        //System.out.println("ENCODING: " + frame.getClass().getSimpleName());
        try {
            frame.writeData(buf); // frame data - dependent on frame
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        //System.out.println("SEND...");
    }

}
