package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Naut extends Monster {

    public Naut(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(80);
        initNaut();
    }

    public Naut(int x, int y) {
        super(x, y);
        setZRange(80);
        initNaut();
    }

    private void initNaut() {
        setName("Whorl");
        setCanJump(true);
        setDamage(17F * getEnemyBuff() * getDamageCoefficient());
    }
}
