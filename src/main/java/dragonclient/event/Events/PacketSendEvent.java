package dragonclient.event.Events;

 import dragonclient.event.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketSendEvent extends CancellableEvent {
    public final Packet<?> packet;

    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }

        public Packet getPacket() {
        return packet;
    }
}
