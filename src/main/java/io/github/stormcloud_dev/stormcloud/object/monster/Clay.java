package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Clay extends Monster {

    public Clay(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(40);
        setXRange(200);
        initClay();
    }

    public Clay(int x, int y) {
        super(x, y);
        setZRange(40);
        setXRange(200);
        initClay();
    }

    private void initClay() {
        setName("Clay Man");
        setCanJump(true);
        setDamage(11F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
