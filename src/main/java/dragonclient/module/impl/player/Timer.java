package dragonclient.module.impl.player;

import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.FloatSetting;

public class Timer extends Module  {
    private FloatSetting timerspeed = new FloatSetting("Speed", 1f, 0.0f, 10.0f);
    public Timer() {
        super("Timer", Category.PLAYER);
        addSettings(timerspeed);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }

    public void onUpdateEvent(UpdateEvent event) {
        mc.timer.field_194147_b = timerspeed.get();
    }

    public void onDisable() {
        mc.timer.field_194147_b = 0f;
    }
}
