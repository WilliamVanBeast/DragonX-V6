package dragonclient.module.impl.misc;

import dragonclient.Dragon;
import dragonclient.IRCServer;
import dragonclient.event.Events.ChatEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;

public final class IRC extends Module{
    private DescriptionSetting description = new DescriptionSetting("Description", "Connect to client chat, to use: Put @ in front of your message to talk to fellow dragonx users");
    public static IRCServer server;
    public IRC() {
        super("IRC", Category.MISC);
        Dragon.eventManager.registerListener(this, ChatEvent.class);
        addSettings(description);
    }

    @Override
    public void onEnable() {
        server = new IRCServer();
    }

    @Override
    public void onDisable() {
        server.disconnect();
        server = null;
    }

    @Override
    public void onChatEvent(ChatEvent event) {
            String message = event.message;
            if (message.startsWith("@")) {
                event.cancelEvent();

                String parsed = message.substring(1);

                String[] args = parsed.split(" ");
                if (args.length > 0 && args[0].equalsIgnoreCase("reconnect")) {
                    server.disconnect();
                    server = null;
                    server = new IRCServer();

                    return;
                }

                server.sendIRCChatMessage(parsed);
            }
    }
}