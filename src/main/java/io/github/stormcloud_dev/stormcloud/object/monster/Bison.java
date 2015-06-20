package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Bison extends Monster {

    public Bison(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(80);
        initBison();
    }

    public Bison(int x, int y) {
        super(x, y);
        setZRange(80);
        initBison();
    }

    private void initBison() {
        setName("Bison");
        setCanJump(false);
        setDamage(17F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
