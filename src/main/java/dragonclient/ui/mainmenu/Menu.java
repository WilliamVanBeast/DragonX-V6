package dragonclient.ui.mainmenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

import dragonclient.ui.mainmenu.buttons.MenuButton;
import dragonclient.util.Particle;
import dragonclient.util.PositionUtils;
import dragonclient.util.RenderUtil;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.profile.GuiScreenEditProfile;
import net.lax1dude.eaglercraft.sp.gui.GuiScreenIntegratedServerStartup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.util.ResourceLocation;


public class Menu extends GuiScreen{
	
	private List<MenuButton> buttons = new ArrayList();
	private List<Particle> particles = new ArrayList();
	
	
	private float startTime;
	
	@Override
	public void initGui() {
		buttons.clear();
		particles.clear();

		
		//init button
		startTime = System.nanoTime();		
		buttons.add(new MenuButton("Single Player", new PositionUtils(this.width/2-50, this.height/2-80, 100, 20,1), 1));
		buttons.add(new MenuButton("Multi Player", new PositionUtils(this.width/2-50, this.height/2-60, 100, 20,1), 2));
		buttons.add(new MenuButton("Edit Profile", new PositionUtils(this.width/2-50, this.height/2-40, 100, 20,1), 3));
		buttons.add(new MenuButton("Settings", new PositionUtils(this.width/2-50, this.height/2-20, 100, 20,1), 4));
		buttons.add(new MenuButton("Discord", new PositionUtils(this.width/2-50, this.height/2, 100, 20,1), 5));
	}



	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		RenderUtil.drawRect(0, 0, this.width, this.height, 0xff000000);
		if(particles.size() < 200) {
			int needed = 200-particles.size();
			for(int i = 0; i < needed; i++) {
				particles.add(new Particle(RandomUtils.nextInt(0, this.width), RandomUtils.nextInt(0, this.height), RandomUtils.nextInt(2, 4)/2,2, 0xffffffff, RandomUtils.nextInt(0, 360), RandomUtils.nextInt(1000, 3000)));
			}

			//CHANGELOG
			
		mc.fontRendererObj.drawString("§lChangelog For Build: " + "Beta 2", 4, 5, -1);
        mc.fontRendererObj.drawString("§a§l - Added new modules", 4, 16, -1);
        mc.fontRendererObj.drawString("§a§l - Added Command System (use .help in chat)", 4, 24, -1);
		mc.fontRendererObj.drawString("§a§l - Added Anticheat", 4, 32, -1);
		mc.fontRendererObj.drawString("§a§l - Added new modules", 4, 40, -1);
		}
		
		
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			boolean b = iterator.next().draw();
		    if (b) {
		        iterator.remove();
		    }
		}
		
		for(Particle p : particles) {
			double diffX = p.getX() - mouseX;
			double diffY = p.getY() - mouseY;
			double dist = Math.sqrt(diffX*diffX+diffY*diffY);
			if(dist < 50) {
				RenderUtil.drawLine(p.getX(), p.getY(), mouseX, mouseY, 0x20ffffff, (float) 0.5);
			}
			for(Particle p2 : particles) {
				if(p2 == p) continue;
				double difX = p.getX() - p2.getX();
				double difY = p.getY() - p2.getY();
				double dit = Math.sqrt(difX*difX+difY*difY);
				if(dit < 30) {
					RenderUtil.drawLine(p.getX(), p.getY(), p2.getX(), p2.getY(), 0x05ffffff, (float) 0.5);
				}
			}
		}
		//mc.fontRendererObj.drawString("DragonX", width/2-mc.fontRendererObj.getStringWidth("DragonX")/2, this.height/2-90-20, 0xffcccccc);
		for(MenuButton b : buttons) {
			b.draw(mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);

		RenderUtil.drawImage(
         (double)(this.width / 2 - 80), (double)(this.height / 2 - 60 - 60), 160.0, 40.0, new ResourceLocation("dragonx/dragonx-logo.png"), -1
      );
	}



	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(MenuButton b : buttons) {
			if(b.getPosition().isInside(mouseX, mouseY)) {
				switch(b.getId()) {
				case 1:
                    mc.displayGuiScreen(new GuiScreenIntegratedServerStartup(this));
					break;
				case 2:
					mc.displayGuiScreen(new GuiMultiplayer(this));
					break;
				case 3:
					mc.displayGuiScreen(new GuiScreenEditProfile(this));
					return;
				case 5:
					EagRuntime.openLink("https://discord.gg/aMhGfybKs4");
					return;
				case 4:
					mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
					break;
				}
			}
		}
			
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}