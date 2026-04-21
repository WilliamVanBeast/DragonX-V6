package dragonclient.anticheat.check.impl.movement;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import net.minecraft.network.Packet;


@Check.Info(name = "Step A")
public class StepA extends Check {

    public StepA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        double deltaY = (getData().getMovementTracker().getY() - getData().getMovementTracker().getLastY());

        boolean invalid = deltaY > 0.6 && getData().getMovementTracker().isLastOnGround();

        boolean exempt = System.currentTimeMillis() - getData().getLastTeleport() < 200;

        if (invalid && !exempt) {
            this.flag();
        }
    }

}
