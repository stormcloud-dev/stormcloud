package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Child extends Monster {

    public Child(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(34);
        initChild();
    }

    public Child(int x, int y) {
        super(x, y);
        setZRange(34);
        initChild();
    }

    private void initChild() {
        setName("Child");
        setCanJump(true);
        setDamage(14F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
