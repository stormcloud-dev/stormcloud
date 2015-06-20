package io.github.stormcloud_dev.stormcloud.object.monster;

/**
 * Created by Deliagwath on 6/20/2015.
 */
public class Spider extends Monster {

    public Spider(int x, int y, String name, boolean locked, String code, double scaleX, double scaleY, long colour, double rotation) {
        super(x, y, name, locked, code, scaleX, scaleY, colour, rotation);
        setZRange(150);
        initSpider();
    }

    public Spider(int x, int y) {
        super(x, y);
        setZRange(150);
        initSpider();
    }

    private void initSpider() {
        setName("Spider");
        setCanJump(true);
        setDamage(9F * (float) Math.pow(getEnemyBuff(), getDamageCoefficient()));
    }
}
