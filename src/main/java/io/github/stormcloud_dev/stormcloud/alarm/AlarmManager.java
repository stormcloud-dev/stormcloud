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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlarmManager {

    private List<Alarm> alarms;

    public AlarmManager(StormCloud server) {
        alarms = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 12; i++) {
            alarms.add(new Alarm(server, i));
        }
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public Alarm getAlarm(int index)  {
        return getAlarms().get(index);
    }

    public void onStep() {
        getAlarms().parallelStream().forEach(Alarm::onTick);
    }

}
