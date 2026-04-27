package dragonclient.module.impl.render;

import org.lwjgl.opengl.GL11;

import dragonclient.Dragon;
import dragonclient.event.Events.Render3DEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.ColorSetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.java.awt.Color;
import net.lax1dude.eaglercraft.opengl.GlStateManager;

public class ChinaHat extends Module {
    private ListSetting quality = new ListSetting("Quality", new String[] {"Not china hat", "Umbrella"
			, "Very Low", "Low", "Normal", "High", "Very High", "Smooth"} , "Normal");
	private final BooleanSetting showInFirstPerson = new BooleanSetting("First Person", true);
	private final BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public ChinaHat() {
        super("China Hat", Category.RENDER);
        addSettings(quality, showInFirstPerson, rotate);
        Dragon.eventManager.registerListener(this, Render3DEvent.class);
    }

    public static long lastFrame = 0;

    	public void onRender3DEvent(final Render3DEvent event) {
		if (mc.gameSettings.thirdPersonView == 0 && !showInFirstPerson.get()) return;
		
		lastFrame = System.currentTimeMillis();
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableCull();
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		
		final double x = mc.player.lastTickPosX +
						 (mc.player.posX - mc.player.lastTickPosX) * mc.timer.field_194147_b -
						 mc.getRenderManager().viewerPosX;
		final double y = (mc.player.lastTickPosY +
						  (mc.player.posY - mc.player.lastTickPosY) * mc.timer.field_194147_b -
						  mc.getRenderManager().viewerPosY
						 ) + mc.player.getEyeHeight() + 0.5 + (mc.player.isSneaking() ? -0.2 : 0);
		final double z = mc.player.lastTickPosZ +
						 (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.field_194147_b -
						 mc.getRenderManager().viewerPosZ;
		
		Color c;
		
		final double rad = 0.65f;
		
		int q = 64;
		
		boolean increaseCount = false;
		
		switch (quality.get()) {
			case "Not china hat anymore":
				q = 8;
				increaseCount = true;
				break;
			case "Umbrella":
				q = 16;
				break;
			case "Very Low":
				q = 32;
				increaseCount = true;
				break;
			case "Low":
				increaseCount = true;
				break;
			case "Normal":
				q = 128;
				break;
			case "High":
				q = 256;
				increaseCount = true;
				break;
			case "Very High":
				q = 512;
				increaseCount = true;
				break;
			case "Smooth":
				q = 1024;
				increaseCount = true;
				break;
		}
		
		final double rotations = rotate.get() ? ((mc.player.prevRenderYawOffset +
														(mc.player.renderYawOffset - mc.player.prevRenderYawOffset
														) * mc.timer.field_194147_b
													   ) / 60
													  ) + 20 : 0;
		
		for (float i = 0; i < Math.PI * 2 + (increaseCount ? 0.01 : 0); i += Math.PI * 4 / q) {
			final double vecX = x + rad * Math.cos(i + rotations);
			final double vecZ = z + rad * Math.sin(i + rotations);
						
			GL11.glColor4f(102 / 255.F,
					0 / 255.F,
					153 / 255.F,
					1f
			);
			
			GL11.glVertex3d(vecX, y - 0.25, vecZ);
			
			GL11.glColor4f(102 / 255.F,
					0 / 255.F,
					153 / 255.F,
					1f
			);
			
			GL11.glVertex3d(x, y, z);
			
		}
		
		GL11.glEnd();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GlStateManager.enableCull();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		
		GL11.glColor3f(255, 255, 255);
	}


    
}
