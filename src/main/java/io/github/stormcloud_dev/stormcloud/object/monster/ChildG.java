package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class ChildG extends Monster {

    public ChildG(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(34);
        initChildG();
    }

    public ChildG(int x, int y) {
        super(x, y);
        setZRange(34);
        initChildG();
    }

    private void initChildG() {
        setName("Parent");
        setCanJump(true);
        setDamage(15F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
