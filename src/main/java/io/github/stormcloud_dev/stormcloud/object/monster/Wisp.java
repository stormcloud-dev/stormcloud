package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Wisp extends Monster {

    public Wisp(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(60);
        initWisp();
    }

    public Wisp(int x, int y) {
        super(x, y);
        setZRange(60);
        initWisp();
    }

    private void initWisp() {
        setName("Wisp");
        setCanJump(false);
        setDamage(17F * getEnemyBuff() * getDamageCoefficient());
    }
}
