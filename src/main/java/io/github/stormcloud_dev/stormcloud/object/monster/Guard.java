package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Guard extends Monster {

    public Guard(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(300);
        setXRange(400);
        initGuard();
    }

    public Guard(int x, int y) {
        super(x, y);
        setZRange(300);
        setXRange(400);
        initGuard();
    }

    private void initGuard() {
        setName("Guard");
        setCanJump(false);
        setDamage(17F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
