package dragonclient.module.impl.movement;

import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;

public class No003 extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Prevents you from false flagging");
    public No003() {
        super("No003", Category.MOVEMENT); 
        addSettings(description);
    }
}
