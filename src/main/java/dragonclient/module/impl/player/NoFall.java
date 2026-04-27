package dragonclient.module.impl.player;

import org.apache.commons.lang3.RandomUtils;

import dragonclient.Dragon;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.PacketUtil;
import dragonclient.util.PlayerUtil;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {

        private final ListSetting mode = new ListSetting("Mode", new String[]{"Ground Spoof",
            "No Ground", "Tick", "Packet", "Collision", "Collision Silent", "Verus", "Math Ground", "Less Fall", "Vulcan", "Artemis 2", "Less Fall"}, "Ground Spoof");

    private final BooleanSetting offset = new BooleanSetting("Offset", true);

    public NoFall() {
        super("NoFall", Category.PLAYER);
        addSettings(mode, offset);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
    }

    
    private float lastTickFallDist, fallDist;
    private int offGroundTicks, tick;
    private boolean bool;

    public void onEnable() {
        tick = 1;
    }

    public void onPreMotionEvent(PreMotionEvent event) {
                    if (mc.player.onGround)
            offGroundTicks = 0;
        else
            offGroundTicks++;

        if (mc.player.fallDistance == 0)
            fallDist = 0;

        fallDist += mc.player.fallDistance - lastTickFallDist;
        lastTickFallDist = mc.player.fallDistance;

        final boolean isBlockUnder = PlayerUtil.isBlockUnder();
        if (/*this.getModule(HighJump.class).isEnabled() ||*/ !isBlockUnder) return;

        switch (mode.get()) {
            case "Ground Spoof":
                if (fallDist > 2) {
                    event.setGround(true);
                    fallDist = 0;
                }
                break;

            case "Packet":
                if (fallDist > 2) {
                    PacketUtil.sendPacket(new CPacketPlayer(true));
                    fallDist = 0;
                }
                break;

            case "Tick":
                if (fallDist > 2 && mc.player.ticksExisted % 3 == 0) {
                    event.setGround(true);
                    fallDist = 0;
                }
                break;

            case "Vulcan":
                double mathGround = Math.round(event.getY() / 0.015625) * 0.015625;

                if (fallDist > 1.3 && mc.player.ticksExisted % 15 == 0) {
                    mc.player.setPosition(mc.player.posX, mathGround, mc.player.posZ);
                    event.setY(mathGround);

                    mathGround = Math.round(event.getY() / 0.015625) * 0.015625;
                    if (Math.abs(mathGround - event.getY()) < 0.01) {
                        if (mc.player.motionY < -0.4) mc.player.motionY = -0.4;

                        PacketUtil.sendPacket(new CPacketPlayer(true));
                        mc.timer.field_194147_b = 0.9f;
                    }
                } else if (mc.timer.field_194147_b == 0.9f) {
                    mc.timer.field_194147_b = 1;
                }
                break;

            case "Collision":
                if (fallDist > 3) {
                    mc.player.motionY = -(mc.player.posY - (mc.player.posY - (mc.player.posY % (1.0 / 64.0))));
                    event.setGround(true);

                    fallDist = 0;
                }
                break;

            case "Collision Silent":
                if (fallDist > 2) {
                    PacketUtil.sendPacket(new CPacketPlayer.PositionRotation(
                            (mc.player.posX + mc.player.lastTickPosX) / 2,
                            (mc.player.posY - (mc.player.posY % (1 / 64.0))),
                            (mc.player.posZ + mc.player.lastTickPosZ) / 2,
                            mc.player.rotationYaw,
                            mc.player.rotationPitch,
                            true)
                    );
                    fallDist = 0;
                }
                break;

            case "Verus":
                if (fallDist > 3 && fallDist < 12) {
                    if (mc.player.posY % (1.0F / 64.0F) < 0.005 && fallDist > 1.5)
                        event.setGround(true);
                }
                break;

            case "Artemis 2":
                if (mc.player.ticksExisted % 8 == 0 && fallDist > 2)
                    event.setGround(true);
                break;

            case "No Ground":
                if (mc.player.onGround && offset.get())
                    event.setY(event.getY() + RandomUtils.nextDouble(0.0001, 0.001));
                event.setGround(false);
                break;

            case "Hypixel":
                if (!mc.player.onGround && mc.player.fallDistance - (tick * 2.8D) >= 0.0D) {
                    if (mc.player.ticksExisted > 150)
                        event.setGround(true);
                    tick++;
                } else if (mc.player.onGround) {
                    tick = 1;
                }
                break;

            case "Math Ground":
                if (mc.player.posY % (1.0F / 64.0F) < 0.005 && fallDist > 1.5)
                    event.setGround(true);
                break;

            case "Less Fall":
                if (mc.player.posY % (1.0F / 64.0F) < 0.005 && fallDist > 1.5 && bool) {
                    event.setGround(true);
                    bool = false;
                }

                if (mc.player.onGround)
                    bool = true;
                break;
        }
    }
    
}
