package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Slime extends Monster {

    public Slime(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(14);
        initSlime();
    }

    public Slime(int x, int y) {
        super(x, y);
        setZRange(14);
        initSlime();
    }

    private void initSlime() {
        setName("Gup");
        setCanJump(true);
        setDamage(27F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
