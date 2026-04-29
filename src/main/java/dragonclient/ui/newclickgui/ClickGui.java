package dragonclient.ui.newclickgui;

import static net.lax1dude.eaglercraft.opengl.RealOpenGLEnums.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dragonclient.Dragon;
import dragonclient.module.Module;
import dragonclient.module.Category;
import dragonclient.module.SettingsSave;
import dragonclient.util.Colors;
import dragonclient.util.RenderUtil;
import net.lax1dude.eaglercraft.Mouse;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen {
    public final List<Panel> panels = new ArrayList<>();
    private Panel clickedPanel;
    public double slide, progress = 0;
    public long lastMS = System.currentTimeMillis();
    private int mouseX;
    private int mouseY;

    private double scale = 1.0;

    public ClickGui() {
        final int width = 110;
        final int height = 18;

        int xPos = 112;

        int yPos = 30;

        for (final Category category : Category.values()) {
            if (xPos + 112 > 112 * 3) {
                xPos = 112;
                yPos += 30;
            } else {
                xPos += 112;
            }
            panels.add(new Panel(category.name(), xPos, yPos, width, height, false) {

                @Override
                public void setupItems() {
                    for (final Module module : Dragon.moduleManager.getModules()) {
                        if (module.getCategory() == category)
                            getElements().add(new ModuleElement(module));
                    }
                }
            });
        }
    }

    public void initGui() {
        slide = progress = 0;
        lastMS = System.currentTimeMillis();
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawGradientRect(0, 0, this.width, this.height, Colors.backgroundGradientTop,
                Colors.backgroundGradientBottom);
        if (progress < 1)
        progress = (float) (System.currentTimeMillis() - lastMS) / (600F / 1.4F);
        if (Dragon.animateClickGui()) {
        slide = easeOutBack(progress);
        } else {
        this.slide = 1;
        }

        mouseX /= scale;
        mouseY /= scale;

        this.mouseX = mouseX;
        this.mouseY = mouseY;

        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();

        if (Dragon.animateClickGui()) {
        GlStateManager.translate((1.0 - slide) * (width / 2.0), (1.0 - slide) *
        (height / 2.0), 0);
        GlStateManager.scale(scale * slide, scale * slide, scale * slide);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        for (Panel panel : panels) {
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        
        // Render descriptions on top
        renderDescriptions(mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        SettingsSave.save();
    }

    @Override
    public void updateScreen() {
        for (final Panel panel : panels) {
            for (final Element element : panel.getElements()) {
                if (element instanceof ButtonElement) {
                    final ButtonElement buttonElement = (ButtonElement) element;

                    if (buttonElement.isHovering(mouseX, mouseY)) {
                        if (buttonElement.hoverTime < 7)
                            buttonElement.hoverTime++;
                    } else if (buttonElement.hoverTime > 0)
                        buttonElement.hoverTime--;
                }

                if (element instanceof ModuleElement) {
                    if (((ModuleElement) element).getModule().isEnabled()) {
                        if (((ModuleElement) element).slowlyFade < 255)
                            ((ModuleElement) element).slowlyFade += 75;
                    } else if (((ModuleElement) element).slowlyFade > 0)
                        ((ModuleElement) element).slowlyFade -= 75;

                    if (((ModuleElement) element).slowlyFade > 255)
                        ((ModuleElement) element).slowlyFade = 255;

                    if (((ModuleElement) element).slowlyFade < 0)
                        ((ModuleElement) element).slowlyFade = 0;
                }
            }
        }
        super.updateScreen();
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        final int wheel = Mouse.getEventDWheel();
        for (int i = panels.size() - 1; i >= 0; i--)
            if (panels.get(i).handleScroll(mouseX, mouseY, wheel))
                break;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, final int mouseButton) {
        mouseX /= scale;
        mouseY /= scale;

        for (int i = panels.size() - 1; i >= 0; i--) {
            if (panels.get(i).mouseClicked(mouseX, mouseY, mouseButton)) {
                break;
            }
        }

        for (final Panel panel : panels) {
            panel.drag = false;

            if (mouseButton == 0 && panel.isHovering(mouseX, mouseY)) {
                clickedPanel = panel;
                break;
            }
        }

        if (clickedPanel != null) {
            clickedPanel.x2 = clickedPanel.x - mouseX;
            clickedPanel.y2 = clickedPanel.y - mouseY;
            clickedPanel.drag = true;

            panels.remove(clickedPanel);
            panels.add(clickedPanel);
            clickedPanel = null;
        }

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, final int state) {
        mouseX /= scale;
        mouseY /= scale;

        for (final Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    public static double easeOutBack(double x) {
        double c1 = 1.70158;
        double c3 = c1 + 1;
        return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
    }

    private void renderDescriptions(int mouseX, int mouseY) {
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        
        for (Panel panel : panels) {
            for (Element element : panel.getElements()) {
                if (element instanceof ModuleElement) {
                    ModuleElement moduleElement = (ModuleElement) element;
                    if (moduleElement.isHoveringModule(mouseX, mouseY)) {
                        dragonclient.module.settings.DescriptionSetting descSetting = moduleElement.getDescriptionSetting();
                        if (descSetting != null && !descSetting.get().isEmpty()) {
                            moduleElement.renderDescriptionTooltip(descSetting.get(), mouseX, mouseY, font);
                            return; // Only render one description at a time
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
