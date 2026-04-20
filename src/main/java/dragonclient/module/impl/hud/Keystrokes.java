package dragonclient.module.impl.hud;

import dragonclient.Dragon;
import dragonclient.module.Category;
import dragonclient.module.RenderModule;
import dragonclient.module.Setting;
import dragonclient.module.settings.BooleanSetting;
import net.lax1dude.eaglercraft.KeyboardConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Keystrokes extends RenderModule {

    private BooleanSetting buttons = new BooleanSetting("Buttons", true);
    	public Keystrokes() {
		super("Keystrokes", Category.HUD, 475, 225, 100, 300);
        addSettings(buttons);
	}

	@Override
	public void draw() {
 		Gui.drawRect(this.x+25+5, this.y, this.x+25+5+25, this.y+25, !mc.gameSettings.keyBindForward.pressed ? 0x88101010 : 0x88888888);
		
		Gui.drawRect(this.x+25+5, this.y+5+25, this.x+25+5+25, this.y+25+5+25, !mc.gameSettings.keyBindBack.pressed ? 0x88101010 : 0x88888888);
		Gui.drawRect(this.x+25+5+5+25, this.y+5+25, this.x+25+5+5+25+25, this.y+25+5+25, !mc.gameSettings.keyBindRight.pressed ? 0x88101010 : 0x88888888);
		Gui.drawRect(this.x, this.y+5+25, this.x+25, this.y+25+5+25, !mc.gameSettings.keyBindLeft.pressed ? 0x88101010 : 0x88888888);
		if (buttons.get()) {
		Gui.drawRect(this.x, this.y+5+25+5+25, this.x+40, this.y+25+5+25+5+25, !mc.gameSettings.keyBindAttack.pressed ? 0x88101010 : 0x88888888);
		Gui.drawRect(this.x+40+5, this.y+5+25+5+25, this.x+40+40+5, this.y+25+5+25+5+25, !mc.gameSettings.keyBindUseItem.pressed ? 0x88101010 : 0x88888888);
		}
		Minecraft.getMinecraft().fontRendererObj.drawString(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindForward.keyCode), this.x+25+5+(25/2-mc.fontRendererObj.getStringWidth(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindForward.keyCode))/2), this.y+25/2-mc.fontRendererObj.FONT_HEIGHT/2, mc.gameSettings.keyBindForward.pressed ? 0 : -1);
		Minecraft.getMinecraft().fontRendererObj.drawString(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindLeft.keyCode), this.x+(25/2-mc.fontRendererObj.getStringWidth(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindLeft.keyCode))/2), (this.y+25+(25/2)), mc.gameSettings.keyBindLeft.pressed ? 0 : -1);
		Minecraft.getMinecraft().fontRendererObj.drawString(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindBack.keyCode), this.x+25+5+(25/2-mc.fontRendererObj.getStringWidth(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindBack.keyCode))/2), (this.y+25+(25/2)), mc.gameSettings.keyBindBack.pressed ? 0 : -1);
		Minecraft.getMinecraft().fontRendererObj.drawString(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindRight.keyCode), this.x+25+25+5+5+(25/2-mc.fontRendererObj.getStringWidth(Minecraft.getMinecraft().gameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindRight.keyCode))/2), (this.y+25+(25/2)), mc.gameSettings.keyBindRight.pressed ? 0 : -1);
		if (buttons.get()) {
		Minecraft.getMinecraft().fontRendererObj.drawString("LEFT", this.x+40/2-mc.fontRendererObj.getStringWidth("LEFT")/2, (this.y+25+25+5+5+25/2)-mc.fontRendererObj.FONT_HEIGHT/2, mc.gameSettings.keyBindAttack.pressed ? 0 : -1);
		Minecraft.getMinecraft().fontRendererObj.drawString("RIGHT", this.x+40+5+40/2-mc.fontRendererObj.getStringWidth("RIGHT")/2, (this.y+25+25+5+5+25/2)-mc.fontRendererObj.FONT_HEIGHT/2, mc.gameSettings.keyBindUseItem.pressed ? 0 : -1);
		}
		this.setHeight(25+5+25+5+25);
		this.setWidth(25+5+25+5+25);
		super.draw();
	}
}
