package dragonclient.irc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public interface IRCUtils {
    // our minecraft instance
    Minecraft mc = Minecraft.getMinecraft();

    // the irc client message prefix
    String IRC_PREFIX = TextFormatting.GRAY + "<" +
            TextFormatting.RED + "DragonX" +
            TextFormatting.GRAY + ">" +
            TextFormatting.RESET + " ";

    /**
     * Checks if necessities needed by the client for modules are null
     * @return if they are null
     */
    default boolean nullCheck() {
        return mc.player == null || mc.world == null;
    }

    /**
     * Sends a client message
     * @param msg the message to present to the user
     */
    default void sendChatMessage(String msg) {
        mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(IRC_PREFIX + msg));
    }

    /**
     * Sends a client message with an editable message id
     * @param id the editable message id
     * @param msg the message to present to the user
     */
    default void sendChatMessage(int id, String msg) {
        mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(IRC_PREFIX + msg), id);
    }

    default void sendIRCMessage(String message) {
        if (!nullCheck()) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(IRC_PREFIX + message));
        }
    }
}
