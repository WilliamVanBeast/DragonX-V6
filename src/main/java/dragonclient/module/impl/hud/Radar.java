package dragonclient.module.impl.hud;

import dragonclient.Dragon;
import dragonclient.event.Events.Render2DEvent;
import dragonclient.module.Category;
import dragonclient.module.RenderModule;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.util.RenderUtil;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class Radar extends RenderModule {

        public BooleanSetting players = new BooleanSetting("Players", true);
    public BooleanSetting animals = new BooleanSetting("Animals", false);
    public BooleanSetting mobs = new BooleanSetting("Mobs", false);
    public BooleanSetting invisible = new BooleanSetting("Invisibles", false);

    public Radar() {
        super("Radar", Category.HUD, 50, 50, 50, 50);
        addSettings(players, animals, mobs, invisible);
        Dragon.eventManager.registerListener(this, Render2DEvent.class);
    }
    
    public void onRender2DEvent(Render2DEvent event) {
        Gui.drawRect(-1,-1,width + 1,height + 1,0xFF212121);

        GlStateManager.translate(width / 2f, height /2f,0);
        for(Entity entity : mc.world.loadedEntityList){
            if(entity instanceof EntityLivingBase){
                int color = 0;
                if(entity.isInvisible() && !invisible.get())
                    continue;
                if(mobs.get())
                    if(entity instanceof EntityMob){
                        color = 0xff4d1410;
                    }

                if(animals.get())
                    if(entity instanceof EntityAnimal){
                        color = 0xff38ffeb;
                    }

                if(players.get())
                    if(entity instanceof EntityPlayer){
                        color = 0xffff3838;
                    }

                double x = mc.player.posX - entity.posX;
                double y = mc.player.posZ - entity.posZ;
                RenderUtil.drawRect(x - 1, y - 1,x + 1, y + 1, color);
            }
        }
        GlStateManager.rotate(mc.player.rotationYaw,0,0,1);
        GlStateManager.color(1,1,1);
        RenderUtil.drawImage(-3,-3,6,6,new ResourceLocation("dragonx/triangle.png"));
        GlStateManager.rotate(-mc.player.rotationYaw,0,0,1);
        GlStateManager.translate(-(width / 2f), -(height /2f),0);

    }
}
