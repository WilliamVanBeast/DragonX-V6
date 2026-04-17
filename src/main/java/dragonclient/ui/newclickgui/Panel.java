package dragonclient.ui.newclickgui;

import java.util.ArrayList;
import java.util.List;

import dragonclient.util.Colors;
import dragonclient.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class Panel {

    private final String name;
    public int x;
    public int y;
    public int x2;
    public int y2;
    private final int width;
    private int height;
    private int scroll;
    private int dragged;
    private boolean open;
    public boolean drag;
    public final List<Element> elements;
    private final boolean visible;

    private final int scrollSpeed = 7;

    private float elementsHeight;

    public Panel(String name, int x, int y, int width, int height, boolean open) {
        this.name = name;
        this.elements = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.open = open;
        this.visible = true;

        setupItems();
    }

    public abstract void setupItems();

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!visible)
            return;

        final int maxElements = 100;

        if (drag) {
            int nx = x2 + mouseX;
            y = y2 + mouseY;
            if (nx > -1)
                x = nx;

            // if (ny > -1)
            //     y = ny;
        }

        elementsHeight = getElementsHeight() - 1;
        int y = this.y + height - scroll * scrollSpeed;
        int count = 0;
        if (!(open && !elements.isEmpty())) {
            RenderUtil.drawRoundedRect(x + 5, this.y + 4 - scroll * scrollSpeed, x + width - 5, this.y + height - 3 - scroll * scrollSpeed, 5, Colors.borderColor);
        } else {
            for (final Element element : elements) {
                y += element.getHeight() + 1;
                if (element instanceof ModuleElement) {
                    y += ((ModuleElement) element).getExpandedHeight();
                }
            }
            RenderUtil.drawRoundedRect(x + 5, this.y + 4 - scroll * scrollSpeed, x + width - 5, y + 9, 5, Colors.borderColor);
        }

        this.drawPanel(mouseX, mouseY);

        y = this.y + height;
        count = 0;
        
        int i = 1;
        if (open) {
            for (final Element element : elements) {
                if (count < maxElements) {
                    if (element instanceof ModuleElement) {
                        ((ModuleElement) element).i = i;
                    }
                    element.setScroll(scroll * scrollSpeed);
                    element.setLocation(x, y);
                    element.setWidth(getWidth());
                    element.drawScreen(mouseX, mouseY, partialTicks, count == 0);
                    y += element.getHeight() + 1;
                    if (element instanceof ModuleElement) {
                        y += ((ModuleElement) element).getExpandedHeight();
                        i += ((ModuleElement) element).i;
                        i++;
                    }
                    element.setVisible(true);
                } else {
                    element.setVisible(false);
                }
                count++;
            }
        }
        if (open && !elements.isEmpty()) {
            int color = i % 2 == 0 ? Colors.disabledColor : Colors.disabledColor2;
            RenderUtil.drawRoundedRect(x + 6, y + 8 - scroll * scrollSpeed, x + width - 6, y + 6 - scroll * scrollSpeed, 5, color, true, true, false, false);
        }
    }

    public void drawPanel(int mouseX, int mouseY) {
        if (open && !elements.isEmpty()) {
            RenderUtil.drawRoundedRect(x + 6, y + 5 - scroll * scrollSpeed, x + width - 6, y + height - 4 - scroll * scrollSpeed, 5, Colors.panelColor, false, false, true, true);
        } else {
            RenderUtil.drawRoundedRect(x + 6, y + 5 - scroll * scrollSpeed, x + width - 6, y + height - 4 - scroll * scrollSpeed, 5, Colors.panelColor);
        }

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        fontRenderer.drawStringWithShadow(name, x + 3, y + 6 - scroll * scrollSpeed,
                Colors.textColor);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!visible)
            return false;

        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            open = !open;
            return true;
        }

        for (final Element element : elements) {
            if (element.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!visible)
            return;

        drag = false;

        if (!open)
            return;

        for (final Element element : elements) {
            if (element.getY() <= getY() - scroll * scrollSpeed && element.mouseReleased(mouseX, mouseY, state)) {
                return;
            }
        }
    }

    public boolean handleScroll(int mouseX, int mouseY, int wheel) {
        scroll += wheel;

        return false;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int dragX) {
        this.x = dragX;
    }

    public void setY(int dragY) {
        this.y = dragY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getOpen() {
        return this.open;
    }

    public List<Element> getElements() {
        return elements;
    }

    private int getElementsHeight() {
        int height = 0;
        int count = 0;
        for (final Element element : elements) {
            if (count >= 30)
                continue;
            height += element.getHeight() + 1;
            ++count;
        }
        return height;
    }

    boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width
                && mouseY >= y - scroll * scrollSpeed
                && mouseY <= y + height - (open ? 2 : 0) - scroll * scrollSpeed;
    }
}
