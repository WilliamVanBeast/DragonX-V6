package dragonclient.anticheat.check.impl.movement;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.util.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;

@Check.Info(name = "Speed B")
public final class SpeedB extends Check {

    private double deltaXOffGround, deltaZOffGround;

    public SpeedB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && getData().isOnGround() && !getData().isLastOnGround() && !getData().getPlayer().isOnLadder() && getData().getTicksSinceLastVelocity() > 20 && getData().getPlayer().ticksExisted > 100) {
            final SPacketEntity wrapper = ((SPacketEntity) packet);
            if (wrapper.entityId != getData().getPlayer().getEntityId()) return;

            final double speed = Math.abs(getData().getDeltaX()) + Math.abs(getData().getDeltaZ());

            if (speed <= 0.375) return;

            final double groundX = getData().getGroundX();
            final double groundZ = getData().getGroundZ();
            final double lastGroundX = getData().getLastGroundX();
            final double lastGroundZ = getData().getLastGroundZ();

            final double direction = Math.atan2((lastGroundX - (lastGroundX + deltaXOffGround)), (lastGroundZ - (lastGroundZ + deltaZOffGround))) * 180 / Math.PI;
            final double groundDirection = Math.atan2((lastGroundX - groundX), (lastGroundZ - groundZ)) * 180 / Math.PI;
            final double difference = Math.abs(groundDirection - direction);

            if (difference > 30 && difference < 360 - 30) {
                increaseBuffer();
                if (buffer >= 6) {
                    this.flag();
                }
            } else
                reduceBuffer(0.05);

        } else if (getData().isLastOnGround()) {
            deltaXOffGround = getData().getDeltaX();
            deltaZOffGround = getData().getDeltaZ();
        }
    }
}