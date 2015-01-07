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
package io.github.stormcloud_dev.stormcloud.event.player;

import io.github.stormcloud_dev.stormcloud.object.Player;
import io.github.stormcloud_dev.stormcloud.event.Event;
import io.github.stormcloud_dev.stormcloud.frame.serverbound.PositionInfoServerBoundFrame;

public class PlayerPositionEvent extends Event {

    private Player player;
    private double x;
    private double y;
    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean jumpHeld;
    private boolean up;
    private boolean down;

    public PlayerPositionEvent(Player player, PositionInfoServerBoundFrame frame) {
        this(player, frame.getX(), frame.getY(), frame.getLeft() == 1, frame.getRight() == 1, frame.getJump() == 1, frame.getJumpHeld() == 1, frame.getUp() == 1, frame.getDown() == 1);
    }

    public PlayerPositionEvent(Player player, double x, double y, boolean left, boolean right, boolean jump, boolean jumpHeld, boolean up, boolean down) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.left = left;
        this.right = right;
        this.jump = jump;
        this.jumpHeld = jumpHeld;
        this.up = up;
        this.down = down;
    }

    public Player getPlayer() {
        return player;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isJumpHeld() {
        return jumpHeld;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

}
