package dragonclient.module.impl.misc;

import com.mojang.authlib.GameProfile;

import dragonclient.Dragon;
import dragonclient.anticheat.AntiCheat;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class Anticheat extends Module {

    public Anticheat() {
        super("Anticheat", Category.MISC);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
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

        /*         mc.world.playerEntities.stream().filter(e -> e != mc.thePlayer)
            .forEach(e -> AntiCheat.INSTANCE.getPlayers().put(e.getUniqueID(), new PlayerData((EntityOtherPlayerMP) e)));
     */
    }

    public void onDisable() {
        AntiCheat.INSTANCE.getPlayers().clear();
    }
}
