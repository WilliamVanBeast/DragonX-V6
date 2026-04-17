package dragonclient.module.impl.render;

import dragonclient.Dragon;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.FloatSetting;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class ESP extends Module{
    private FloatSetting linewidth = new FloatSetting("LineWidth", 2f, 1f, 10f);
    public ESP() {
        super("ESP", Category.RENDER);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
        addSettings(linewidth);
    }

    public void onRender3DEvent(Render3DEvent e) {
        for(Object o : mc.world.loadedEntityList){
            if(o instanceof EntityPlayer && !o.equals(mc.player)){
                entityESPBox(((Entity)o), 0);
            }
        }
    }

    
        public void entityESPBox(Entity entity, int mode) {
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            EaglercraftGPU.glLineWidth(linewidth.get());
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
