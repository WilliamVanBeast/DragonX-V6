package dragonclient.anticheat.check.impl.movement;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.util.PacketUtil;
import net.lax1dude.eaglercraft.vector.Vector3f;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Vec3d;

@Check.Info(name = "InvMove A")
public class InvMoveA extends Check {

    public InvMoveA(PlayerData data) {
        super(data);
    }
    
    @Override
    public void handle(Packet<?> packet) {
        if (PacketUtil.isRelMove(packet)) {

            Vec3d delta = new Vec3d(data.getDeltaX(), 0, data.getDeltaZ());
            Vec3d lastDelta = new Vec3d(data.getLastDeltaX(), 0, data.getLastDeltaZ());

            if (delta.lengthVector() >= lastDelta.lengthVector()) {

            }
        }
    }
}
