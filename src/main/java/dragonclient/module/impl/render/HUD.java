package dragonclient.module.impl.render;

import java.util.Comparator;
import java.util.List;

import dragonclient.Dragon;
import dragonclient.event.Events.Render2DEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.util.Colors;
import dragonclient.util.RenderUtil;
import dragonclient.util.java.awt.Color;
import dragonclient.util.notifications.Notification;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;

public class HUD extends Module {
    private BooleanSetting arraylist = new BooleanSetting("Arraylist" , false);
    public static BooleanSetting notifications = new BooleanSetting("Notifications" , false);
    private BooleanSetting watermark = new BooleanSetting("Watermark" , false);
    public HUD() {
        super("HUD", Category.RENDER);
        addSettings(arraylist, notifications, watermark);
        Dragon.eventManager.registerListener(this, Render2DEvent.class);

    }

    @Override
    public void onRender2DEvent(Render2DEvent event) {
        if(arraylist.get()) {
                  List<Module> enabledModules = Dragon.moduleManager.getEnabledModules();
        enabledModules.sort(Comparator.comparingInt(module -> Minecraft.getMinecraft().fontRendererObj.getStringWidth(((Module) module).getName()) * -1));
        for (int i = 0; i < enabledModules.size(); i++) {
            Module module = enabledModules.get(i);
            int currentWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(module.name);
            GlStateManager.pushMatrix();
            RenderUtil.drawRect(event.resolution.getScaledWidth() - currentWidth - 4, i * 12, event.resolution.getScaledWidth(), i * 12 + 12, Colors.arrayListBackgroundColor);
            RenderUtil.drawRect(event.resolution.getScaledWidth() - currentWidth - 6, i * 12, event.resolution.getScaledWidth() - currentWidth - 4, i * 12 + 12, Colors.arrayListHighlightColor);
            Minecraft.getMinecraft().fontRendererObj.drawString(module.name, event.resolution.getScaledWidth() - currentWidth - 2, i * 12 + 2, Colors.arrayListTextColor);


            GlStateManager.popMatrix();
            }
        } if (notifications.get()) {
        Dragon.notificationManager.amongus();
        } if (watermark.get()) {
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            int men = fr.getStringWidth(getText1() + " | "+getMinutes()+"m "+getSeconds()+"s "+"Elapsed" + " | " + Minecraft.getDebugFPS() + "FPS") + 20;
            Gui.drawRect(0, 0, men, 15, Color.black.getRGB());
            RenderUtil.drawChromaString(getText1() + " | "+getMinutes()+"m "+getSeconds()+"s "+"Elapsed" + " | " + Minecraft.getDebugFPS() + "FPS", 5, 4, false);
        }
    }

    public String getText1(){
        return Dragon.name + Dragon.version;
    }

    
    public long getMinutes(){
        return (System.currentTimeMillis()-Dragon.startTime)/1000/60;
    }

    public long getSeconds(){
        return (System.currentTimeMillis()-Dragon.startTime)/1000-((System.currentTimeMillis()-Dragon.startTime)/1000/60)*60;
    }

}
