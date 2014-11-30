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
import io.netty.channel.ChannelHandlerContext;

public class PauldronItemProcFrame extends ItemProcFrame {

    private double x;
    private double y;
    private double damage;
    private short alarm0;
    private String team;

    public PauldronItemProcFrame(double x, double y, double damage, short alarm0, String team) {
        super((short) 1);
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.alarm0 = alarm0;
        this.team = team;
    }

    @Override
    public int getLength() {
        return 30 + getTeam().getBytes().length;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDamage() {
        return damage;
    }

    public short getAlarm0() {
        return alarm0;
    }

    public String getTeam() {
        return team;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeDouble(getDamage());
        buf.writeShort(getAlarm0());
        for (byte b : team.getBytes()) {
            buf.writeByte(b);
        }
        buf.writeByte(0);
    }
}
