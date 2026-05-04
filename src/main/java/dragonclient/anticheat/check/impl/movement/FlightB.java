package dragonclient.anticheat.check.impl.movement;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.util.MovementUtils;
import dragonclient.util.MovementUtil;
import net.minecraft.network.Packet;

@Check.Info(name = "Flight B")
public class FlightB extends Check {
    public FlightB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> packet) {
        if( data.getPlayer().getAir() > 20 && data.getPlayer().motionY == 0 && MovementUtils.isMoving(data.getPlayer())) {
            this.flag();
        }
    }
    
}
