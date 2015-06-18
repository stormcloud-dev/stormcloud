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

import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.object.StormCloudObject;

public abstract class Monster extends StormCloudObject {

    private float forceDeath;
    private float sync;
    private float ghostX;
    private float ghostY;
    private float mId;
    private float forceKnockback;
    private String state;
    private float target;
    private float deathTimer;
    private boolean makeSound;
    private float elite;
    private float eliteTier;
    private boolean canJump;
    private float pointValue;
    private float zRange;
    private float xRange;
    private float cRange;
    private float vRange;
    private float[][] froze;
    private float deactivated;
    private float deactivatedTime;
    private float hitPitch;
    private float damage;
    private float enemyBuff;
    private float damageCoefficient;

    public Monster(StormCloud server, int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(server, x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        initAIDefault();
    }

    public Monster(StormCloud server, int x, int y) {
        super(server, x, y);
        initAIDefault();
    }

    private void initAIDefault() {
        forceDeath = 0F;
        sync = 0F;
        ghostX = getX();
        ghostY = getY();
        mId = -1F;
        forceKnockback = 0F;
        state = "idle";
        target = -5F;
        deathTimer = 3600;
        makeSound = true;
        elite = 0F;
        eliteTier = 0F;
        canJump = false;
        pointValue = 0F;
        xRange = 0F;
        cRange = 0F;
        vRange = 0F;
        froze = new float[4][3];
        for (int i = 0; i <= 3; i++) {
            froze[i][0] = -1F; // x position
            froze[i][1] = -1F; // y position
            froze[i][2] = -1F; // height
        }
        deactivated = 0F;
        deactivatedTime = 0F;
        hitPitch = 1F;
    }

    public boolean canJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getZRange() {
        return zRange;
    }

    public void setZRange(float zRange) {
        this.zRange = zRange;
    }

    public float getXRange() {
        return xRange;
    }

    public void setXRange(float xRange) {
        this.xRange = xRange;
    }

    public float getCRange() {
        return cRange;
    }

    public void setCRange(float cRange) {
        this.cRange = cRange;
    }

    public float getVRange() {
        return vRange;
    }

    public void setVRange(float vRange) {
        this.vRange = vRange;
    }

    public float getEnemyBuff() {
        return enemyBuff;
    }

    public float getDamageCoefficient() {
        return damageCoefficient;
    }

}
