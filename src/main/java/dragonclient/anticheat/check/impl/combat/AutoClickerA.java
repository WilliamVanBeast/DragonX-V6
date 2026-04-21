package dragonclient.anticheat.check.impl.combat;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.anticheat.util.PacketUtil;
import dragonclient.util.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketAnimation;

@Check.Info(name = "AutoClicker A")
public class AutoClickerA extends Check {

    
    private final TimeUtil timer = new TimeUtil();
    private int swings;

    public AutoClickerA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> packet) {
                if (packet instanceof SPacketAnimation) {
            final SPacketAnimation wrapper = (SPacketAnimation) packet;
            if (wrapper.getEntityID() == data.getPlayer().getEntityId())
                swings++;
        }
        if (PacketUtil.isRelMove(packet)) {
            if (swings > 3) {
                if (getBuffer() >= 3)
                    this.flag();

                increaseBuffer();
                timer.reset();
            } else if (timer.hasReached(1000L)) {
                resetBuffer();
                timer.reset();
            }

            swings = 0;
        }
    }
}
