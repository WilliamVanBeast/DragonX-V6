package dragonclient.anticheat.check.impl.movement;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.util.MovementUtils;
import dragonclient.util.MovementUtil;
import net.minecraft.network.Packet;

@Check.Info(name = "Flight A")
public class FlightA extends Check {
    public FlightA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> packet) {
        if( !data.getPlayer().onGround && data.getPlayer().motionY == 0 && MovementUtils.isMoving(data.getPlayer())) {
            this.flag();
        }
    }
    
}
