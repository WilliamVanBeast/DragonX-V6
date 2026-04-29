package dragonclient.module.impl.misc;

import java.util.Random;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketReceiveEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;

public class KillMessage extends Module {
   private DescriptionSetting description = new DescriptionSetting("Description", "Sends a random message from a list of messages when you kill someone.");
   private final String[] messages = new String[]{
      "Get Good Get DragonX", "ac is trash", "ezs", "Autoclient is shit, get DragonX", "DragonX Best", "Killed BY DragonX"
   };

   public KillMessage() {
      super("KillMessage", Category.MISC);
      Dragon.eventManager.registerListener(this, PacketReceiveEvent.class);
      addSettings(description);
   }
   @Override
   public void onPacketReceiveEvent(PacketReceiveEvent event) {
         Random rnd = new Random();
            SPacketChat s02PacketChat = new SPacketChat();
            String cp21 = s02PacketChat.getChatComponent().getUnformattedText();
            if (cp21.contains("was killed by " + mc.session.getUsername())) {
               mc.player.sendChatMessage(this.messages[rnd.nextInt(this.messages.length)]);
            }

            if (cp21.contains("was slain by " + mc.session.getUsername())) {
               mc.player.sendChatMessage(this.messages[rnd.nextInt(this.messages.length)]);
            }
   }
}