package dragonclient;

import dragonclient.ui.newclickgui.*;
import dragonclient.util.notifications.NotificationManager;
import dragonclient.module.ModuleManager;
import dragonclient.module.SettingsSave;
import dragonclient.anticheat.check.CheckStorage;
import dragonclient.command.CommandManager;
import dragonclient.event.EventManager;

import net.lax1dude.eaglercraft.Display;

public class Dragon {
    public static String name = "DragonX", version = " V6.0", creator = "Fatal";
    public static final String CLIENT_NAME_CHAT = "§2[" + name + "]§r ";
    public static Dragon instance = new Dragon();
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;
    public static EventManager eventManager;
    public static NotificationManager notificationManager;
    public static CommandManager commandManager;

    

    public static void startClient() {
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        moduleManager.registerModules();
        clickGui = new ClickGui();
        notificationManager = new NotificationManager();
        commandManager = new CommandManager();
        CheckStorage.setInstance(new CheckStorage());

        CheckStorage.getInstance().init();

        SettingsSave.read();
        Display.setTitle(name + " " + version + "by " + creator);
    }
    
    public static boolean animateClickGui() {
        return true;
    }
    public final static ClickGui getClickgui() { return clickGui; }

    public static long startTime = System.currentTimeMillis();



}
