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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class RORObjectDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objects) throws Exception {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        buf.resetReaderIndex();
        while (buf.readableBytes() > 0) {
            Object obj = readNextObject(buf);

            if (obj != null) {
                objects.add(obj);
            }
        }
    }

    private Object readNextObject(ByteBuf buf) {

        if (buf.readableBytes() < 12) {
            return null;
        }

        byte[] header = new byte[8];
        buf.readBytes(8).readBytes(header);

        //Handshake header
        if (Arrays.equals(header, new byte[]{-66, -70, -2, -54, 11, -80, -83, -34})) {
            if (buf.readableBytes() >= 8) {
                buf.readBytes(8); //We read the rest of the handshake packet
                //Contains the length (inclusive the header) and 2 unknown numbers
                return new HandshakeFrame();
            } else {
                return null;
            }
        }

        /* Print packets for debugging
        int readerIndex = buf.readerIndex(), writerIndex = buf.writerIndex();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(buf.readableBytes()).readBytes(bytes);
        buf.setIndex(readerIndex, writerIndex);
        System.out.println(Arrays.toString(bytes)); */

        int length = buf.readByte();
        buf.readBytes(3);
        byte id = buf.readByte();

        switch (id) {
            case 0:
                if (length < 39) return null;
                return new PositionInfoServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte());
            case 1:
                return new SetReadyServerBoundFrame(buf.readByte());
            case 2:
                return new SetPlayerServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), readString(buf));
            case 3:
                return new AddPlayerServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), readString(buf));
            case 4:
                return new ChatPlayerServerBoundFrame(readString(buf));
            case 5:
                return new UpdatePlayerServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), readString(buf));
            case 6:
                return new KeyPlayerServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readByte());
            case 7:
                return new HealPlayerServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 8:
                return new NPCHPServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 9:
                return new MDeadServerBoundFrame();
            case 10:
                return new NPCTargetServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readByte());
            case 11:
                return new KeyMonsterServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 12:
                return new CreateObjectServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 13:
                return new CreateLevelObjectServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt());
            case 14:
                return new SyncAlarmServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt());
            case 15:
                return new SpawnClassicServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 16:
                return new LagPlayerServerBoundFrame(readString(buf));
            case 17:
                return new DisPlayerServerBoundFrame();
            case 18:
                return new TransportServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte());
            case 19:
                return new ActivateObjectServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readByte(), buf.readByte());
            case 20:
                return new SpawnItemServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt());
            case 21:
                return new SyncVarServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 22:
                return new SpawnBossServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 23:
                return new SpawnClassicBossServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 24:
                return new LevelUpServerBoundFrame();
            case 25:
                return new ItemPickupServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readInt(), buf.readByte());
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
                return new SpawnDroneServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 32:
                return new SetPriceServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 33:
                return new CreateChestServerBoundFrame();
            case 34:
                return new UpdateBuffsServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 35:
                return new SpawnImpmServerboundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readByte(), buf.readByte());
            case 36:
                return new UpdateChest4ServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 37:
                return new EliteTeleportServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 38:
                return new UpdateHpServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 39:
                return new UpdateDiffServerBoundFrame(buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte(), buf.readByte());
            case 40:
                return new SpawnGhostServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readFloat(), buf.readFloat());
            case 41:
                return new StopTimeServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 42:
                return new LandLizardServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 43:
                return new DestroyObjectServerBoundFrame();
            case 44:
                return new CreateSlimeServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readFloat(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 45:
                return new CrewChoiceServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 46:
                return new CreateItemServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 47:
                return new ActivateSwitchServerBoundFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
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
        short id = buf.order(ByteOrder.LITTLE_ENDIAN).readShort();
        switch (id) {
            case 1:
                return new PauldronItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), readString(buf));
            case 3:
                return new DebuffsItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 4:
                return new MortarItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 5:
                return new ScopeItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 6:
                return new PlasmaChainItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 8:
                return new BleedItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 9:
                return new LightningItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), readString(buf));
            case 10:
                return new MissileItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 11:
                return new MineItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 12:
                return new EnemyMissileItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 13:
                return new StickyItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readShort());
            case 14:
                return new MeteorItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 15:
                return new GoldItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            case 16:
                return new BottleItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte());
            case 17:
                return new HornItemProcFrame(buf.order(ByteOrder.LITTLE_ENDIAN).readShort(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble(), buf.readByte(), buf.order(ByteOrder.LITTLE_ENDIAN).readDouble());
            default:
                return null;
        }
    }

}
