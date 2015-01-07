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
import io.github.stormcloud_dev.stormcloud.frame.clientbound.TransportClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.object.Player;

import static io.github.stormcloud_dev.stormcloud.command.CommandHandlerPriority.HIGH;
import static java.lang.Double.parseDouble;

public class TransportCommand {
    
    private StormCloud server;

    public TransportCommand(StormCloud server) {
        this.server = server;
    }

    @CommandHandler(
            name = "transport",
            priority = HIGH,
            aliases = {"t"},
            description = "Transports you to a different area"
    )
    public void onTransportCommand(Player sender, String[] args) {
        if (args.length >= 6) {
            // This is for going back to the lobby. Right now, this is ignored.
//            if (parseDouble(transportInfo[2]) == 40) {
//                //ingame = false;
//            }
            server.getChannels().stream().forEach(channel -> {
                //TODO: We need to do some stuff with setting room here, and enemy spawning needs to be done.
                // Room loading is now mostly done (see io.github.stormcloud_dev.stormcloud.room.Room)
                // First double = 40 means lobby
                // 18 -> 38 different maps
                // 41 boss level
                // 16 end scene
                channel.writeAndFlush(new TransportClientBoundFrame(0.0, 0.0, parseDouble(args[1]), parseDouble(args[2]), parseDouble(args[3]), parseDouble(args[4]), parseDouble(args[5]), (byte) 0));
            });
        }
    }

}
