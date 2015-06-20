package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Mush extends Monster {

    public Mush(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(24);
        initMush();
    }

    public Mush(int x, int y) {
        super(x, y);
        setZRange(24);
        initMush();
    }

    private void initMush() {
        setName("Mushrum");
        setCanJump(false);
        setDamage(17F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
