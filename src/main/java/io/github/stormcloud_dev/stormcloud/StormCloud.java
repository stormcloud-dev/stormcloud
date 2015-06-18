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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.stormcloud_dev.stormcloud.command.*;
import io.github.stormcloud_dev.stormcloud.event.EventManager;
import io.github.stormcloud_dev.stormcloud.event.game.BeginStepEvent;
import io.github.stormcloud_dev.stormcloud.event.game.EndStepEvent;
import io.github.stormcloud_dev.stormcloud.event.game.StepEvent;
import io.github.stormcloud_dev.stormcloud.object.Player;
import io.github.stormcloud_dev.stormcloud.room.RoomManager;
import io.github.stormcloud_dev.stormcloud.seralization.RORObjectDecoder;
import io.github.stormcloud_dev.stormcloud.seralization.RORObjectEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class StormCloud {

    // 16ms means 60 ticks per second, emulating what Risk of Rain does with room speed.
    // (you can see this in the FPS counter in game)
    private static final long DELAY = 16L;

    private Configuration configuration;

    private int port;
    private Timer timer;
    private boolean running;

    private Logger logger;

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

    private CommandManager commandManager;
    private EventManager eventManager;
    private RoomManager roomManager;

    public StormCloud(int port) {
        this.port = port;
        logger = Logger.getLogger(getClass().getName());
        loadConfiguration();
        timer = new HashedWheelTimer();
        commandManager = new CommandManager();
        eventManager = new EventManager();
        roomManager = new RoomManager(this);
        roomManager.loadRooms();
    }

    public void run() throws InterruptedException {
        getLogger().info(format("Starting server on port %d", port));
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
            Channel channel = bootstrap.bind(port).sync().channel();
            startGameLoop();
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void startGameLoop() {
        setRunning(true);
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (isRunning()) {
            onTick();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            beforeTime = System.currentTimeMillis();
        }
    }

    private void loadConfiguration() {
        saveDefaultConfiguration();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Scanner scanner = new Scanner(new FileInputStream(new File("./server.json")));
            StringBuilder jsonBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonBuilder.append(scanner.nextLine()).append('\n');
            }
            configuration = gson.fromJson(jsonBuilder.toString(), Configuration.class);
        } catch (FileNotFoundException exception) {
            getLogger().log(SEVERE, "Failed to load configuration", exception);
        }
    }

    private void saveDefaultConfiguration() {
        File configFile = new File("./server.json");
        if (!configFile.getParentFile().isDirectory()) {
            if (!configFile.getParentFile().delete()) {
                try {
                    getLogger().severe("Could not delete existing file at " + configFile.getParentFile().getCanonicalPath() + ": does the server have the correct directory permissions?");
                } catch (IOException exception) {
                    getLogger().log(SEVERE, "Could not get path to parent directory of config file: does the server have the correct directory permissions?", exception);
                }
            }
        }
        if (!configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdirs()) {
                try {
                    getLogger().severe("Could not create server directory at " + configFile.getParentFile().getCanonicalPath() + ": does the server have the correct directory permissions?");
                } catch (IOException exception) {
                    getLogger().log(SEVERE, "Could not get path to parent directory of config file: does the server have the correct directory permissions?");
                }
            }
        }
        if (!configFile.exists()) {
            Configuration defaultConfig = new Configuration();
            defaultConfig.setMaxPlayers(10);
            defaultConfig.setPort(11100);
            defaultConfig.setTimeout((byte) 20);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                FileWriter writer = new FileWriter(configFile);
                writer.write(gson.toJson(defaultConfig));
                writer.close();
            } catch (IOException exception) {
                getLogger().log(SEVERE, "Failed to save default configuration: does the server have write permissions to the config file?", exception);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private void onTick() {
        getEventManager().onEvent(new BeginStepEvent());
        getEventManager().onEvent(new StepEvent());
        getEventManager().onEvent(new EndStepEvent());
    }

    public Timer getTimer() {
        return timer;
    }

    public Logger getLogger() {
        return logger;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 11100;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        new StormCloud(port).run();
    }

}
