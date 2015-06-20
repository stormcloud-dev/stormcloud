package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Crab extends Monster {

    public Crab(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(24);
        initCrab();
    }

    public Crab(int x, int y) {
        super(x, y);
        setZRange(24);
        initCrab();
    }

    private void initCrab() {
        setName("Sand Crab");
        setCanJump(false);
        setDamage(17F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
