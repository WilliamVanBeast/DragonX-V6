package dragonclient.anticheat.check.impl.movement;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;

@Check.Info(name = "Speed A")
public class SpeedA extends Check {

    public SpeedA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        double maxSpeed = getData().getPlayer().hurtTime > 0 ? 2.0D : 1.0D;

        if(getData().getPlayer().isPotionActive(Potion.getPotionById(1))) {
            maxSpeed *= 1.2D;
        }

        if(getData().getPlayer().isPotionActive(Potion.getPotionById(2))) {
            maxSpeed *= 0.85D;
        }

        boolean invalid = getData().getMovementTracker().getSpeed() > maxSpeed;

        boolean exempt = System.currentTimeMillis() - getData().getLastTeleport() < 200;

        if(invalid && !exempt) {
            if(increaseBuffer() > 3) {
                this.flag();
            }
        }
        reduceBuffer(0.05);
    }
    
}
