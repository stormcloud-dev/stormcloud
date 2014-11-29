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

public class Player {

    private double MId, objectIndex;

    private String name;

    private CrewMember clazz;

    public Player(double MId, double objectIndex) {
        this.MId = MId;
        this.objectIndex = objectIndex;
        this.clazz = CrewMember.COMMANDO;
        this.name = "Anonymous";
    }

    public double getMId() {
        return MId;
    }

    public double getObjectIndex() {
        return objectIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CrewMember getClazz() {
        return clazz;
    }

    public void setClazz(CrewMember clazz) {
        this.clazz = clazz;
    }
}
