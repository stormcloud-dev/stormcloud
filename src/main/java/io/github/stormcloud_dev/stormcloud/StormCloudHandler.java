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
package io.github.stormcloud_dev.stormcloud;

import io.github.stormcloud_dev.stormcloud.frame.HandshakeFrame;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.AddPlayerServerBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.SetPlayerServerBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.TestServerBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.UpdateDiffServerBoundFrame;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Random;

import static io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class StormCloudHandler extends ChannelHandlerAdapter {

    public static final AttributeKey<Player> PLAYER =
            AttributeKey.valueOf(StormCloudHandler.class, "PLAYER");

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel active");
        Random random = new Random();
        ctx.channel().attr(StormCloudHandler.PLAYER).set(new Player(random.nextInt(), random.nextInt()));

        channels.add(ctx.channel());
        ctx.writeAndFlush(Unpooled.wrappedBuffer("GM:Studio-Connect\u0000".getBytes("utf8")));

        //Thread that tests if the connection is alive (The client needs that, else it will disconnect)
        Thread testThread = new Thread(new Runnable() {
            Channel channel = ctx.channel();

            public void run() {
                while(channel.isActive()) {
                    try {
                        Thread.sleep(1000);
                        channel.writeAndFlush(new TestServerBoundFrame());
                    } catch(Exception e) {

                    }
                }
            }
        });

        testThread.start();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel inactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HandshakeFrame) {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{-83, -66, -81, -34, -21, -66, 13, -16, 12, 0, 0, 0}));
            ctx.writeAndFlush(new SetPlayerServerBoundFrame(0.0, 0.0, "v1.2.4"));
            ctx.writeAndFlush(new UpdateDiffServerBoundFrame((byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
            //ctx.writeAndFlush(new AddPlayerServerBoundFrame(0.0, 0.0, 0.0, -1, 1, "HOST|"));

            for (Channel channel : channels) {
                if(!channel.equals(ctx.channel())) {
                    Player player = ctx.channel().attr(StormCloudHandler.PLAYER).get();
                    channel.writeAndFlush(new AddPlayerServerBoundFrame(0.0, 0.0, player.getMId(), player.getClazz(), 1, player.getName()));
                }
            }

        } else {
            for (Channel channel : channels) {
                if(!channel.equals(ctx.channel())) {
                    channel.writeAndFlush(msg);
                }
            }
        }
//        if (msg instanceof ByteBuf) {
//            ByteBuf buf = (ByteBuf) msg;
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.readBytes(bytes);
//            System.out.println(join(bytes, ", "));
//            if (bytes.length > 12) {
//                byte id = bytes[12];
//                byte length = (byte) Math.min(bytes[8], bytes.length - 8);
//                byte[] data = new byte[length];
//                System.arraycopy(bytes, bytes.length - length, data, 0, length);
//                System.out.printf("id: %d\nlength: %d\ndata: %s\n\n", id, length, join(data, ", "));
//                handlePacket(ctx, id, data);
//            }
//        }
    }

//    private void handlePacket(ChannelHandlerContext ctx, byte id, byte[] data) throws UnsupportedEncodingException {
//        if (id == 4) {
//            // Chat message
//            byte[] chatMessage = new byte[data[8]];
//            System.arraycopy(data, data.length - data[8], chatMessage, 0, data[8]);
//            System.out.println("Chat message: " + new String(chatMessage, "utf8"));
//        } else if (id == 5) {
//            // Character selection
//
//        } else if (id == 0) {
//            // Join
//            ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[] {-83, -66, -81, -34, -21, -66, 13, -16, 12, 0, 0, 0}));
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

//    private String join(byte[] bytes, String separator) {
//        StringBuilder builder = new StringBuilder();
//        for (byte b : bytes) {
//            builder.append(b).append(separator);
//        }
//        if (builder.length() > 0) builder.delete(builder.length() - 2, builder.length());
//        return builder.toString();
//    }
}
