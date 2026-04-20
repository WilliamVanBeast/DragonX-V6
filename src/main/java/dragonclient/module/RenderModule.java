package dragonclient.module;

import dragonclient.util.RenderUtil;
import dragonclient.util.java.awt.Color;
import net.lax1dude.eaglercraft.Mouse;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class RenderModule extends Module {
    public int x, y, lastX, lastY, width, height;
    private boolean dragging;
    public RenderModule(String name, Category cat, int x, int y, int width, int height) {
        super(name, cat);
        this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
    }
    

       public void draw() {}

    public void renderLayout(final int mouseX, final int mouseY) {
        if ((getX() + getWidth()) > GuiScreen.width) {
            this.x = GuiScreen.width - getWidth();
            dragging = false;
        } else if ((getY() + getHeight()) > GuiScreen.height) {
            this.y = GuiScreen.height - getHeight();
            dragging = false;
        } else if ((getX()) < 0) {
            this.x = 0;
            dragging = false;
        } else if ((getY()) < 0) {
            this.y = 0;
            dragging = false;
        }

        draw();

        if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
            if (!Mouse.isButtonDown(0)) this.dragging = false;
        }

        final boolean hovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + this.getHeight();

        Gui.drawRect(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), hovered ? 0x50FFFFFF : 0x40FFFFFF);
        RenderUtil.drawRectOutline(this.x, this.y, this.x + this.getWidth(), this.y + this.getHeight(), -1);
        mc.fontRendererObj.drawStringWithShadow(getName(), this.x, this.y - 10, new Color(255, 255, 255).getRGB());
        final boolean mouseOverX = (mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth());
        final boolean mouseOverY = (mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight());

        if (mouseOverX && mouseOverY && Mouse.isButtonDown(0) && !this.dragging) {
            this.lastX = x - mouseX;
            this.lastY = y - mouseY;
            this.dragging = true;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }
    
}
