package dragonclient.module;

import java.util.ArrayList;
import java.util.List;

import dragonclient.Dragon;
import dragonclient.event.Listener;
import dragonclient.module.impl.render.HUD;
import dragonclient.module.settings.KeySetting;
import dragonclient.util.MinecraftInstance;
import dragonclient.util.notifications.Notification;
import dragonclient.util.notifications.NotificationManager;
import dragonclient.util.notifications.Notification.NotificationType;
import net.lax1dude.eaglercraft.KeyboardConstants;


public abstract class Module extends MinecraftInstance implements Listener {
    public String name;
    private String description;
    private Category category;
    private KeySetting key = new KeySetting("Key", KeyboardConstants.KEY_NONE);
    public boolean array = false;
    private boolean canEnable = true;
    private boolean enabled = false;
    private List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Setting<?> getValue(String valueName) {
        for (Setting<?> value : getSettings()) {
            if (value.getName().equalsIgnoreCase(valueName)) {
                return value;
            }
        }
        return null;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void addSettings(Setting<?>... values) {
        for (Setting<?> value : values) {
            this.settings.add(value);
        }
    }

    public void setEnabled(boolean enabled) {
        if (canEnable) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }

            onToggle(enabled);
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    protected void onInitialize() {
        addSettings(key);
    }

    protected void onLoad() {
    }

    protected void onEnable() {
        if(HUD.notifications.get()) {
            Dragon.notificationManager.registerNotification("Enabled: " + name , NotificationType.NOTIFICATION);
        }
    }

    protected void onDisable() {
        if(HUD.notifications.get()) {
            Dragon.notificationManager.registerNotification("Disabled: " + name , NotificationType.NOTIFICATION);
        }
    }

    protected void onToggle(boolean enabled) {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public KeySetting getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }
}
