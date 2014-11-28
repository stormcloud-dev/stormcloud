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
package io.github.stormcloud_dev.stormcloud.frame.itemproc;

import io.netty.buffer.ByteBuf;

public class EnemyMissileItemProcFrame extends ItemProcFrame {

    private double x;
    private double y;
    private short damage;
    private short direction;
    private short alarm0;

    public EnemyMissileItemProcFrame(double x, double y, short damage, short direction, short alarm0) {
        super((short) 12);
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.direction = direction;
        this.alarm0 = alarm0;
    }

    @Override
    public int getLength() {
        return 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public short getDamage() {
        return damage;
    }

    public short getDirection() {
        return direction;
    }

    public short getAlarm0() {
        return alarm0;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeShort(getDamage());
        buf.writeShort(getDirection());
        buf.writeShort(getAlarm0());
    }

}
