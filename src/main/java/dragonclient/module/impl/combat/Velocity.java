package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Category.COMBAT);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
    }
    
        public void onPacketReceiveEvent(PacketReceiveEvent event){
        if(event.getPacket() instanceof SPacketExplosion){
            event.cancelEvent();
        }
        if(event.getPacket() instanceof SPacketEntityVelocity){
            event.cancelEvent();
        }
    }
    
}
