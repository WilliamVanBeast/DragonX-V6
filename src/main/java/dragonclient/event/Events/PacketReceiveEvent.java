package dragonclient.event.Events;

 import dragonclient.event.CancellableEvent;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends CancellableEvent {
    public final Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }
}
