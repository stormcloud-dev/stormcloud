/*
 *   Copyright 2014 StormCloud Development Group
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.stormcloud_dev.stormcloud.object;

import io.github.stormcloud_dev.stormcloud.CrewMember;
import io.github.stormcloud_dev.stormcloud.StormCloud;
import io.github.stormcloud_dev.stormcloud.event.EventHandler;
import io.github.stormcloud_dev.stormcloud.event.InvalidEventHandlerException;
import io.github.stormcloud_dev.stormcloud.event.game.AlarmEvent;
import io.github.stormcloud_dev.stormcloud.event.game.BeginStepEvent;
import io.github.stormcloud_dev.stormcloud.event.game.EndStepEvent;
import io.github.stormcloud_dev.stormcloud.event.game.ObjectCreateEvent;
import io.github.stormcloud_dev.stormcloud.event.player.PlayerQuitEvent;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.ChatSystemClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.ClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.frame.clientbound.DisPlayerClientBoundFrame;
import io.github.stormcloud_dev.stormcloud.item.Hourglass;
import io.github.stormcloud_dev.stormcloud.item.HourglassUsed;
import io.github.stormcloud_dev.stormcloud.item.Item;
import io.github.stormcloud_dev.stormcloud.object.effect.EffectStun;
import io.netty.channel.Channel;

import java.util.Random;

import static java.lang.Math.*;
import static java.lang.System.currentTimeMillis;
import static java.util.logging.Level.SEVERE;

public class Player extends StormCloudObject {

    private Channel channel; // RoR stores a socket ID, but Java allows reference types so we can reference the channel here.

    private double mId, objectIndex;

    private String name;
    private String login;
    private CrewMember clazz;
    private long zCooldown;
    private long xCooldown;
    private long cCooldown;
    private long vCooldown;
    private long useItemCooldown;
    private long lastPing;
    private boolean ready;
    private byte lag;
    private int ghostX;
    private int ghostY;
    private boolean forceSwap;
    private boolean playerSet;
    private int forceZ;
    private int forceX;
    private int forceC;
    private int forceV;
    private int forceUse;
    private POI childPOI;
    private int useItemHeld;
    private int swapItem;
    private boolean stunned;
    private EffectStun stun;
    private float attackSpeed;
    private boolean dead;
    private int itemCount;
    private Item[] item;
    // These are from individual player scripts - I've tried to find the ones that appear in all of them
    private int canFire;
    private int zCount;
    private int mineCount;
    private int stunnerId;
    private int damage;
    private double pHMax;
    private int pVMax;
    private int hp;
    private int maxHP;
    private int maxHPBase;
    private int hpRegen;
    private int criticalChance;
    // init_default variables
    private int facingWall;
    private int disableAI;
    private int stunImmune;
    private int walkSpeedCoefficient;
    private int forceDeath = 0;
    private int elite = 0;
    private int armour = 0;
    private int pointValue = 0;
    private int pHSpeed;
    private int pVSpeed;
    private double pGravity1;
    private double pGravity2;
    private int free;
    private int activity;
    private int activityType;
    private int activityVar1;
    private int activityVar2;
    private int zSkill;
    private int xSkill;
    private int cSkill;
    private int vSkill;
    private int criticalChanceBonus;
    private int cdr;
    private int moveLeft;
    private int moveRight;
    private int moveUp;
    private int moveUpHold;
    private int lastHP;
    private int knockbackValue;
    private int lastHitX;
    private int shakeFrame;
    private int dotImmune;
    private int weatherImmune;
    private int frozen;
    private boolean invincible;
    // These are commented as "Orb stuff" - I believe they're all items & their effects
    private int fireTrail;
    private int explosiveShot;
    private int lifeSteal;
    private int lightning;
    private int goldOnHit;
    private int ghost;
    private int stompers;
    private int jetpack;
    private int poisonTrail;
    private int poisonSpeedBoost;
    private int buffCount;
    private int[][] buff;
    private int missile;
    //private int stun; // May be an object ID - already have an EffectStun variable of the same name
    private int skullRing;
    private int missileTri;
    private int twins;
    private int wormEye;
    private int healAfterKill;
    private int hpAfterKill;
    private int eggRegen;
    private int shield;
    private int maxShield;
    private int shieldCooldown;
    private int drill;
    private int embryo;
    private int mine;
    private int gp5;
    private int slowOnHit;
    private int goldGun;
    private int bleed;
    private int fireShield;
    private int trueInvincible;
    private int sp;
    private int spDur;
    private int purse;
    private int jackpot;
    private int knockback;
    private int mushroom;
    private int stillTimer;
    private int mask;
    private int scythe;
    private int gas;
    private int[] hero;
    private int mark;
    private int markTally;
    private int percentHP;
    private int plasma;
    private int plasmaCount;
    private int freeze;
    private int dagger;
    private int medKit;
    private int medkitTimer;
    private int spikeStrip;
    private int medkitCd;
    private int scoope;
    private int axe;
    private int clover;
    private int reflector;
    private int reflectorCharge;
    private int reflecting;
    private int reflectingHit;
    private int tesla;
    private int mortar;
    private int jetpackFuel;
    private int jetpackCd;
    private int poisonMine;
    private int cell;
    private int hippo;
    private double cellBonusLast;
    private int thallium;
    private int warbanner;
    private int spark;
    private int dice;
    private int armsRace;
    private int nugget;
    private int tentacle;
    private int tentacleCd;
    private int tentacleId;
    private int scepter;
    private int hourglass;
    private int hourglassCd;
    private int scarf;
    private int scarfed;
    private int lavaPillar;
    private int crowbar;
    private int sunder;
    private int lightningRing;
    private int sticky;
    private int keycard;
    private int wolfblood;
    private int blaster;
    private int horn;

    public Player(StormCloud server, double mId, double objectIndex, String login) {
        super(server, 0, 0);
        this.mId = mId;
        this.objectIndex = objectIndex;
        this.name = "Player";
        this.login = login;
        childPOI = new POI(server, getX(), getY(), this);
        setPlayerSet(true);
        try {
            server.getEventManager().addListener(this);
        } catch (InvalidEventHandlerException exception) {
            server.getLogger().log(SEVERE, "Could not register player event handlers", exception);
        }
    }

    @EventHandler
    public void onCreate(ObjectCreateEvent event) {
        if (event.getObject() == this) {
            // Start multiplayer locals
            setMId(0);
            setReady(false);
            // Since GML is dynamically typed, RoR actually uses '---' for no ping message.
            // We're storing as a long for a little more memory efficiency, so obviously we can't do that.
            // -1 is impossible, so --- can be used as the ping string where required, but -1 is stored on the player.
            setPing(-1);
            setLag((byte) 0);
            setGhostX(getX());
            setGhostY(getY());
            setForceSwap(false);
            setPlayerSet(false);
            setForceZ(0);
            setForceX(0);
            setForceC(0);
            setForceV(0);
            setForceUse(0);
            // End multiplayer locals
            setName("Player");
            setLogin("");
            getAlarmManager().getAlarm(6).setCountdown(1);
            getAlarmManager().getAlarm(11).setCountdown(30);
        }
    }

    @EventHandler
    public void onAlarm(AlarmEvent event) {
        if (event.getAlarm().getIndex() == 6) {
            getAlarmManager().getAlarm(6).setCountdown(300); // 5 seconds at 60 tps
            setUseItemHeld(getUseItemHeld() + 1);
        } else if (event.getAlarm().getIndex() == 11) {
            getAlarmManager().getAlarm(11).setCountdown(60); // 1 second at 60 tps
            setLag((byte) (getLag() + 1));
            if (getLag() > getServer().getConfiguration().getTimeout()) {
                PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(this);
                getServer().getEventManager().onEvent(playerQuitEvent);
                getServer().getChannels().writeAndFlush(new DisPlayerClientBoundFrame(getObjectIndex(), getMId()));
                getChannel().close();
                if (isPlayerSet()) {
                    getServer().getChannels().writeAndFlush(new ChatSystemClientBoundFrame(getObjectIndex(), getMId(), (byte) 0, "'" + getName() + "' has left the game!"));
                }
                destroy();
                getServer().getPlayers().values().forEach(player -> player.setReady(false));
            }
        }
    }

    @EventHandler
    public void onBeginStep(BeginStepEvent event) {
        setSwapItem(0);
    }

    @EventHandler
    public void onEndStep(EndStepEvent event) {
        switch (getClazz()) {
            case COMMANDO:
                stepCommando();
                activityCommando();
                break;
            case ENFORCER:
                stepEnforcer();
                activityEnforcer();
                break;
            case BANDIT:
                stepBandit();
                activityBandit();
                break;
            case HUNTRESS:
                stepHuntress();
                activityHuntress();
                break;
            case HAN_D:
                stepHand();
                activityHand();
                break;
            case ENGINEER:
                stepEngineer();
                activityEngineer();
                break;
            case MINER:
                stepMiner();
                activityMiner();
                break;
            case SNIPER:
                stepSniper();
                activitySniper();
                break;
            case ACRID:
                stepAcrid();
                activityAcrid();
                break;
            case MERCENARY:
                stepMercenary();
                activityMercenary();
                break;
            case LOADER:
                stepLoader();
                activityLoader();
                break;
            case CHEF:
                stepChef();
                activityChef();
                break;
        }
        setForceSwap(false);
    }

    private void stepDefault() {

    }

    private int stepPlayer() {
        boolean tMoveChange = false;
        boolean tSkillChange = false;
        boolean tUsedItem = false;
        if (getAlarmManager().getAlarm(7).getCountdown() != -1 && !isStunned()) {
            setStunned(true);
            stun = new EffectStun(getServer(), getX(), getY());
            stun.setParent(this);
        }
        maxHP = (int) min(ceil(maxHPBase * percentHP), 9999);
        Random random = new Random();
        if (scarf != 0 && random.nextInt(100) < min(10 + (scarf - 1) * 3, 25)) {
            invincible = true;
            scarfed = 1;
        } else {
            scarfed = 0;
        }
        if (hourglass != 0 && !dead) {
            if (hourglassCd == 1) {
                for (int i = 0; i <= itemCount; i++) {
                    if (item[i] instanceof HourglassUsed) {
                        item[i] = new Hourglass();
                        break;
                    }
                }
            }
            hourglassCd = max(hourglassCd - 1, 0);
        }
        if (cell != 0) {
            attackSpeed -= cellBonusLast;
            attackSpeed += min(0.9, (0.4 + (cell - 1) * 0.2)) * (1 - (hp / maxHP));
            cellBonusLast = min(0.9, (0.4 + (cell - 1) * 0.2)) * (1 - (hp / maxHP));
        }
        if (tentacle != 0) {
            if (!getRoom().containsInstanceOf(Tentacle.class))
        }
    }

    private void stepCommando() {
        int tSkillChange = 0;
        int tzs = 0;
        int txs = 0;
        int tcs = 0;
        int tvs = 0;
        int tui = 0;
        stepDefault();
        tui = stepPlayer();
        if ((getActivity() == 0 && getAlarmManager().getAlarm(2).getCountdown() == -1 && getZCooldown() == 1) || getForceZ() == 1) {
            getAlarmManager().getAlarm(2).setCountdown(round(((float) getZCooldown() / getAttackSpeed())));
            setActivityVar1(0);
            setActivity(1);
            setForceZ(0);
            tzs = 1;
        }
        if ((getActivity() == 0 && getAlarmManager().getAlarm(3).getCountdown() == -1 && getXCooldown() == 1) || getForceX() == 1) {
            getAlarmManager().getAlarm(3).setCountdown(round(((float) getXCooldown() * (1 - cdr))));
        }
    }

    private void activityCommando() {

    }

    private void stepEnforcer() {

    }

    private void activityEnforcer() {

    }

    private void stepBandit() {

    }

    private void activityBandit() {

    }

    private void stepHuntress() {

    }

    private void activityHuntress() {

    }

    private void stepHand() {

    }

    private void activityHand() {

    }

    private void stepEngineer() {

    }

    private void activityEngineer() {

    }

    private void stepMiner() {

    }

    private void activityMiner() {

    }

    private void stepSniper() {

    }

    private void activitySniper() {

    }

    private void stepAcrid() {

    }

    private void activityAcrid() {

    }

    private void stepMercenary() {

    }

    private void activityMercenary() {

    }

    private void stepLoader() {

    }

    private void activityLoader() {

    }

    private void stepChef() {

    }

    private void activityChef() {

    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void sendFrame(ClientBoundFrame frame) {
        getChannel().writeAndFlush(frame);
    }

    public double getMId() {
        return mId;
    }

    public void setMId(double mId) {
        this.mId = mId;
    }

    public double getObjectIndex() {
        return objectIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public CrewMember getClazz() {
        return clazz;
    }

    public int getClazzId() { // Utility for sending clientbound frames
        return clazz != null ? clazz.getId() : -1;
    }

    public void setClazz(CrewMember clazz) {
        this.clazz = clazz;
    }

    public void setClazz(int clazz) {
        this.clazz = (clazz != -1?CrewMember.values()[clazz]:null);
    }

    public long getZCooldown() {
        return max(zCooldown - currentTimeMillis(), 0);
    }

    public void setZCooldown(long millis) {
        zCooldown = currentTimeMillis() + millis;
    }

    public long getXCooldown() {
        return max(xCooldown - currentTimeMillis(), 0);
    }

    public void setXCooldown(long millis) {
        xCooldown = currentTimeMillis() + millis;
    }

    public long getCCooldown() {
        return max(cCooldown - currentTimeMillis(), 0);
    }

    public void setCCooldown(long millis) {
        cCooldown = currentTimeMillis() + millis;
    }

    public long getVCooldown() {
        return max(vCooldown - currentTimeMillis(), 0);
    }

    public void setVCooldown(long millis) {
        vCooldown = currentTimeMillis() + millis;
    }

    public long getUseItemCooldown() {
        return max(useItemCooldown - currentTimeMillis(), 0);
    }

    public void setUseItemCooldown(long millis) {
        useItemCooldown = currentTimeMillis() + millis;
    }

    public long getPing() {
        return max(currentTimeMillis() - lastPing, 0);
    }

    public void setPing(long millis) {
        lastPing = currentTimeMillis() + millis;
    }

    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getGhostX() {
        return ghostX;
    }

    public void setGhostX(int ghostX) {
        this.ghostX = ghostX;
    }

    public int getGhostY() {
        return ghostY;
    }

    public void setGhostY(int ghostY) {
        this.ghostY = ghostY;
    }

    public byte getLag() {
        return lag;
    }

    public void setLag(byte lag) {
        this.lag = lag;
    }

    public boolean isForceSwap() {
        return forceSwap;
    }

    public void setForceSwap(boolean forceSwap) {
        this.forceSwap = forceSwap;
    }

    public boolean isPlayerSet() {
        return playerSet;
    }

    public void setPlayerSet(boolean playerSet) {
        this.playerSet = playerSet;
    }

    public int getForceZ() {
        return forceZ;
    }

    public void setForceZ(int forceZ) {
        this.forceZ = forceZ;
    }

    public int getForceX() {
        return forceX;
    }

    public void setForceX(int forceX) {
        this.forceX = forceX;
    }

    public int getForceC() {
        return forceC;
    }

    public void setForceC(int forceC) {
        this.forceC = forceC;
    }

    public int getForceV() {
        return forceV;
    }

    public void setForceV(int forceV) {
        this.forceV = forceV;
    }

    public int getForceUse() {
        return forceUse;
    }

    public void setForceUse(int forceUse) {
        this.forceUse = forceUse;
    }

    public int getUseItemHeld() {
        return useItemHeld;
    }

    public void setUseItemHeld(int useItemHeld) {
        this.useItemHeld = useItemHeld;
    }

    public int getSwapItem() {
        return swapItem;
    }

    public void setSwapItem(int swapitem) {
        this.swapItem = swapitem;
    }

    public int getActivityVar1() {
        return activityVar1;
    }

    public void setActivityVar1(int activityVar1) {
        this.activityVar1 = activityVar1;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public boolean isStunned() {
        return stunned;
    }

    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

}
