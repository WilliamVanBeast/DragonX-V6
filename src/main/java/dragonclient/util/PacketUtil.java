package dragonclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;

public final class PacketUtil {
    public static void sendPacket(final Packet<INetHandlerPlayServer> packet) {
        Minecraft.getMinecraft().getNetHandler().sendPacket(packet);
    }

    public static void sendPacketNoEvent(final Packet<INetHandlerPlayServer> packet) {
        Minecraft.getMinecraft().getNetHandler().sendPacket(packet);
    }

    public static void sendPacketSilent(final Packet<INetHandlerPlayServer> packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static void sendPacketVerySilent(final Packet<INetHandlerPlayServer> packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static void sendPacketNoEventhuh(final Packet<?> packet) {
        Minecraft.getMinecraft().getNetHandler().sendPacket(packet);
    }
}
