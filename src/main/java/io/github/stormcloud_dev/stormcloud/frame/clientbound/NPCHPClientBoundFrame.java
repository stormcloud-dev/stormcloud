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
package io.github.stormcloud_dev.stormcloud.frame.clientbound;

import io.netty.buffer.ByteBuf;

public class NPCHPClientBoundFrame extends ClientBoundFrame {

    private double hp;
    private double x;
    private double y;
    private short xScale;
    private short knockback;
    private short ticksTillChase;

    public NPCHPClientBoundFrame(double objectIndex, double multiplayerId, double hp, double x, double y, short xScale, short knockback, short ticksTillChase) {
        super((byte) 8, objectIndex, multiplayerId);
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.knockback = knockback;
        this.ticksTillChase = ticksTillChase;
    }

    @Override
    public int getLength() {
        return 47;
    }

    public double getHp() {
        return hp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public short getXScale() {
        return xScale;
    }

    public short getKnockback() {
        return knockback;
    }

    public short getTicksTillChase() {
        return ticksTillChase;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeDouble(getHp());
        buf.writeDouble(getX());
        buf.writeDouble(getY());
        buf.writeShort(getXScale());
        buf.writeShort(getKnockback());
        buf.writeShort(getTicksTillChase());
    }

}
