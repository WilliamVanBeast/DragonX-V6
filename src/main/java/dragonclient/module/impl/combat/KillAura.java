package dragonclient.module.impl.combat;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import dragonclient.Dragon;
import dragonclient.event.Events.JumpEvent;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.event.Events.TickEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.DoubleSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.PacketUtil;
import dragonclient.util.RenderUtil;
import dragonclient.util.RotationUtil;
import dragonclient.util.TimeUtil;
import dragonclient.util.killaura.RotationsUtil;
import dragonclient.util.killaura.StopWatch;
import dragonclient.util.killaura.TargetingUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class KillAura extends Module {

     /*   
    public ModeValue rotations = new ModeValue("Rotations", this)
            .add(new Watermark.StringMode("Normal", this))
            .add(new Watermark.StringMode("Disabled", this))
            .setDefault("Normal");
    public ModeValue autoblock = new ModeValue("Auto Block", this)
            .add(new Watermark.StringMode("Fake", this))
            .add(new Watermark.StringMode("Disabled", this))
            .setDefault("Fake");
*/ 
    private DoubleSetting autoBlockRange = new DoubleSetting("Block Range", 3.0, 1.0, 6.0);
    private DoubleSetting rotationRange = new DoubleSetting("Rotate Range", 3.0, 1.0, 6.0);
    private DoubleSetting range = new DoubleSetting("Range", 3.0, 1.0, 6.0);
    private IntegerSetting minCps = new IntegerSetting("Min Cps", 5, 1, 30);
    private IntegerSetting maxCps = new IntegerSetting("Max Cps", 10, 1, 30);
    private IntegerSetting missChance = new IntegerSetting("Miss Chance", 5, 0, 100);
    public static ListSetting sortMode = new ListSetting("Sort Mode", new String[]{
        "Distance",
        "Hurt",
        "Health"
    }, "Distance");

    private ListSetting rotations = new ListSetting("Rotations", new String[]{
        "Disabled"
    }, "Disabled");

    private ListSetting autoblock = new ListSetting("Auto Block", new String[]{
        "Fake",
        "Disabled"
    }, "Fake");

    private BooleanSetting raytrace = new BooleanSetting("RayTrace", true);
    private BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);


    private RotationsUtil fixedRotations;
    public static EntityLivingBase target = null;
    public boolean shouldAttack;
    public StopWatch stopwatch = new StopWatch();
    public float cps;
    public boolean block;
    public KillAura() {
        super("KillAura", Category.COMBAT);
        addSettings(autoBlockRange, rotationRange, range, minCps, maxCps, missChance, sortMode, rotations, autoblock, raytrace, keepSprint);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
        Dragon.eventManager.registerListener(this, TickEvent.class);
    }

      public void onDisable() {
        super.onDisable();
        target = null;
        shouldAttack = false;
        block = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        target = null;
        block = false;
        fixedRotations = new RotationsUtil(mc.player.rotationYaw, mc.player.rotationPitch);
    }

    public void onTickEvent(TickEvent event) {
        List<Entity> entities = TargetingUtil.collectTargets();
        target = TargetingUtil.getCloset(entities, 5);


        boolean miss = (new SecureRandom().nextFloat() * 100) > missChance.get().intValue();
        cps = (float) (minCps.get().intValue() + new Random().nextInt((maxCps.get().intValue() - minCps.get().intValue()) + 1) + Math.random() * 0.2f);

        if (target == null) {
            return;
        }

        if (!autoblock.get().equals("Disabled")) {
            performAutoblock();
        }

        shouldAttack = true;

        if (raytrace.get()) {
            RayTraceResult movingObjectPosition = mc.world.rayTraceBlocks(new Vec3d(mc.player.getPosition())
                    .add(new Vec3d(0, mc.player.getEyeHeight(), 0)), new Vec3d(target.getPosition()));

            RayTraceResult movingObjectPosition2 = mc.world.rayTraceBlocks(new Vec3d(mc.player.getPosition())
                    .add(new Vec3d(0, mc.player.getEyeHeight(), 0)), new Vec3d(target.getPosition())
                    .add(new Vec3d(0, target.getEyeHeight(), 0)));

            if (movingObjectPosition != null && movingObjectPosition2 != null) {
                shouldAttack = false;
            }
        }


        if (shouldAttack && target.getDistanceToEntity(mc.player) <= range.get().doubleValue()) {
            if(this.stopwatch.elapsed((long) (1000 / cps))){
            attack(target);
            stopwatch.reset();
        }
    }
    }

    public void onPreMotionEvent(PreMotionEvent e) {
        if (target != null) {
            e.setYaw(fixedRotations.getYaw());
            e.setPitch(fixedRotations.getPitch());
            mc.player.renderYawOffset = fixedRotations.getYaw();
            mc.player.rotationYawHead = fixedRotations.getYaw();
        }
    }

    public void attack(EntityLivingBase ent) {
        mc.player.swingArm(EnumHand.MAIN_HAND);

        if (keepSprint.get().booleanValue()) {
            if (mc.player.onGround) {
                mc.playerController.attackEntity(mc.player, ent);
            } else {
                PacketUtil.sendPacket(new CPacketUseEntity(ent, EnumHand.MAIN_HAND));
            }
        } else {
            mc.playerController.attackEntity(mc.player, ent);
        }
    }


    public void performAutoblock() {
        if (target != null) {
            if (target.getDistanceToEntity(mc.player) > autoBlockRange.get().intValue()) return;

            block = true;
        } else if (block) {
            block = false;
        }
    }

}
