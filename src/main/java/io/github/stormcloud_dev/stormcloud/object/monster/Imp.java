package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Imp extends Monster {

    public Imp(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(40);
        setXRange(200);
        initImp();
    }

    public Imp(int x, int y) {
        super(x, y);
        setZRange(40);
        setXRange(200);
        initImp();
    }

    private void initImp() {
        setName("Black Imp");
        setCanJump(true);
        setDamage(13F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
