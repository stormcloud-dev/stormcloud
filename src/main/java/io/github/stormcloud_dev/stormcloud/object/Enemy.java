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
package io.github.stormcloud_dev.stormcloud.object;

public class Enemy extends StormCloudObject {

    private double mId, objectIndex;

    private short hp;

    public Enemy(double mId, double objectIndex, int x, int y) {
        super(x, y);
        this.mId = mId;
        this.objectIndex = objectIndex;
    }

    public double getMId() {
        return mId;
    }

    public double getObjectIndex() {
        return objectIndex;
    }

    public short getHp() {
        return hp;
    }

    public void setHp(short hp) {
        this.hp = hp;
    }

}
