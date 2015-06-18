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

package io.github.stormcloud_dev.stormcloud.alarm;

import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.event.game.AlarmEvent;

public class Alarm {

    private StormCloud server;

    private int index;
    private int countdown;

    public Alarm(StormCloud server, int index) {
        this.server = server;
        this.index = index;
        this.countdown = -1;
    }

    public int getIndex() {
        return index;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void onTick() {
        countdown = countdown > -1 ? countdown - 1 : -1;
        if (countdown == 0) {
            server.getEventManager().onEvent(new AlarmEvent(this));
        }
    }

}
