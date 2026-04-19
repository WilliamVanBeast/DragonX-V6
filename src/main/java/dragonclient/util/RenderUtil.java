package dragonclient.util;

import static net.lax1dude.eaglercraft.opengl.RealOpenGLEnums.*;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public final class RenderUtil {
    private static final float zLevel = 0F;

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
            mc.fontRendererObj.drawString(tmp, (float) xTmp, (float) y, i, shadow);
            xTmp += mc.fontRendererObj.getCharWidth(textChar);
        }
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
