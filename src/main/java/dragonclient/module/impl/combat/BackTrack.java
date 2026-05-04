package dragonclient.module.impl.combat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import dragonclient.Dragon;
import dragonclient.event.Events.AttackEvent;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.util.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Vec3d;

public class BackTrack extends Module {
    
    public static EntityLivingBase target;
    public static List<Vec3d> pastPositions = new ArrayList<>();
    public static List<Vec3d> forwardPositions = new ArrayList<>();
    public static List<Vec3d> positions = new ArrayList<>();
    private final Deque<Packet<?>> packets = new ArrayDeque<>();

    private final IntegerSetting amount = new IntegerSetting("Amount", 20, 1, 100);
    private final IntegerSetting forward = new IntegerSetting("Forward", 20, 1, 100);

    private int ticks;

    public BackTrack() {
        super("BackTrack", Category.COMBAT);
        addSettings(amount, forward);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
        Dragon.eventManager.registerListener(this, AttackEvent.class);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);

    }

      public void onPreMotionEvent(final PreMotionEvent event) {
        if (mc.player.ticksExisted < 5) {
            onDisable();
            return;
        }

        if (target == null) return;

        pastPositions.add(new Vec3d(target.posX, target.posY, target.posZ));

        final double deltaX = (target.posX - target.lastTickPosX) * 2;
        final double deltaZ = (target.posZ - target.lastTickPosZ) * 2;

        forwardPositions.clear();
        int i = 0;
        while (forward.get() > forwardPositions.size()) {
            i++;
            forwardPositions.add(new Vec3d(target.posX + deltaX * i, target.posY, target.posZ + deltaZ * i));
        }

        while (pastPositions.size() > (int) amount.get()) {
            pastPositions.remove(0);
        }

        positions.clear();
        positions.addAll(forwardPositions);
        positions.addAll(pastPositions);

        ticks++;
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (target != null && !positions.isEmpty()) RenderUtil.renderBreadCrumbs(positions);
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityPlayer) target = (EntityLivingBase) event.getTarget();
        ticks = 0;
    }

    @Override
    protected void onDisable() {
        target = null;
        positions.clear();
        pastPositions.clear();
        forwardPositions.clear();
        packets.clear();
    }


}
