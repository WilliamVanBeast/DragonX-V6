package dragonclient.util;

import static net.lax1dude.eaglercraft.opengl.RealOpenGLEnums.*;

import java.util.ConcurrentModificationException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import dragonclient.util.java.awt.Color;
import net.lax1dude.eaglercraft.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.lax1dude.eaglercraft.opengl.OpenGlHelper;
import net.lax1dude.eaglercraft.opengl.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RenderUtil {
    private static final float zLevel = 0F;
    private static double ticks;

    public static void drawGradientRect(final int left, final int top, final int right, final int bottom,
            final int startColor, final int endColor) {
        final float f = (float) (startColor >> 24 & 255) / 255.0F;
        final float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        final float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        final float f3 = (float) (startColor & 255) / 255.0F;
        final float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        final float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        final float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        final float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawImage(double x, double y, double width, double height, ResourceLocation image, int color) {
      GL11.glDisable(2929);
      GlStateManager.enableBlend();
      GL11.glDepthMask(false);
      color(color);
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0F, 0.0F, width, height, width, height);
      GlStateManager.resetColor();
      GL11.glDepthMask(true);
      GlStateManager.disableBlend();
      GL11.glEnable(2929);
   }

    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }

        final float f3 = (float) (color >> 24 & 255) / 255.0F;
        final float f = (float) (color >> 16 & 255) / 255.0F;
        final float f1 = (float) (color >> 8 & 255) / 255.0F;
        final float f2 = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, zLevel).endVertex();
        worldrenderer.pos(right, bottom, zLevel).endVertex();
        worldrenderer.pos(right, top, zLevel).endVertex();
        worldrenderer.pos(left, top, zLevel).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRectThing(double left, double top, double right, double bottom, final int color) {
        final float f3 = (float) (color >> 24 & 255) / 255.0F;
        final float f = (float) (color >> 16 & 255) / 255.0F;
        final float f1 = (float) (color >> 8 & 255) / 255.0F;
        final float f2 = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, zLevel).endVertex();
        worldrenderer.pos(right, bottom, zLevel).endVertex();
        worldrenderer.pos(right, top, zLevel).endVertex();
        worldrenderer.pos(left, top, zLevel).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawCircle(double cx, double cy, double r, int num_segments, int color) {
        final float f3 = (float) (color >> 24 & 255) / 255.0F;
        final float f = (float) (color >> 16 & 255) / 255.0F;
        final float f1 = (float) (color >> 8 & 255) / 255.0F;
        final float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.enableColorMaterial();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        EaglercraftGPU.glLineWidth(1.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(cx, cy, zLevel).endVertex();
    
        for (int i = num_segments; i >= 0; --i) {
            float theta = (float) (i * 3.1415927410125732D * 2.0D / num_segments);
            float x = MathHelper.sin(theta) * (float) r;
            float y = MathHelper.cos(theta) * (float) r;
            worldrenderer.pos((double) ((float) cx + x), (double) ((float) cy - y), zLevel).endVertex();
        }
        tessellator.draw();
        GlStateManager.disableBlend();
    }

public static void drawChromaString(final String string, final int x, final int y, final boolean shadow) {
        final Minecraft mc = Minecraft.getMinecraft();
        int xTmp = x;
        char[] charArray;
        for (int length = (charArray = string.toCharArray()).length, j = 0; j < length; ++j) {
            final char textChar = charArray[j];
            final long l = System.currentTimeMillis() - (xTmp * 10 - y * 10);
            final int i = Color.HSBtoRGB(l % 2000L / 2000.0f, 0.8f, 0.8f);
            final String tmp = String.valueOf(textChar);
            Minecraft.getMinecraft().fontRendererObj.drawString(tmp, (float) xTmp, (float) y, i, shadow);
            xTmp += Minecraft.getMinecraft().fontRendererObj.getCharWidth(textChar);
        }
    }

        public static void drawRectOutline(double param1, double param2, double width1, double height1, int color) {
        drawRect(param1, param2, width1, param2 + 1, color);
        drawRect(param1, param2, param1 + 1, height1, color);
        drawRect(width1 - 1, param2, width1, height1, color);
        drawRect(param1, height1 - 1, width1, height1, color);
    }

        public static void drawImage(double x, double y, int width, int height, ResourceLocation rec) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(rec);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture((float) x, (float) y, 0, 0, width, height, width, height);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
   
        public void renderBreadCrumb(final Vec3d vec3) {

        GlStateManager.disableDepth();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        try {

            final double x = vec3.xCoord - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
            final double y = vec3.yCoord - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
            final double z = vec3.zCoord - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;

            final double distanceFromPlayer = Minecraft.getMinecraft().player.getDistance(vec3.xCoord, vec3.yCoord - 1, vec3.zCoord);
            int quality = (int) (distanceFromPlayer * 4 + 10);

            if (quality > 350)
                quality = 350;

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);

            final float scale = 0.04f;
            GL11.glScalef(-scale, -scale, -scale);

            GL11.glRotated(-(Minecraft.getMinecraft().getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
            GL11.glRotated((Minecraft.getMinecraft().getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);

            final Color c = Color.WHITE;

            RenderUtil.drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

            if (distanceFromPlayer < 4)
                RenderUtil.drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);

            if (distanceFromPlayer < 20)
                RenderUtil.drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);


            GL11.glScalef(0.8f, 0.8f, 0.8f);

            GL11.glPopMatrix();


        } catch (final ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableDepth();

        GL11.glColor3f(255, 255, 255);
    }

    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / 20; i++) {
            final double x2 = Math.sin(((i * 20 * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * 20 * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();

    }

    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = ((c >> 24) & 0xff) / 255F;
        final float f1 = ((c >> 16) & 0xff) / 255F;
        final float f2 = ((c >> 8) & 0xff) / 255F;
        final float f3 = (c & 0xff) / 255F;

        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
    }

    public static void renderBreadCrumbs(final List<Vec3d> vec3s) {

        GlStateManager.disableDepth();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int i = 0;
        try {
            for (final Vec3d v : vec3s) {

                i++;

                boolean draw = true;

                final double x = v.xCoord - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
                final double y = v.yCoord - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
                final double z = v.zCoord - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;

                final double distanceFromPlayer = Minecraft.getMinecraft().player.getDistance(v.xCoord, v.yCoord - 1, v.zCoord);
                int quality = (int) (distanceFromPlayer * 4 + 10);

                if (quality > 350)
                    quality = 350;

                if (i % 10 != 0 && distanceFromPlayer > 25) {
                    draw = false;
                }

                if (i % 3 == 0 && distanceFromPlayer > 15) {
                    draw = false;
                }

                if (draw) {

                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);

                    final float scale = 0.04f;
                    GL11.glScalef(-scale, -scale, -scale);

                    GL11.glRotated(-(Minecraft.getMinecraft().getRenderManager()).playerViewY, 0.0D, 1.0D, 0.0D);
                    GL11.glRotated((Minecraft.getMinecraft().getRenderManager()).playerViewX, 1.0D, 0.0D, 0.0D);

                    final Color c = Color.WHITE;


                    RenderUtil.drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);

                    if (distanceFromPlayer < 4)
                        RenderUtil.drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);

                    if (distanceFromPlayer < 20)
                        RenderUtil.drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);

                    GL11.glScalef(0.8f, 0.8f, 0.8f);

                    GL11.glPopMatrix();

                }

            }
        } catch (final ConcurrentModificationException ignored) {
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableDepth();

        GL11.glColor3f(255, 255, 255);
    }

    	public static void drawPoint(double x, double y, int color, float size) {
		GL11.glPushMatrix();

		start2D();
		GL11.glPointSize(size);
		GL11.glBegin(GL11.GL_POINTS);
		color(color);
		GL11.glVertex2d(x, y);

		GL11.glEnd();

		stop2D();

		GL11.glPopMatrix();
	}
    	public static void drawLine(double x, double y, double x1, double y1, int color, float size) {
		GL11.glPushMatrix();

		start2D();

		GL11.glLineWidth(size);
		color(color);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x1, y1);
		}
		GL11.glEnd();

		stop2D();

		GL11.glPopMatrix();
	}

    	public static void color(int color) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, f3);
	}

	public static void color(int color, float a) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, a);
	}

    	public static void start2D() {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableAlpha();
		GlStateManager.disableDepth();
	}
    	public static void stop2D() {
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.resetColor();
	}
    public static void drawArc(double cx, double cy, double r, double start_angle, double end_angle, int num_segments, int color) {
        final float f3 = (float) (color >> 24 & 255) / 255.0F;
        final float f = (float) (color >> 16 & 255) / 255.0F;
        final float f1 = (float) (color >> 8 & 255) / 255.0F;
        final float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.enableColorMaterial();
        EaglercraftGPU.glLineWidth(1.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        worldrenderer.pos(cx, cy, zLevel).endVertex();
    
        for (int i = num_segments; i >= 0; --i) {
            float theta = (float) (i / (double) num_segments * (end_angle - start_angle) * Math.PI / 180.0D + start_angle * Math.PI / 180.0D);
            float x = MathHelper.sin(theta) * (float) r;
            float y = MathHelper.cos(theta) * (float) r;
            worldrenderer.pos((double) ((float) cx + x), (double) ((float) cy - y), zLevel).endVertex();
        }
        tessellator.draw();
        GlStateManager.disableBlend();
    }
    

    public static void drawRoundedRect(double left, double top, double right, double bottom, double radius, int color) {
        int numSegments = 10;

        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }

        drawRect(left + radius, top, right - radius, bottom, color);
        drawRect(left, top + radius, right, bottom - radius, color);
        drawArc(left, top, radius, 90, 180, numSegments, color);
        drawArc(right, top, radius, 180, 270, numSegments, color);
        drawArc(left, bottom, radius, 0, 90, numSegments, color);
        drawArc(right, bottom, radius, 270, 360, numSegments, color);
    }

    public static void drawRoundedRect(double left, double top, double right, double bottom, double radius, int color, boolean roundedTopLeft, boolean roundedTopRight, boolean roundedBottomLeft, boolean roundedBottomRight) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }

        int numSegments = 10;
        // Draw main rectangle
        drawRect(left + radius, top, right - radius, bottom, color);
        drawRect(left, top + radius, right, bottom - radius, color);
    
        if (roundedTopLeft) {
            drawArc(left, top, radius, 90, 180, numSegments, color);
        } else {
            drawRect(left, top, left + radius, top + radius, color);
        }
        
        if (roundedTopRight) {
            drawArc(right, top, radius, 180, 270, numSegments, color);
        } else {
            drawRect(right - radius, top, right, top + radius, color);
        }
    
        if (roundedBottomLeft) {
            drawArc(left, bottom, radius, 0, 90, numSegments, color);
        } else {
            drawRect(left, bottom - radius, left + radius, bottom, color);
        }
    
        if (roundedBottomRight) {
            drawArc(right, bottom, radius, 270, 360, numSegments, color);
        } else {
            drawRect(right - radius, bottom - radius, right, bottom, color);
        }
    }
    
    
}
