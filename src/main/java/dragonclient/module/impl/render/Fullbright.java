package dragonclient.module.impl.render;

import dragonclient.module.Module;
import dragonclient.module.settings.FloatSetting;
import dragonclient.module.settings.ListSetting;
import net.minecraft.client.Minecraft;



public class Fullbright extends Module {
     private ListSetting mode = new ListSetting("Mode", new String[] {"Normal", "Custom"} , "Normal");
     private FloatSetting brightnessenable = new FloatSetting("Brightness (on)",100f,0f,100f, () -> mode.get().equalsIgnoreCase("Custom"));
     private FloatSetting brightnessdisable = new FloatSetting("Brightness (off)",1f,0f,0.5f, () -> mode.get().equalsIgnoreCase("Custom"));
    public Fullbright() {
        super("Fullbright", dragonclient.module.Category.RENDER);
        addSettings(mode,brightnessenable, brightnessdisable);
    }

    public void onEnable() {
        if(mode.get().equals("Normal")){
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100f;
        } else if(mode.get().equals("Custom")) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = brightnessenable.get();
        }

    }

    public void onDisable() {
            if(mode.get().equals("Normal")){
        Minecraft.getMinecraft().gameSettings.gammaSetting = 0.5f;
        } else if(mode.get().equals("Custom")) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = brightnessdisable.get();
        }

    }

    
}
