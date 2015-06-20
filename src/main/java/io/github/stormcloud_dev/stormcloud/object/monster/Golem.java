package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Golem extends Monster {

    public Golem(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(24);
        initGolem();
    }

    public Golem(int x, int y) {
        super(x, y);
        setZRange(24);
        initGolem();
    }

    private void initGolem() {
        setName("Golem");
        setCanJump(false);
        setDamage(17F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
