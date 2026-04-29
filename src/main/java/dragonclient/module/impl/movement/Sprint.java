package dragonclient.module.impl.movement;

import dragonclient.Dragon;
import dragonclient.event.Events.PostMotionEvent;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.ListSetting;

public class Sprint extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Automatically sprints for you.");
    private ListSetting mode = new ListSetting("Mode", new String[] {"Normal" }, "Normal");

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        addSettings(mode, description);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }
    
    public void onUpdateEvent(UpdateEvent event) {
        if(mode.get().equalsIgnoreCase("Normal")) {
            mc.player.setSprinting(true);
        }
    }

}
