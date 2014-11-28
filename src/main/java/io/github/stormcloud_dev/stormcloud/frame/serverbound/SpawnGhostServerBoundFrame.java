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
package io.github.stormcloud_dev.stormcloud.frame.serverbound;

import io.netty.buffer.ByteBuf;

public class SpawnGhostServerBoundFrame extends ServerBoundFrame {

    private short x;
    private short y;
    private float damageCoefficient;
    private float healthCoefficient;

    public SpawnGhostServerBoundFrame(short x, short y, float damageCoefficient, float healthCoefficient) {
        super((byte) 40);
        this.x = x;
        this.y = y;
        this.damageCoefficient = damageCoefficient;
        this.healthCoefficient = healthCoefficient;
    }

    @Override
    public int getLength() {
        return 29;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    public float getDamageCoefficient() {
        return damageCoefficient;
    }

    public float getHealthCoefficient() {
        return healthCoefficient;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeShort(getX());
        buf.writeShort(getY());
        buf.writeFloat(getDamageCoefficient());
        buf.writeFloat(getHealthCoefficient());
    }
}
