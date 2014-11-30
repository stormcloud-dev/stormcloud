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
import io.github.stormcloud_dev.stormcloud.frame.clientbound.LagPlayerClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.TestClientBoundFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Arrays;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class RORObjectEncoder extends MessageToByteEncoder<Frame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buf) throws Exception {
        buf.writeBytes(new byte[] {-34, -64, -83, -34, 12, 0, 0, 0}); // GM:Studio header

        //System.out.println("ENCODING: " + frame.getClass().getSimpleName());
        try {
            frame.writeData(buf.order(LITTLE_ENDIAN)); // frame data - dependent on frame
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (!(frame instanceof TestClientBoundFrame) && !(frame instanceof LagPlayerClientBoundFrame)) {
            // Print packets for debugging
            int readerIndex = buf.readerIndex(), writerIndex = buf.writerIndex();
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(buf.readableBytes()).readBytes(bytes);
            buf.setIndex(readerIndex, writerIndex);
            System.out.println("SEND " + frame.getClass().getSimpleName() + " TO " + ctx.channel().remoteAddress() + " - " + Arrays.toString(bytes));
        }

    }

}
