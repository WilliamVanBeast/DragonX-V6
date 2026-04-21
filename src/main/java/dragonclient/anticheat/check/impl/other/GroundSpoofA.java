package dragonclient.anticheat.check.impl.other;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.util.PacketUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;

@Check.Info(name = "GroundSpoof A")
public final class GroundSpoofA extends Check {

    public GroundSpoofA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet<?> packet) {
        if (PacketUtil.isRelMove(packet)) { // Disables the check on Hypixel since ground states are hidden on there.
            final SPacketEntity wrapper = ((SPacketEntity) packet);

            if (data.getPlayer().getEntityId() != wrapper.entityId) return;

            final double x = data.getX();
            final double y = data.getY();
            final double z = data.getZ();

            final boolean server = !(data.getBlock(x - 0.5, y - 0.52, z - 0.5) instanceof BlockAir)
                    || !(data.getBlock(x + 0.5, y - 0.52, z - 0.5) instanceof BlockAir)
                    || !(data.getBlock(x + 0.5, y - 0.52, z + 0.5) instanceof BlockAir)
                    || !(data.getBlock(x - 0.5, y - 0.52, z + 0.5) instanceof BlockAir);

            final boolean ground = wrapper.onGround && !server && y % 0.5 != 0.0;

            if (ground) {
                if (increaseBuffer() > 2) {
                    this.flag();
                }
            } else {
                reduceBuffer(0.1);
            }
        }
    }
}