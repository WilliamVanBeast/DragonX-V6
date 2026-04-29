package dragonclient.module.impl.misc;

import java.util.ArrayDeque;
import java.util.Queue;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketSendEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;
 
public class XCarry extends Module {

    private DescriptionSetting description = new DescriptionSetting("Description", "Allows you to carry items in your crafting slots.");
    Queue<Packet<?>> packets = new ArrayDeque<>();

    public XCarry() {
        super("XCarry", Category.MISC);
        Dragon.eventManager.registerListener(this, PacketSendEvent.class);
        addSettings(description);
    }

    public void onPacketSendEvent(PacketSendEvent event) {
        if (event.packet instanceof CPacketCloseWindow) {
            packets.add(event.packet);
            event.cancelEvent();
        }
    }

}
