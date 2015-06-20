package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Spitter extends Monster {

    public Spitter(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(350);
        initSpitter();
    }

    public Spitter(int x, int y) {
        super(x, y);
        setZRange(350);
        initSpitter();
    }

    private void initSpitter() {
        setName("Spitter");
        setCanJump(false);
        setDamage(19F * getEnemyBuff() * getDamageCoefficient());
    }
}
