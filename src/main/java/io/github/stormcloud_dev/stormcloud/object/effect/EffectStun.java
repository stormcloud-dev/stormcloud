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

package io.github.stormcloud_dev.stormcloud.object.effect;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.event.EventHandler;
import io.github.stormcloud_dev.stormcloud.event.InvalidEventHandlerException;
import io.github.stormcloud_dev.stormcloud.event.game.EndStepEvent;
import io.github.stormcloud_dev.stormcloud.object.Player;
import io.github.stormcloud_dev.stormcloud.object.StormCloudObject;

import static java.util.logging.Level.SEVERE;

public class EffectStun extends StormCloudObject {

    private Player parent;

    public EffectStun(StormCloud server, int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(server, x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        registerEventHandlers();
    }

    public EffectStun(StormCloud server, int x, int y) {
        super(server, x, y);
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        try {
            getServer().getEventManager().addListener(this);
        } catch (InvalidEventHandlerException exception) {
            getServer().getLogger().log(SEVERE, "Failed to register events for stun effect", exception);
        }
    }

    @EventHandler
    public void onEndStep(EndStepEvent event) {
        if (getParent() != null && getParent().isExistent()) {
            setX(getParent().getX());
            setY(getParent().getY() - 8);
            if (getParent().getAlarmManager().getAlarm(7).getCountdown() <= 0) {
                getParent().setStunned(false);
                destroy();
            }
        } else {
            destroy();
        }
    }

    public Player getParent() {
        return parent;
    }

    public void setParent(Player parent) {
        this.parent = parent;
    }

}
