package dragonclient.anticheat.check.impl.other;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import net.minecraft.network.Packet;

@Check.Info(name = "Invalid A")
public class InvalidA extends Check {

    public InvalidA(PlayerData data) {
        super(data);
    }
    @Override
    public void handle(final Packet<?> packet) {
        if (Math.abs(getData().getPlayer().rotationPitch) > 90)
            this.flag();
    }

}