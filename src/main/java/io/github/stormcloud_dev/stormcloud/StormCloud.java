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

import io.github.stormcloud_dev.stormcloud.command.*;
import io.github.stormcloud_dev.stormcloud.event.EventManager;
import io.github.stormcloud_dev.stormcloud.object.Enemy;
import io.github.stormcloud_dev.stormcloud.object.Player;
import io.github.stormcloud_dev.stormcloud.seralization.RORObjectDecoder;
import io.github.stormcloud_dev.stormcloud.seralization.RORObjectEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.util.concurrent.ConcurrentHashMap;

public class StormCloud {

    private int port;
    private Timer timer;

    private StormCloudHandler handler;

    public ChannelGroup getChannels() {
        return handler.getChannels();
    }

    public ConcurrentHashMap<Double, Player> getPlayers() {
        return handler.getPlayers();
    }
    public ConcurrentHashMap<String, Player> getDisconnectedPlayers() {
        return handler.getDisconnectedPlayers();
    }
    public ConcurrentHashMap<Double, Enemy> getEnemyList() {
        return handler.getEnemyList();
    }

    private CommandManager commandManager;
    private EventManager eventManager;

    public StormCloud(int port) {
        this.port = port;
        timer = new HashedWheelTimer();
        commandManager = new CommandManager();
        eventManager = new EventManager();
    }

    public void run() throws InterruptedException {
        try {
            getCommandManager().addCommand(new SpawnCommand(this));
            getCommandManager().addCommand(new TransportCommand(this));
        } catch (InvalidCommandHandlerException | CommandConflictException exception) {
            exception.printStackTrace();
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            handler = new StormCloudHandler(this);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RORObjectEncoder())
                                    .addLast(new RORObjectDecoder())
                                    .addLast(handler);
                        }
                    });
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 11100;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        System.out.println("Listening on port: " + port);
        new StormCloud(port).run();
    }

}
