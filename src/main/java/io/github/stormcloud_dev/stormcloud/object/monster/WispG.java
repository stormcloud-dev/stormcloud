package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class WispG extends Monster {

    public WispG(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(260);
        initWispG();
    }

    public WispG(int x, int y) {
        super(x, y);
        setZRange(260);
        initWispG();
    }

    private void initWispG() {
        setName("Greater Wisp");
        setCanJump(false);
        setDamage(23F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
