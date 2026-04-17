package dragonclient.module.impl.render;

import static net.lax1dude.eaglercraft.opengl.RealOpenGLEnums.GL_LINE_SMOOTH;

import dragonclient.Dragon;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.FloatSetting;
import net.lax1dude.eaglercraft.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class Tracers extends Module {
    private FloatSetting linewidth = new FloatSetting("LineWidth", 1.0f, 1.0F, 10.0f);
    public Tracers() {
        super("Tracers", Category.RENDER);
        addSettings(linewidth);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        for (final EntityPlayer playerEntity : mc.world.playerEntities) {
            if (playerEntity != mc.player && !playerEntity.isDead && !playerEntity.isInvisible())
                drawToPlayer(playerEntity);
        }
        GlStateManager.resetColor();
    }

    public void drawToPlayer(final EntityLivingBase entity) {
        final double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.field_194147_b)
                - mc.getRenderManager().viewerPosX;
        final double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.field_194147_b)
                - mc.getRenderManager().viewerPosY;
        final double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.field_194147_b)
                - mc.getRenderManager().viewerPosZ;

        drawTracerLine(xPos, yPos, zPos, 0, 1, 0, 1.0f, linewidth.get());
    }

    public static void drawTracerLine(double x, double y, double z, final float red, final float green,
            final float blue, final float alpha, final float lineWidth) {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Minecraft.getMinecraft().entityRenderer.orientCamera(Minecraft.getMinecraft().timer.field_194147_b);
        PlatformOpenGL._wglEnable(GL_LINE_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        EaglercraftGPU.glLineWidth(lineWidth);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(2, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(0, 0, 0).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(x, y, z).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        PlatformOpenGL._wglDisable(GL_LINE_SMOOTH);
        GlStateManager.popMatrix();
    }
}
