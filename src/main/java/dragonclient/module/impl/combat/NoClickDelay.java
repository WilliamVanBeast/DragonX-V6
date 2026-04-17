package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;

public class NoClickDelay extends Module {
    public NoClickDelay() {
        super("NoClickDelay", Category.COMBAT);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
    }

    public void onPreMotionEvent(PreMotionEvent event) {
            if (mc.player != null && mc.world != null) {
            mc.leftClickCounter = 0;
        }
    }
}
