package dragonclient.module.impl.render;

import org.lwjgl.opengl.GL11;

import dragonclient.Dragon;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.FloatSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.java.awt.Color;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class ESP extends Module{
    private ListSetting mode = new ListSetting("Mode", new String[]{"Normal", "2D"}, "Normal");
    private BooleanSetting healthBar = new BooleanSetting("Health Bar", true, () -> mode.get().equalsIgnoreCase("2D"));
    public ESP() {
        super("ESP", Category.RENDER);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
        addSettings(mode, healthBar);
    }

    public void onRender3DEvent(Render3DEvent e) {
        if(mode.get().equals("Normal")){
        for(Object o : mc.world.loadedEntityList){
            if(o instanceof EntityPlayer && !o.equals(mc.player)){
                entityESPBox(((Entity)o), 0);
            }
        }
    } else if(mode.get().equals("2D")){
            for (Entity o2 : this.mc.world.getLoadedEntityList()) {
                String ree;
                if (!(o2 instanceof EntityPlayer) || o2 == this.mc.player) continue;
                Color color = Color.WHITE;
                this.esp2d(o2, color);
            }
        }
    }

        public void esp2d(Entity e2, Color color) {
        this.esp2d(e2, color, 2);
    }

            public void esp2d(Entity e2, Color color, int w2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.disableBlend();
        
        // Normalize color values from 0-255 to 0.0-1.0
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;
        
        GL11.glColor4f(r, g, b, a);
        GL11.glLineWidth(w2);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX - (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX + (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX + (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY + (double)e2.height, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX - (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY + (double)e2.height, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glEnd();
        
        if (healthBar.get()) {
            if(e2 instanceof EntityPlayer) {
            this.renderHealthBar((EntityPlayer) e2, w2);
         }
        } 
        
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    
    public void renderHealthBar(EntityPlayer player, int width) {
        double x = player.posX - this.mc.renderManager.viewerPosX;
        double y = player.posY - this.mc.renderManager.viewerPosY;
        double z = player.posZ - this.mc.renderManager.viewerPosZ;
        
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float healthPercent = Math.max(0, Math.min(1, health / maxHealth));
        
        double barWidth = 0.15;
        double barHeight = player.height;
        double barOffsetX = player.width + 0.1;
        
        // Background (black)
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(x + barOffsetX, y, z);
        GL11.glVertex3d(x + barOffsetX + barWidth, y, z);
        GL11.glVertex3d(x + barOffsetX + barWidth, y + barHeight, z);
        GL11.glVertex3d(x + barOffsetX, y + barHeight, z);
        GL11.glEnd();
        
        // Determine color based on health percentage
        float r, g, b;
        if(healthPercent > 0.5f) {
            // Green for > 50%
            r = 0.0f;
            g = 1.0f;
            b = 0.0f;
        } else if(healthPercent > 0.25f) {
            // Orange for 25% - 50%
            r = 1.0f;
            g = 0.65f;
            b = 0.0f;
        } else {
            // Red for < 25%
            r = 1.0f;
            g = 0.0f;
            b = 0.0f;
        }
        
        // Health bar (vertical, from bottom to top)
        GL11.glColor4f(r, g, b, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(x + barOffsetX, y, z);
        GL11.glVertex3d(x + barOffsetX + barWidth, y, z);
        GL11.glVertex3d(x + barOffsetX + barWidth, y + (barHeight * healthPercent), z);
        GL11.glVertex3d(x + barOffsetX, y + (barHeight * healthPercent), z);
        GL11.glEnd();
        
        // Border (white)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(x + barOffsetX, y, z);
        GL11.glVertex3d(x + barOffsetX + barWidth, y, z);
        GL11.glVertex3d(x + barOffsetX + barWidth, y + barHeight, z);
        GL11.glVertex3d(x + barOffsetX, y + barHeight, z);
        GL11.glEnd();
    }
    
        public void entityESPBox(Entity entity, int mode) {
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            EaglercraftGPU.glLineWidth(2.0f);
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            if(mode == 0) //Enemy
                GlStateManager.color(
                        1 - (float)mc.player.getDistanceSqToEntity(entity) / 40,
                        (float)mc.player.getDistanceSqToEntity(entity) / 40,
                        0, 0.5F);
                        
            
            else if(mode == 1)//friend
                GlStateManager.color(0, 0, 1, 0.5F);
            else if(mode == 2)//Other
                GlStateManager.color(1, 1, 0, 0.5F);
            else if(mode == 3)// Target
                GlStateManager.color(1, 0, 0, 0.5F);
            else if(mode == 4)//Team
                GlStateManager.color(0, 1, 0, 0.5F);
            RenderGlobal.func_181561_a(
                    new AxisAlignedBB(
                            entity.getEntityBoundingBox().minX
                                -0.05
                                - entity.posX
                                + (entity.posX -mc.getRenderManager().renderPosX),
                            entity.getEntityBoundingBox().minY
                                -0.05
                                - entity.posY
                                + (entity.posY -mc.getRenderManager().renderPosY),
                            entity.getEntityBoundingBox().minZ
                                -0.05
                                - entity.posZ
                                + (entity.posZ -mc.getRenderManager().renderPosZ),
                            entity.getEntityBoundingBox().maxX
                                +0.05
                                - entity.posX
                                + (entity.posX -mc.getRenderManager().renderPosX),
                            entity.getEntityBoundingBox().maxY
                                +0.1
                                - entity.posY
                                + (entity.posY -mc.getRenderManager().renderPosY),
                            entity.getEntityBoundingBox().maxZ
                                +0.05
                                - entity.posZ
                                + (entity.posZ -mc.getRenderManager().renderPosZ)));
            
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
        }
    
}
