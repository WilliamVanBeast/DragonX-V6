package dragonclient.anticheat.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntity;

public class PacketUtil {

    public static boolean isRelMove(final Packet<?> p) {
        return p instanceof SPacketEntity.S15PacketEntityRelMove
                || p instanceof SPacketEntity.S17PacketEntityLookMove
                || p instanceof SPacketEntity.S16PacketEntityLook;
    }

    public boolean isLook(final Packet<?> p) {
        return p instanceof SPacketEntity.S17PacketEntityLookMove
                || p instanceof SPacketEntity.S16PacketEntityLook;
    }
}