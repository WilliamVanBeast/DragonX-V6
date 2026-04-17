package dragonclient.ui.newclickgui;

import dragonclient.util.Colors;
import dragonclient.util.RenderUtil;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.Minecraft;

public class ButtonElement extends Element {
    protected String displayName;
    protected int color = 0xffffff;

    public int hoverTime;

    public ButtonElement(String displayName) {
        createButton(displayName);
    }

    public void createButton(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float button, boolean last) {
        RenderUtil.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() + 2,
                hoverColor(Colors.enabledColor, hoverTime));
        GlStateManager.resetColor();
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(displayName, this.getX() + 3, this.getY() + 6,
                Colors.textColor);
        super.drawScreen(mouseX, mouseY, button, last);
    }

    @Override
    public int getHeight() {
        return 19;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + 16;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
