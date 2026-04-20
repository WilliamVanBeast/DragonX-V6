package dragonclient.module.impl.hud;

import dragonclient.module.Category;
import dragonclient.module.RenderModule;
import net.minecraft.client.Minecraft;

public class Fps extends RenderModule {
    public Fps() {
        super("FPS", Category.HUD, 10, 10, 60, 9);
    }

    public void draw() {
        Minecraft.getMinecraft().fontRendererObj.drawString("[FPS: " + Minecraft.getDebugFPS() + "]", this.x, this.y, -1);
    }
}