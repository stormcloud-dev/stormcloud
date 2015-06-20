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
package io.github.stormcloud_dev.stormcloud.object.monster;

public class WispG extends Monster {

    public WispG(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(260);
        initWispG();
    }

    public WispG(int x, int y) {
        super(x, y);
        setZRange(260);
        initWispG();
    }

    private void initWispG() {
        setName("Greater Wisp");
        setCanJump(false);
        setDamage(23F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
