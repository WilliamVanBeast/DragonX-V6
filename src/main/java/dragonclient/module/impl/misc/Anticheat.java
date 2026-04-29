package dragonclient.module.impl.misc;

import java.util.List;


import dragonclient.Dragon;
import dragonclient.anticheat.AntiCheat;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import net.minecraft.entity.player.EntityPlayer;

public class Anticheat extends Module {

    private DescriptionSetting description = new DescriptionSetting("Description", "Detects other hackers");

    public Anticheat() {
        super("Anticheat", Category.MISC);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
        addSettings(description);
    }
    

        public final void onUpdateEvent(UpdateEvent updateEvent) {
        if(mc.player != null) {
            AntiCheat.INSTANCE.handlePlayers();
        }
    }

        public void onPacketReceiveEvent(PacketReceiveEvent packetEvent) {
        if(mc.player != null) {
            AntiCheat.INSTANCE.getPlayers().values().stream().filter(d -> d.getTicksExisted() > 80).forEach(d -> d.handle(packetEvent.getPacket()));
        }
    }

       public void onEnable() {
        AntiCheat.INSTANCE.getPlayers().clear();


        List<EntityPlayer> list = mc.world.playerEntities;

        for(int i = 0, j = list.size(); i < j; ++i) {
             EntityPlayer player = list.get(i);
        if(player != mc.player) {
             AntiCheat.INSTANCE.getPlayers().put(player.getUniqueID(), new PlayerData(player));
    }
}
    }

    public void onDisable() {
        AntiCheat.INSTANCE.getPlayers().clear();
    }
}
