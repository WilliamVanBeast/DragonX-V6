package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;

public class NoClickDelay extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Removes the delay between your clicks.");
    public NoClickDelay() {
        super("NoClickDelay", Category.COMBAT);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
        addSettings(description);
    }

    public void onPreMotionEvent(PreMotionEvent event) {
            if (mc.player != null && mc.world != null) {
            mc.leftClickCounter = 0;
        }
    }
}
