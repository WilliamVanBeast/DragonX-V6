package dragonclient.module.impl.player;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import net.minecraft.network.play.server.SPacketChat;

public class TpaKiller extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Automatically kills players who teleport to you.");

    public TpaKiller() {
        super("TpaKiller", Category.PLAYER);
        addSettings(description);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
    }

    public void onPacketReceiveEvent(PacketReceiveEvent event) {
        if (event.getPacket() instanceof SPacketChat) {
            SPacketChat sPacketChat = (SPacketChat) event.getPacket();
            if (sPacketChat.getChatComponent().getUnformattedText().contains("tpa") || sPacketChat.getChatComponent().getUnformattedText().contains("request")) {
                mc.player.setPosition(mc.player.posX, mc.player.posY - 6, mc.player.posZ);
                mc.player.sendChatMessage("/tpa accept");
                if (mc.player.ticksExisted % 2 == 0) {
                    mc.player.setPosition(mc.player.posX, mc.player.posY + 6, mc.player.posZ);
                }
            }
        }
    }
    
}
