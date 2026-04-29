package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {

    private DescriptionSetting description = new DescriptionSetting("Description", "Prevents knockback and explosion velocity.");
    public Velocity() {
        super("Velocity", Category.COMBAT);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
        addSettings(description);
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
