/*
 * Copyright 2014 StormCloud Development Group
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

package io.github.stormcloud_dev.stormcloud.command;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.SpawnClassicClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.object.Player;

import static io.github.stormcloud_dev.stormcloud.command.CommandHandlerPriority.HIGH;
import static java.lang.Short.parseShort;

public class SpawnCommand {

    private StormCloud server;

    public SpawnCommand(StormCloud server) {
        this.server = server;
    }

    @CommandHandler(
            name = "spawn",
            priority = HIGH,
            aliases = {"s"},
            description = "Spawns a monster"
    )
    public void onSpawnCommand(Player sender, String[] args) {
        if (args.length >= 6) {
            server.getChannels().stream().forEach(channel -> channel.writeAndFlush(new SpawnClassicClientBoundFrame(0.0, 0.0, parseShort(args[1]), sender.getX(), sender.getY(), parseShort(args[2]) == 1 ? (byte) 1 : (byte) 0, parseShort(args[4]), parseShort(args[5]), parseShort(args[6]))));
        }
    }

}
