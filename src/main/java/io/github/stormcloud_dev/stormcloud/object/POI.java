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

package io.github.stormcloud_dev.stormcloud.object;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.event.EventHandler;
import io.github.stormcloud_dev.stormcloud.event.InvalidEventHandlerException;
import io.github.stormcloud_dev.stormcloud.event.game.EndStepEvent;

import static java.util.logging.Level.SEVERE;

public class POI extends StormCloudObject {

    private StormCloudObject parent;

    public POI(StormCloud server, int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation, StormCloudObject parent) {
        super(server, x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setParent(parent);
        registerEventHandlers();
    }

    public POI(StormCloud server, int x, int y, StormCloudObject parent) {
        super(server, x, y);
        setParent(parent);
        registerEventHandlers();
    }

    @EventHandler
    public void onEndStep(EndStepEvent event) {
        if (!getParent().isExistent()) {
            destroy();
            return;
        }
        setX(getParent().getX());
        setY(getParent().getY());
    }

    public StormCloudObject getParent() {
        return parent;
    }

    public void setParent(StormCloudObject parent) {
        this.parent = parent;
    }

    private void registerEventHandlers() {
        try {
            getServer().getEventManager().addListener(this);
        } catch (InvalidEventHandlerException exception) {
            getServer().getLogger().log(SEVERE, "Could not register POI event handlers", exception);
        }
    }

}
