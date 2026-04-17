package dragonclient.ui.mainmenu.buttons;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import dragonclient.util.Colors;
import dragonclient.util.PositionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class MenuButton {
	
	private String name;
	
	private PositionUtils position;
	
	private double imageAnimation, textAnimation;
	
	private float startTime;
	
	private int id;
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	public MenuButton(String name,PositionUtils position, int id) {

		this.position = position;
		this.id = id;
		this.name = name;
	}
	
	public void draw(int mouseX, int mouseY) {
		if(position.isInside(mouseX, mouseY)) {
			imageAnimation = (imageAnimation * (4) + 3)/5;
			textAnimation = (textAnimation * 9 + 3) /10;
		}else {
			imageAnimation = (imageAnimation * (4))/5;
			textAnimation = (textAnimation * 9) /10;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(this.position.getX()+this.position.getWidth()/2-mc.fontRendererObj.getStringWidth(name)/2*1.05, this.position.getY()+5, 0);
		GL11.glScaled(1.05,1.05, 1);
		mc.fontRendererObj.drawString(name, 0,0, -1);
		GL11.glPopMatrix();
		
	}
	public void clicked(int mouseX, int mouseY, int button) {
		
	}
	
	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}

	public int getId() {
		
		return id;
	}
	
	
	
}