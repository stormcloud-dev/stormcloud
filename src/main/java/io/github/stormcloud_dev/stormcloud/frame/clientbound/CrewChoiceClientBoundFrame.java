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

import io.github.stormcloud_dev.stormcloud.CrewMember;
import io.netty.buffer.ByteBuf;

public class CrewChoiceClientBoundFrame extends ClientBoundFrame {

    private short crewMember;

    public CrewChoiceClientBoundFrame(double objectIndex, double multiplayerId, CrewMember crewMember) {
        super((byte) 45, objectIndex, multiplayerId);
        this.crewMember = crewMember.getId();
    }

    public CrewChoiceClientBoundFrame(double objectIndex, double multiplayerId, short crewMember) {
        super((byte) 45, objectIndex, multiplayerId);
        this.crewMember = crewMember;
    }

    @Override
    public int getLength() {
        return 19;
    }

    public short getCrewMember() {
        return crewMember;
    }

    @Override
    public void writeData(ByteBuf buf) {
        super.writeData(buf);
        buf.writeShort(getCrewMember());
    }

}
