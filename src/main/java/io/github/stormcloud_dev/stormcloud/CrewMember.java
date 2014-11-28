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

public enum CrewMember {

    COMMANDO((short) 0),
    ENFORCER((short) 1),
    BANDIT((short) 2),
    HUNTRESS((short) 3),
    HAN_D((short) 4),
    ENGINEER((short) 5),
    MINER((short) 6),
    SNIPER((short) 7),
    ACRID((short) 8),
    MERCENARY((short) 9),
    LOADER((short) 10),
    CHEF((short) 11);

    private short id;

    private CrewMember(short id) {
        this.id = id;
    }

    public short getId() {
        return id;
    }

}
