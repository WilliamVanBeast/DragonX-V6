package dragonclient.module.impl.hud;

import dragonclient.Dragon;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.ui.hudconfigscreen.HUDConfigScreen;

public class ConfigScreen extends Module {
    public ConfigScreen() {
        super("ConfigScreen", Category.HUD);
    }

     @Override
     public void onEnable() {
         mc.displayGuiScreen(new HUDConfigScreen());
         setEnabled(false);
     }
    
}
