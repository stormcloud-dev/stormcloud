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

import io.github.stormcloud_dev.stormcloud.Player;
import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.StormCloudHandler;
import io.github.stormcloud_dev.stormcloud.event.EventHandler;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.StartGameClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.TransportClientBoundFrame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerListener {
    private ConcurrentHashMap<Double, Player> playerList;

    private StormCloud server;

    public PlayerListener(StormCloud server) {
        this.playerList = new ConcurrentHashMap<Double, Player>();
        this.server = server;
    }

    @EventHandler
    public void onPlayerReadyChange(PlayerReadyChangeEvent event) {
        playerList.get(event.getPlayer().getMId()).setReady(event.isReady());
        boolean allReady = true;
        for (Map.Entry<Double, Player> entry : playerList.entrySet()) {
            if (!entry.getValue().isReady()) {
                allReady = false;
            }
        }
        if (allReady) {
            server.getChannels().stream().forEach(channel -> {
                Player player = channel.attr(StormCloudHandler.PLAYER).get();
                channel.writeAndFlush(new StartGameClientBoundFrame(0.0, 0.0, 2));
                channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, 23.0, 1216.0, 736.0, 2440.0, 832.0, (byte)0));
            });
        }
    }

    @EventHandler
    public void onPlayerAdd(PlayerAddEvent event) {
        System.out.println("Added player");
        playerList.put(event.getPlayer().getMId(), event.getPlayer());
    }

}
