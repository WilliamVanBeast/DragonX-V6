package dragonclient.module.impl.player;

import dragonclient.Dragon;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.IntegerSetting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;

public class FastPlace extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Allows you to place blocks faster than normal.");
        private final IntegerSetting ticks = new IntegerSetting("Ticks", 1, 0, 4);

    public FastPlace() {
        super("FastPlace", Category.PLAYER);
        addSettings(description, ticks);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
    }

    public void onPreMotionEvent(PreMotionEvent event) {
            mc.rightClickDelayTimer = Math.min(0, ticks.get());
    }
    

        public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

}
