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
package io.github.stormcloud_dev.stormcloud.seralization;

import io.github.stormcloud_dev.stormcloud.frame.HandshakeFrame;
import io.github.stormcloud_dev.stormcloud.frame.itemproc.*;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Arrays;
import java.util.List;

public class RORObjectDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects) throws Exception {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        //System.out.println(Arrays.toString(bytes));
        if (new String(bytes, "utf8").equals("GM:Studio-Connect\u0000")) {
            objects.add(new HandshakeFrame());
            return;
        }
        buf.resetReaderIndex();
        while (buf.readableBytes() > 0) {
            Object obj = readNextObject(buf, ctx);
            if (obj != null) {
                objects.add(obj);
            }
        }
    }

    private Object readNextObject(ByteBuf buf, ChannelHandlerContext ctx) {
        if (buf.readableBytes() < 12) return null;
        byte[] header = new byte[8];
        buf.readBytes(8).readBytes(header);

        if (!Arrays.equals(header, new byte[]{-34, -64, -83, -34, 12, 0, 0, 0})) {
            //System.out.println(Arrays.toString(header));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[]{-83, -66, -81, -34, -21, -66, 13, -16, 12, 0, 0, 0}));
            ctx.writeAndFlush(new SetPlayerServerBoundFrame(0.0, 0.0, "v1.2.4"));
            ctx.writeAndFlush(new UpdateDiffServerBoundFrame((byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0));
            ctx.writeAndFlush(new AddPlayerServerBoundFrame(0.0, 0.0, 0.0, -1, 1, "HOST|"));

            return null;
        }

        int length = buf.readByte();
        buf.readBytes(3);
        byte id = buf.readByte();
        switch (id) {
            case 0:
                if (length < 39) return null;
                return new PositionInfoServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte());
            case 1:
                return new SetReadyServerBoundFrame(buf.readByte());
            case 2:
                return new SetPlayerServerBoundFrame(buf.readDouble(), buf.readDouble(), readString(buf));
            case 3:
                return new AddPlayerServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readInt(), readString(buf));
            case 4:
                return new ChatPlayerServerBoundFrame(readString(buf));
            case 5:
                return new UpdatePlayerServerBoundFrame(buf.readInt(), buf.readDouble(), buf.readDouble(), readString(buf));
            case 6:
                return new KeyPlayerServerBoundFrame(buf.readShort(), buf.readShort(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readShort(), buf.readByte());
            case 7:
                return new HealPlayerServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
            case 8:
                return new NPCHPServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readShort(), buf.readShort(), buf.readShort());
            case 9:
                return new MDeadServerBoundFrame();
            case 10:
                return new NPCTargetServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readShort(), buf.readByte());
            case 11:
                return new KeyMonsterServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readShort());
            case 12:
                return new CreateObjectServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble());
            case 13:
                return new CreateLevelObjectServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt());
            case 14:
                return new SyncAlarmServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt());
            case 15:
                return new SpawnClassicServerBoundFrame(buf.readShort(), buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readShort(), buf.readShort(), buf.readShort());
            case 16:
                return new LagPlayerServerBoundFrame(readString(buf));
            case 17:
                return new DisPlayerServerBoundFrame();
            case 18:
                return new TransportServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readByte());
            case 19:
                return new ActivateObjectServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readShort(), buf.readShort(), buf.readByte(), buf.readByte());
            case 20:
                return new SpawnItemServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readInt());
            case 21:
                return new SyncVarServerBoundFrame(buf.readDouble());
            case 22:
                return new SpawnBossServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readDouble());
            case 23:
                return new SpawnClassicBossServerBoundFrame(buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readInt(), buf.readDouble(), buf.readByte(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readDouble());
            case 24:
                return new LevelUpServerBoundFrame();
            case 25:
                return new ItemPickupServerBoundFrame(buf.readInt(), buf.readByte());
            case 26:
                return new ChatSystemServerBoundFrame(buf.readByte(), readString(buf));
            case 27:
                return new TestServerBoundFrame();
            case 28:
                return new ItemSwapServerBoundFrame();
            case 29:
                return readItemProcFrame(buf);
            case 30:
                return new ShrineProcServerBoundFrame(buf.readByte());
            case 31:
                return new SpawnDroneServerBoundFrame(buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort());
            case 32:
                return new SetPriceServerBoundFrame(buf.readShort(), buf.readShort());
            case 33:
                return new CreateChestServerBoundFrame();
            case 34:
                return new UpdateBuffsServerBoundFrame(buf.readDouble(), buf.readShort(), buf.readShort());
            case 35:
                return new SpawnImpmServerboundFrame(buf.readShort(), buf.readShort(), buf.readShort(), buf.readByte(), buf.readByte());
            case 36:
                return new UpdateChest4ServerBoundFrame(buf.readShort());
            case 37:
                return new EliteTeleportServerBoundFrame(buf.readShort(), buf.readShort());
            case 38:
                return new UpdateHpServerBoundFrame(buf.readShort());
            case 39:
                return new UpdateDiffServerBoundFrame(buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte());
            case 40:
                return new SpawnGhostServerBoundFrame(buf.readShort(), buf.readShort(), buf.readFloat(), buf.readFloat());
            case 41:
                return new StopTimeServerBoundFrame(buf.readShort());
            case 42:
                return new LandLizardServerBoundFrame(buf.readShort(), buf.readShort());
            case 43:
                return new DestroyObjectServerBoundFrame();
            case 44:
                return new CreateSlimeServerBoundFrame(buf.readShort(), buf.readShort(), buf.readFloat(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readByte(), buf.readShort(), buf.readDouble());
            case 45:
                return new CrewChoiceServerBoundFrame(buf.readShort());
            case 46:
                return new CreateItemServerBoundFrame(buf.readShort(), buf.readShort(), buf.readShort());
            case 47:
                return new ActivateSwitchServerBoundFrame(buf.readShort(), buf.readShort());
            default:
                return null;
        }
    }

    private String readString(ByteBuf buf) {
        String str = "";
        byte b = -1;
        while (buf.readableBytes() > 0 && b != 0) {
            b = buf.readByte();
            if (b != 0) str += (char) b;
        }
        return str;
    }

    private Object readItemProcFrame(ByteBuf buf) {
        short id = buf.readShort();
        switch (id) {
            case 1:
                return new PauldronItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readShort(), readString(buf));
            case 3:
                return new DebuffsItemProcFrame(buf.readDouble(), buf.readShort(), buf.readShort(), buf.readShort());
            case 4:
                return new MortarItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readShort());
            case 5:
                return new ScopeItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readShort());
            case 6:
                return new PlasmaChainItemProcFrame(buf.readShort(), buf.readDouble(), buf.readShort(), buf.readShort());
            case 8:
                return new BleedItemProcFrame(buf.readDouble(), buf.readShort(), buf.readShort());
            case 9:
                return new LightningItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readShort(), readString(buf));
            case 10:
                return new MissileItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readShort(), buf.readShort(), buf.readShort());
            case 11:
                return new MineItemProcFrame(buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort(), buf.readShort());
            case 12:
                return new EnemyMissileItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readShort(), buf.readShort(), buf.readShort());
            case 13:
                return new StickyItemProcFrame(buf.readShort(), buf.readShort(), buf.readShort());
            case 14:
                return new MeteorItemProcFrame(buf.readDouble(), buf.readDouble());
            case 15:
                return new GoldItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readDouble());
            case 16:
                return new BottleItemProcFrame(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readByte());
            case 17:
                return new HornItemProcFrame(buf.readShort(), buf.readDouble(), buf.readDouble(), buf.readByte(), buf.readDouble());
            default:
                return null;
        }
    }

}
