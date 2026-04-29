package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.AttackEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.util.PacketUtil;
import net.minecraft.network.play.client.CPacketUseEntity;

public class ComboOneHit extends Module {

    private DescriptionSetting description = new DescriptionSetting("Description", "Sends a lot of attack packets to the server when you hit an entity.");  
    public final IntegerSetting packets = new IntegerSetting("Packets", 50, 1, 1000);

    public ComboOneHit() {
        super("ComboOneHit", Category.COMBAT);
        Dragon.eventManager.registerListener(this, AttackEvent.class);
        addSettings(packets, description);
    }
    
    public void onAttackEvent(final AttackEvent event) {
        for (int i = 0; i < (int) packets.get(); i++) {
            PacketUtil.sendPacketNoEvent(new CPacketUseEntity(event.getTarget()));
        }
    }
}
