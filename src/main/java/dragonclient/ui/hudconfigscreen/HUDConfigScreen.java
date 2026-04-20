package dragonclient.ui.hudconfigscreen;

import dragonclient.Dragon;
import dragonclient.module.RenderModule;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class HUDConfigScreen extends GuiScreen{
    
    public HUDConfigScreen() {
    }
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, width/2-100, height/6+148, "Back"));
        super.initGui();
    }

    @Override
    public void drawScreen(int i, int j, float var3) {
        for(dragonclient.module.Module m : Dragon.moduleManager.modules) {
            if(m.isEnabled() && m instanceof RenderModule) {
                ((RenderModule)m).renderLayout(i, j);
            }
        }
        super.drawScreen(i, j, var3);
    }

    @Override
    protected void actionPerformed(GuiButton parGuiButton) {
        if(parGuiButton.id == 1) {
            mc.displayGuiScreen(Dragon.getClickgui());
        }
    }
}
