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

    private volatile boolean ingame;

    private StormCloud server;

    public StormCloudHandler(StormCloud server) {
        this.server = server;
        this.playerList = new ConcurrentHashMap<>();
        this.disconnectedPlayerList = new ConcurrentHashMap<>();
        this.enemyList = new ConcurrentHashMap<>();
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
        PlayerRemoveEvent event = new PlayerRemoveEvent(ctx.channel().attr(PLAYER).get());
        server.getEventManager().onEvent(event);
        System.out.println(event.getPlayer().getName() + " left the game!");
        server.getChannels().stream().filter(channel -> !channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
            channel.writeAndFlush(new DisPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId()));
            channel.writeAndFlush(new ChatPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), 0.0, "'" + event.getPlayer().getName() + "' has left the game!"));
        });
        server.getDisconnectedPlayers().put(event.getPlayer().getLogin(), event.getPlayer());
        server.getPlayers().remove(event.getPlayer().getMId());
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
            PlayerAddEvent event = new PlayerAddEvent(sender);
            server.getEventManager().onEvent(new PlayerAddEvent(sender));
            System.out.println(event.getPlayer().getName() + " joined the game!");
            server.getPlayers().put(event.getPlayer().getMId(), event.getPlayer());
            server.getChannels().stream().forEach(channel -> { //Telling the clients that there's a new player and informing the new player about current players
                if (channel.attr(PLAYER).get().equals(event.getPlayer())) { //Sending the new player, which players are already in the game
                    server.getChannels().stream().filter(playerChannel -> !playerChannel.equals(channel)).forEach(playerChannel -> {
                        Player currentPlayer = playerChannel.attr(PLAYER).get();
                        channel.writeAndFlush(new AddPlayerClientBoundFrame(currentPlayer.getObjectIndex(), currentPlayer.getMId(), 0.0, 0.0, currentPlayer.getMId(), currentPlayer.getClazz(), 0, currentPlayer.getName()));
                    });
                } else { //Sending each player that a new player joined the game
                    channel.writeAndFlush(new AddPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), 0.0, 0.0, event.getPlayer().getMId(), event.getPlayer().getClazz(), 0, event.getPlayer().getName()));
                }
            });
        } else if (msg instanceof ChatPlayerServerBoundFrame) {
            ChatPlayerServerBoundFrame frame = (ChatPlayerServerBoundFrame) msg;
            if (frame.getText().startsWith("!")) {
                String[] messageParts = frame.getText().split("\\s+");
                String[] args = new String[messageParts.length - 1];
                System.arraycopy(messageParts, 1, args, 0, messageParts.length - 1);
                server.getCommandManager().onCommand(sender, messageParts[0].substring(1), args);
            } else {
                PlayerChatEvent event = new PlayerChatEvent(sender, frame);
                server.getEventManager().onEvent(event);
                getChannels().stream().filter(channel -> channel != ctx.channel()).forEach(channel -> channel.writeAndFlush(new ChatPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), event.getMessage())));
            }
        } else if (msg instanceof UpdatePlayerServerBoundFrame) {
            UpdatePlayerServerBoundFrame frame = (UpdatePlayerServerBoundFrame) msg;
            PlayerUpdateEvent event = new PlayerUpdateEvent(sender, frame);
            server.getEventManager().onEvent(event);
            event.getPlayer().setName(event.getFrame().getName().replace("|", ""));
            //event.getPlayer().setLogin(event.getPlayer().getName());
            if (ingame) { //Ingame -> direct transport to the map
                if (event.getFrame().getClazz() == -1) { //The first player update says that this player has no clazz
                    if (server.getDisconnectedPlayers().containsKey(event.getPlayer().getLogin())) { //If there was a player on that slot he has to get the same clazz etc.
                        //event.getPlayer().setMId(oldPlayerList.get(event.getPlayer().getLogin()).getClazz());
                        event.getPlayer().setClazz(server.getDisconnectedPlayers().get(event.getPlayer().getLogin()).getClazz());
                        event.getPlayer().setX(server.getDisconnectedPlayers().get(event.getPlayer().getLogin()).getX());
                        event.getPlayer().setY(server.getDisconnectedPlayers().get(event.getPlayer().getLogin()).getY());
                        server.getDisconnectedPlayers().remove(event.getPlayer().getLogin());
                    }
                    //Only to the new player
                    server.getChannels().stream().filter(channel -> channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                        //We have to update the positions of the other players if we join mid game
                        server.getPlayers().entrySet().stream().filter(entry -> !entry.getValue().equals(event.getPlayer())).forEach(entry -> {
                            Player player = entry.getValue();
                            channel.writeAndFlush(new UpdatePlayerClientBoundFrame(210.0, player.getMId(), player.getClazz(), player.getX(), player.getY(), player.getName() + "|"));
                        });
                        //We have to setup the disconnected players too (If more than one player disconnects)
                        server.getDisconnectedPlayers().entrySet().stream().filter(entry -> !entry.getValue().equals(event.getPlayer())).forEach(entry -> {
                            Player player = entry.getValue();
                            channel.writeAndFlush(new UpdatePlayerClientBoundFrame(210.0, player.getMId(), player.getClazz(), player.getX(), player.getY(), player.getName() + "|"));
                        });
                        //Setup the old character status and go ingame
                        channel.writeAndFlush(new UpdatePlayerClientBoundFrame(210.0, event.getPlayer().getMId(), event.getPlayer().getClazz(), event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getName() + "|"));
                        channel.writeAndFlush(new CrewChoiceClientBoundFrame(0.0, 0.0, (short) 2));
                        channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, event.getPlayer().getX(), event.getPlayer().getY(), 2440.0, 832.0, (byte) 0));
                        //We have to update the positions of the other players if we join mid game
                        server.getPlayers().entrySet().stream().filter(entry -> !entry.getValue().equals(event.getPlayer())).forEach(entry -> {
                            Player player = entry.getValue();
                            channel.writeAndFlush(new PositionInfoClientBoundFrame(167.0, player.getMId(), player.getX(), player.getY(), (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0));

                        });
                        //We also update the positions of the other disconnected players
                        server.getDisconnectedPlayers().entrySet().stream().filter(entry -> !entry.getValue().equals(event.getPlayer())).forEach(entry -> {
                            Player player = entry.getValue();
                            channel.writeAndFlush(new PositionInfoClientBoundFrame(167.0, player.getMId(), player.getX(), player.getY(), (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0));
                        });
                    });
                    return; // We won't remove the character selection if it was already set
                }
            }
            System.out.println(event.getPlayer().getName() + " changed class to " + event.getFrame().getClazz());
            event.getPlayer().setClazz((event.getFrame().getClazz() != -1 ? CrewMember.values()[event.getFrame().getClazz()] : null));
            server.getChannels().stream().filter(channel -> !channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                // Object Index of 210 required (seems to be the index for player updates)
                channel.writeAndFlush(new UpdatePlayerClientBoundFrame(210.0, event.getPlayer().getMId(), event.getFrame().getClazz(), event.getFrame().getX(), event.getFrame().getY(), event.getFrame().getName()));
            });
        } else if (msg instanceof LagPlayerServerBoundFrame) {
            PlayerLagEvent event = new PlayerLagEvent(sender);
            server.getEventManager().onEvent(event);
            getChannels().stream().filter(channel -> channel != ctx.channel()).forEach(channel -> channel.writeAndFlush(new LagPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), event.getPlayer().getName())));
        } else if (msg instanceof SetReadyServerBoundFrame) {
            SetReadyServerBoundFrame frame = (SetReadyServerBoundFrame) msg;
            PlayerReadyChangeEvent event = new PlayerReadyChangeEvent(sender, frame);
            server.getEventManager().onEvent(event);
            System.out.println(event.getPlayer().getName() + " is ready!");
            server.getPlayers().get(event.getPlayer().getMId()).setReady(event.isReady());
            //Check if all players are ready
            boolean allReady = true;
            for (Map.Entry<Double, Player> entry : server.getPlayers().entrySet()) {
                if (!entry.getValue().isReady()) {
                    allReady = false;
                }
            }
            if (ingame) { //When we are ingame, players have to take the slots of left players (Can't add new players)
                server.getChannels().stream().filter(channel -> channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                    channel.writeAndFlush(new CrewChoiceClientBoundFrame(0.0, 0.0, (short) 2));
                    channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, event.getPlayer().getX(), event.getPlayer().getY(), 2440.0, 832.0, (byte) 0));
                    //We have to update the positions of the other players if we join mid game
                    server.getChannels().stream().filter(playerChannel -> !playerChannel.attr(PLAYER).get().equals(event.getPlayer())).forEach(playerChannel -> {
                        Player player = playerChannel.attr(PLAYER).get();
                        channel.writeAndFlush(new PositionInfoClientBoundFrame(167.0, player.getMId(), player.getX(), player.getY(), (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
                    });
                });
            } else if (allReady) { //If all players are ready, we send the frames to start the game
                System.out.println("All players are ready... game starting!");
                //Random random = new Random();
                server.getChannels().stream().forEach(channel -> {
                    //Player player = channel.attr(PLAYER).get();
                    //TODO: Level generation
                    channel.writeAndFlush(new CrewChoiceClientBoundFrame(0.0, 0.0, (short) 2));
                    //First double = 40 means lobby
                    //Levels start at 18
                    channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, 1216.0, 736.0, 2440.0, 832.0, (byte) 0));
                    //channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, 1216.0, 736.0, 2440.0, 832.0, (byte) 0));
                });
                this.ingame = true;
            } else { //Telling the other clients that someone pressed ready
                server.getChannels().stream().filter(channel -> !channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                    channel.writeAndFlush(new SetReadyClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), (byte)(event.getPlayer().isReady()?1:0)));
                });
            }
        } else if (msg instanceof PositionInfoServerBoundFrame) {
            PositionInfoServerBoundFrame frame = (PositionInfoServerBoundFrame) msg;
            PlayerPositionEvent event = new PlayerPositionEvent(sender, frame);
            server.getEventManager().onEvent(event);
            event.getPlayer().setX((int) event.getX());
            event.getPlayer().setY((int) event.getY());
            getChannels().stream().filter(channel -> !channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                // Object Index of 167 required (seems to be the index for position updates)
                channel.writeAndFlush(new PositionInfoClientBoundFrame(167.0, event.getPlayer().getMId(), event.getX(), event.getY(), event.isLeft() ? (byte) 1 : (byte) 0, event.isRight() ? (byte) 1 : (byte) 0, event.isJump() ? (byte) 1 : (byte) 0, event.isJumpHeld() ? (byte) 1 : (byte) 0, event.isUp() ? (byte) 1 : (byte) 0, event.isDown() ? (byte) 1 : (byte) 0));
            });
        } else if (msg instanceof KeyPlayerServerBoundFrame) {
            KeyPlayerServerBoundFrame frame = (KeyPlayerServerBoundFrame) msg;
            PlayerKeyEvent event = new PlayerKeyEvent(sender, frame);
            server.getEventManager().onEvent(event);
            getChannels().stream().filter(channel -> !channel.attr(PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                //Object Index of 167 required (seems to be the index for position updates)
                channel.writeAndFlush(new KeyPlayerClientBoundFrame(167.0, event.getPlayer().getMId(), event.getFrame().getX(), event.getFrame().getY(), event.getFrame().getZAction(), event.getFrame().getXAction(), event.getFrame().getCAction(), event.getFrame().getVAction(), event.getFrame().getItemUsed(), event.getFrame().getUnknown()));
            });
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
