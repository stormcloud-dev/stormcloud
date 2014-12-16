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

import io.github.stormcloud_dev.stormcloud.event.InvalidEventHandlerException;
import io.github.stormcloud_dev.stormcloud.event.player.*;
import io.github.stormcloud_dev.stormcloud.frame.HandshakeFrame;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.*;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.*;
import io.github.stormcloud_dev.stormcloud.listener.PlayerListener;
import io.github.stormcloud_dev.stormcloud.object.Enemy;
import io.github.stormcloud_dev.stormcloud.object.Player;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.stormcloud_dev.stormcloud.Attribute.PLAYER;
import static io.netty.channel.ChannelHandler.Sharable;
import static java.util.concurrent.TimeUnit.SECONDS;

@Sharable
public class StormCloudHandler extends ChannelHandlerAdapter {

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public ChannelGroup getChannels() {
        return channels;
    }

    private ConcurrentHashMap<Double, Player> playerList;
    public ConcurrentHashMap<Double, Player> getPlayers() {
        return playerList;
    }

    private ConcurrentHashMap<String, Player> disconnectedPlayerList;
    public ConcurrentHashMap<String, Player> getDisconnectedPlayers() {
        return disconnectedPlayerList;
    }

    private ConcurrentHashMap<Double, Enemy> enemyList;
    public ConcurrentHashMap<Double, Enemy> getEnemyList() {
        return enemyList;
    }

    private StormCloud server;

    public StormCloudHandler(StormCloud server) {
        this.server = server;
        this.playerList = new ConcurrentHashMap<>();
        this.disconnectedPlayerList = new ConcurrentHashMap<>();
        this.enemyList = new ConcurrentHashMap<>();
        try {
            this.server.getEventManager().addListener(new PlayerListener(server));
        } catch(InvalidEventHandlerException e) {
            e.printStackTrace();
        }
    }

    public void scheduleTestPacket(Channel channel) {
        server.getTimer().newTimeout(timeout -> {
            Player player = channel.attr(PLAYER).get();
            if (channel.isActive()) {
                channel.writeAndFlush(new TestClientBoundFrame(player.getObjectIndex(), player.getMId()));
                enemyList.entrySet().stream().forEach(entry -> {
                    Enemy enemy = entry.getValue();
                    enemy.setX(enemy.getX() + 1);
                    enemy.setHp((short) (enemy.getHp() - 1));
                    if (enemy.getHp() <= 0) {
                        channel.writeAndFlush(new KeyMonsterClientBoundFrame(220.0, enemy.getMId(), enemy.getX(), enemy.getY(), (byte) 0, (byte) 0, (byte) 0, (byte) 0, (short) 1));
                        channel.writeAndFlush(new NPCHPClientBoundFrame(220.0, enemy.getMId(), enemy.getHp(), enemy.getX(), enemy.getY(), (short) 1, (short) 1, (byte) 0));
                    } else {
                        channel.writeAndFlush(new MDeadClientBoundFrame(220.0, enemy.getMId()));
                    }
                });
                scheduleTestPacket(channel);
            }
        }, 1, SECONDS);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel active");
        //Random random = new Random();
        //Multiplayer ID's are starting at 9
        ctx.channel().attr(PLAYER).set(new Player(getPlayerId(9.0, ctx.channel().remoteAddress().toString().split(":")[0]), 210.0, ctx.channel().remoteAddress().toString().split(":")[0]));
        channels.add(ctx.channel());

        ctx.writeAndFlush(Unpooled.wrappedBuffer("GM:Studio-Connect\u0000".getBytes("utf8")));
        //Thread that tests if the connection is alive (The client needs that, else it will disconnect)
        Thread testThread = new Thread(() -> {
            Channel channel = ctx.channel();
            scheduleTestPacket(channel);
        });
        testThread.start();
    }

    //The id is the start id, if its used it checks the next
    public double getPlayerId(Double id, String ip) {
        if (disconnectedPlayerList.containsKey(ip)) {
            return disconnectedPlayerList.get(ip).getMId();
        }
        return getPlayerId(id);
    }

    public double getPlayerId(Double id) {
        ConcurrentHashMap<Double, Player> allPlayers = new ConcurrentHashMap<>();
        allPlayers.putAll(playerList);
        for (Map.Entry<String, Player> entry : disconnectedPlayerList.entrySet()) {
            allPlayers.put(entry.getValue().getMId(), entry.getValue());
        }
        for (Map.Entry<Double, Player> entry : allPlayers.entrySet()) {
            if (entry.getValue().getMId() == id){
                return getPlayerId(id + 1);
            }
        }
        return id;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel inactive");
        //Removing the player
        server.getEventManager().onEvent(new PlayerRemoveEvent(ctx.channel().attr(PLAYER).get()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //The player that send a frame
        Player sender = ctx.channel().attr(PLAYER).get();
        if (msg instanceof HandshakeFrame) { //These packets have to send in the exact order with exactly these values, else the client gets a black screen...
            ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{-83, -66, -81, -34, -21, -66, 13, -16, 12, 0, 0, 0}));
            //40 = Room ID of Lobby, 15 = Object Index for set player
            ctx.writeAndFlush(new SetPlayerClientBoundFrame(15.0, 0.0, sender.getMId(), 40.0, "v1.2.4"));
            ctx.writeAndFlush(new UpdateDiffClientBoundFrame(0.0, 0.0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
            //We tell the player listener that there's a new player
            server.getEventManager().onEvent(new PlayerAddEvent(sender));
        } else if (msg instanceof ChatPlayerServerBoundFrame) {
            ChatPlayerServerBoundFrame serverFrame = (ChatPlayerServerBoundFrame) msg;
            if (serverFrame.getText().startsWith("!")) {
                String[] messageParts = serverFrame.getText().split("\\s+");
                String[] args = new String[messageParts.length - 1];
                System.arraycopy(messageParts, 1, args, 0, messageParts.length - 1);
                server.getCommandManager().onCommand(sender, messageParts[0].substring(1), args);
            } else {
                server.getEventManager().onEvent(new PlayerChatEvent(sender, serverFrame));
            }
        } else if (msg instanceof UpdatePlayerServerBoundFrame) {
            UpdatePlayerServerBoundFrame serverFrame = (UpdatePlayerServerBoundFrame) msg;
            server.getEventManager().onEvent(new PlayerUpdateEvent(sender, serverFrame));
        } else if (msg instanceof LagPlayerServerBoundFrame) {
            server.getEventManager().onEvent(new PlayerLagEvent(sender));
        } else if (msg instanceof SetReadyServerBoundFrame) {
            SetReadyServerBoundFrame serverFrame = (SetReadyServerBoundFrame) msg;
            server.getEventManager().onEvent(new PlayerReadyChangeEvent(sender, serverFrame));
        } else if (msg instanceof PositionInfoServerBoundFrame) {
            PositionInfoServerBoundFrame serverFrame = (PositionInfoServerBoundFrame) msg;
            server.getEventManager().onEvent(new PlayerPositionEvent(sender, serverFrame));
        } else if (msg instanceof KeyPlayerServerBoundFrame) {
            KeyPlayerServerBoundFrame serverFrame = (KeyPlayerServerBoundFrame) msg;
            server.getEventManager().onEvent(new PlayerKeyEvent(sender, serverFrame));
        } else {
            System.out.println(msg.getClass().getSimpleName());
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
