/*
 * Copyright 2014 StormCloud Development Group
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
package io.github.stormcloud_dev.stormcloud.object;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import java.awt.*;

public class StormCloudObject {

    private int x;
    private int y;
    private String name;
    private boolean locked;
    private String code;
    private double scaleX;
    private double scaleY;
    private Color colour;
    private double rotation;
    private Body body;
    private BodyType dynamic;

    public StormCloudObject(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation, Body body, BodyType dynamic) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.locked = locked;
        this.code = code;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.colour = new Color((int) ((colour & 16711680) >> 16 | (colour & 65280) | (colour & 255) << 16));
        this.rotation = rotation;
        this.dynamic = dynamic;
    }

    public StormCloudObject(int x, int y) {
        this(x, y, "", false, "", 1D, 1D, 4294967295L, 0D, null, BodyType.DYNAMIC);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public Color getColour() {
        return colour;
    }

    public int getGMColour() {
        int rgb = getColour().getRGB();
        return (rgb & 16711680) >> 16 | (rgb & 65280) | (rgb & 255) << 16;
    }

    public void setGMColour(int gmColour) {
        this.colour = new Color((gmColour & 16711680) >> 16 | (gmColour & 65280) | (gmColour & 255) << 16);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public BodyType getDynamic() { return dynamic; }

    public void setDynamic(BodyType btype) { this.dynamic = btype; }

    public Body getBody() { return body; }

    public void setBody(Body body) { this.body = body; }
}
