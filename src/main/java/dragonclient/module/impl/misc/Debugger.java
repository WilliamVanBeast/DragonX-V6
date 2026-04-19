package dragonclient.module.impl.misc;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketKeepAlive;

public final class Debugger extends Module {
    public Debugger() {
        super("Debugger", Category.MISC);
        Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
    }

    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof SPacketConfirmTransaction) {
            final SPacketConfirmTransaction wrapper = ((SPacketConfirmTransaction) packet);

            Dragon.moduleManager.addChatMessage("Transaction | id: " + wrapper.getActionNumber());
        }

        if (packet instanceof SPacketKeepAlive) {
            final SPacketKeepAlive wrapper = ((SPacketKeepAlive) packet);

            Dragon.moduleManager.addChatMessage("KeepAlive | id: " + wrapper.getId());
        }
    }
}