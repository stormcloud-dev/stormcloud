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
package io.github.stormcloud_dev.stormcloud.event.player;

import io.github.stormcloud_dev.stormcloud.*;
import io.github.stormcloud_dev.stormcloud.event.EventHandler;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.*;

import java.util.Map;

public class PlayerListener {

    private StormCloud server;

    private boolean ingame = false;

    public PlayerListener(StormCloud server) {
        this.server = server;
    }

    @EventHandler
    public void onPlayerReadyChange(PlayerReadyChangeEvent event) {
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
            server.getChannels().stream().filter(channel -> channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                channel.writeAndFlush(new CrewChoiceClientBoundFrame(0.0, 0.0, (short) 2));
                channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, event.getPlayer().getX(), event.getPlayer().getY(), 2440.0, 832.0, (byte) 0));
                //We have to update the positions of the other players if we join mid game
                server.getChannels().stream().filter(playerChannel -> !playerChannel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(playerChannel -> {
                    Player player = playerChannel.attr(StormCloudHandler.PLAYER).get();
                    channel.writeAndFlush(new PositionInfoClientBoundFrame(167.0, player.getMId(), player.getX(), player.getY(), (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0));

                });
            });
        } else if (allReady) { //If all players are ready, we send the frames to start the game
            System.out.println("All players are ready... game starting!");
            //Random random = new Random();
            server.getChannels().stream().forEach(channel -> {
                //Player player = channel.attr(StormCloudHandler.PLAYER).get();
                //TODO: Level generation
                channel.writeAndFlush(new CrewChoiceClientBoundFrame(0.0, 0.0, (short) 2));
                //First double = 40 means lobby
                //Levels start at 18
                channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, 1216.0, 736.0, 2440.0, 832.0, (byte) 0));
                //channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, 1216.0, 736.0, 2440.0, 832.0, (byte) 0));
            });
            this.ingame = true;
        } else { //Telling the other clients that someone pressed ready
            server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                channel.writeAndFlush(new SetReadyClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), (byte)(event.getPlayer().isReady()?1:0)));
            });
        }
    }

    @EventHandler
    public void onPlayerAdd(PlayerAddEvent event) {
        System.out.println(event.getPlayer().getName() + " joined the game!");
        server.getPlayers().put(event.getPlayer().getMId(), event.getPlayer());
        server.getChannels().stream().forEach(channel -> { //Telling the clients that there's a new player and informing the new player about current players
            if (channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())) { //Sending the new player, which players are already in the game
                server.getChannels().stream().filter(playerChannel -> !playerChannel.equals(channel)).forEach(playerChannel -> {
                    Player currentPlayer = playerChannel.attr(StormCloudHandler.PLAYER).get();
                    channel.writeAndFlush(new AddPlayerClientBoundFrame(currentPlayer.getObjectIndex(), currentPlayer.getMId(), 0.0, 0.0, currentPlayer.getMId(), currentPlayer.getClazz(), 0, currentPlayer.getName()));
                });
             } else { //Sending each player that a new player joined the game
                channel.writeAndFlush(new AddPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), 0.0, 0.0, event.getPlayer().getMId(), event.getPlayer().getClazz(), 0, event.getPlayer().getName()));
            }
        });
    }

    @EventHandler
    public void onPlayerRemove(PlayerRemoveEvent event) {
        System.out.println(event.getPlayer().getName() + " left the game!");
        server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
            channel.writeAndFlush(new DisPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId()));
            channel.writeAndFlush(new ChatPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), 0.0, "'" + event.getPlayer().getName() + "' has left the game!"));
        });
        server.getDisconnectedPlayers().put(event.getPlayer().getLogin(), event.getPlayer());
        server.getPlayers().remove(event.getPlayer().getMId());
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        System.out.println(event.getMessage());

        //TODO: Only debugging will be handled by the command handler later
        String command = event.getMessage().split(" ")[1];
        switch (command) {
            case "!t":
                String[] transportInfo = event.getMessage().split(" ");
                if (transportInfo.length == 7) {
                    if (Double.valueOf(transportInfo[2]) == 40) {
                        ingame = false;
                    }
                    server.getChannels().stream().forEach(channel -> {
                        Player player = channel.attr(StormCloudHandler.PLAYER).get();
                        //TODO: Level generation
                        //channel.writeAndFlush(new CrewChoiceClientBoundFrame(0.0, 0.0, (short) 2));
                        //First double = 40 means lobby
                        //18 -> 38 different maps
                        //41 boss level
                        //16 end scene
                        channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, Double.valueOf(transportInfo[2]), Double.valueOf(transportInfo[3]), Double.valueOf(transportInfo[4]), Double.valueOf(transportInfo[5]), Double.valueOf(transportInfo[6]), (byte) 0));
                        //channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, 1216.0, 736.0, 2440.0, 832.0, (byte) 0));
                    });
                }
                break;
            case "!s":
                String[] spawnInfo = event.getMessage().split(" ");
                if (spawnInfo.length == 7) {
                    server.getChannels().stream().forEach(channel -> {
                        Player player = channel.attr(StormCloudHandler.PLAYER).get();
                        channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, 0.0, Short.valueOf(spawnInfo[2]), event.getPlayer().getX(), event.getPlayer().getY(), (Short.valueOf(spawnInfo[3]) == 1 ? (byte) 1 : (byte) 0), Short.valueOf(spawnInfo[4]), Short.valueOf(spawnInfo[5]), Short.valueOf(spawnInfo[6])));
                    });
                }
                break;
            case "!w":
                //TODO: Remove, just for debugging the possible monster cards for the map
                Double posX = event.getPlayer().getX() - ((Math.random() * 300) - 150);
                Double newEnemyMId = server.getEnemyList().size() + 100.0;
                server.getEnemyList().put(newEnemyMId, new Enemy(newEnemyMId, 0.0, posX, (event.getPlayer().getY() - 300)));
                server.getChannels().stream().forEach(channel -> { //Card 0
                    channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, newEnemyMId, (short) 0, posX, (event.getPlayer().getY() - 300), (byte) 0, (short) 0, (short) 10, (short) 1));
                });
                Double posX1 = event.getPlayer().getX() - ((Math.random() * 300) - 150);
                Double newEnemyMId1 = server.getEnemyList().size() + 100.0;
                server.getEnemyList().put(newEnemyMId1, new Enemy(newEnemyMId1, 0.0, posX, (event.getPlayer().getY() - 300)));
                server.getChannels().stream().forEach(channel -> { //Card 1
                    channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, newEnemyMId1, (short) 1, posX1, (event.getPlayer().getY() - 300), (byte) 0, (short) 0, (short) 10, (short) 1));
                });
                Double posX2 = event.getPlayer().getX() - ((Math.random() * 300) - 150);
                Double newEnemyMId2 = server.getEnemyList().size() + 100.0;
                server.getEnemyList().put(newEnemyMId2, new Enemy(newEnemyMId2, 0.0, posX, (event.getPlayer().getY() - 300)));
                server.getChannels().stream().forEach(channel -> { //Card 3
                    channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, newEnemyMId2, (short) 3, posX2, (event.getPlayer().getY() - 300), (byte) 0, (short) 0, (short) 10, (short) 1));
                });
                Double posX3 = event.getPlayer().getX() - ((Math.random() * 300) - 150);
                Double newEnemyMId3 = server.getEnemyList().size() + 100.0;
                server.getEnemyList().put(newEnemyMId3, new Enemy(newEnemyMId3, 0.0, posX, (event.getPlayer().getY() - 300)));
                server.getChannels().stream().forEach(channel -> { //Card 4
                    channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, newEnemyMId3, (short) 4, posX3, (event.getPlayer().getY() - 300), (byte) 0, (short) 0, (short) 10, (short) 1));
                });
                Double posX4 = event.getPlayer().getX() - ((Math.random() * 300) - 150);
                Double newEnemyMId4 = server.getEnemyList().size() + 100.0;
                server.getEnemyList().put(newEnemyMId4, new Enemy(newEnemyMId4, 0.0, posX, (event.getPlayer().getY() - 300)));
                server.getChannels().stream().forEach(channel -> { //Card 5
                    channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, newEnemyMId4, (short) 5, posX4, (event.getPlayer().getY() - 300), (byte) 0, (short) 0, (short) 10, (short) 1));
                });
                break;
            default:
                server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
                    channel.writeAndFlush(new ChatPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), event.getMessage()));
                });
                break;
        }

    }

    @EventHandler
    public void onPlayerLag(PlayerLagEvent event) {
        server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
            channel.writeAndFlush(new LagPlayerClientBoundFrame(event.getPlayer().getObjectIndex(), event.getPlayer().getMId(), event.getPlayer().getName()));
        });
    }

    @EventHandler
    public void onPlayerPosition(PlayerPositionEvent event) {
        //System.out.println(event.getPlayer().getName() + " moves to " + event.getFrame().getX() + "/" + event.getFrame().getY());
        event.getPlayer().setX(event.getFrame().getX());
        event.getPlayer().setY(event.getFrame().getY());
        server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
            //Object Index of 167 required (seems to be the index for position updates)
            channel.writeAndFlush(new PositionInfoClientBoundFrame(167.0, event.getPlayer().getMId(), event.getFrame().getX(), event.getFrame().getY(), event.getFrame().getLeft(), event.getFrame().getRight(), event.getFrame().getJump(), event.getFrame().getJumpHeld(), event.getFrame().getUp(), event.getFrame().getDown()));
        });
    }

    @EventHandler
    public void onPlayerKey(PlayerKeyEvent event) {
        server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
            //Object Index of 167 required (seems to be the index for position updates)
            channel.writeAndFlush(new KeyPlayerClientBoundFrame(167.0, event.getPlayer().getMId(), event.getFrame().getX(), event.getFrame().getY(), event.getFrame().getZAction(), event.getFrame().getXAction(), event.getFrame().getCAction(), event.getFrame().getVAction(), event.getFrame().getItemUsed(), event.getFrame().getUnknown()));
        });
    }

    @EventHandler
    public void onPlayerUpdate(PlayerUpdateEvent event) {
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
                server.getChannels().stream().filter(channel -> channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
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
        System.out.println(event.getPlayer().getName() + " changed clazz to " + event.getFrame().getClazz());
        event.getPlayer().setClazz((event.getFrame().getClazz() != -1 ? CrewMember.values()[event.getFrame().getClazz()] : null));
        server.getChannels().stream().filter(channel -> !channel.attr(StormCloudHandler.PLAYER).get().equals(event.getPlayer())).forEach(channel -> {
            //Object Index of 210 required (seems to be the index for player updates)
            channel.writeAndFlush(new UpdatePlayerClientBoundFrame(210.0, event.getPlayer().getMId(), event.getFrame().getClazz(), event.getFrame().getX(), event.getFrame().getY(), event.getFrame().getName()));
        });
    }
}
