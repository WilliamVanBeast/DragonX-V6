package dragonclient.module.impl.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Queue;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketSendEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.util.PacketUtil;

import java.util.ArrayDeque;

public final class Blink extends Module {
    private final BooleanSetting allPackets = new BooleanSetting("All Packets", true);
    private final BooleanSetting showPlayer = new BooleanSetting("Show Player", false);

    private EntityOtherPlayerMP blinkEntity;
    Queue<Packet<?>> packets = new ArrayDeque<>();

    public Blink() {
        super("Blink", Category.PLAYER);
        Dragon.eventManager.registerListener(this, PacketSendEvent.class);
        addSettings(allPackets, showPlayer);
    }

    @Override
    public void onPacketSendEvent(PacketSendEvent event) {
        if (mc.player == null || mc.player.isDead || mc.isSingleplayer() || mc.player.ticksExisted < 50) {
            packets.clear();
            return;
        }

        if (allPackets.get()) {
            packets.add(event.packet);
            event.cancelEvent();
        } else {
            if (event.packet instanceof CPacketPlayer) {
                packets.add(event.packet);
                event.cancelEvent();
            }
        }
    }

    @Override
    protected void onEnable() {
        if (showPlayer.get()) {
            blinkEntity = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
            blinkEntity.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ,
                    mc.player.rotationYaw, mc.player.rotationPitch);
            blinkEntity.rotationYawHead = mc.player.rotationYawHead;
            blinkEntity.setSprinting(mc.player.isSprinting());
            blinkEntity.setInvisible(mc.player.isInvisible());
            blinkEntity.setSneaking(mc.player.isSneaking());

            mc.world.addEntityToWorld(blinkEntity.getEntityId(), blinkEntity);
        }
    }

    @Override
    protected void onDisable() {
        packets.forEach(PacketUtil::sendPacketNoEventhuh);
        packets.clear();

        if (showPlayer.get()) {
            if (blinkEntity != null) {
                mc.world.removeEntityFromWorld(blinkEntity.getEntityId());
            }
        }
    }

}
