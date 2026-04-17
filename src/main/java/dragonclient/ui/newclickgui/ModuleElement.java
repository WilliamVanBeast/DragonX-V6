package dragonclient.ui.newclickgui;

import java.util.List;

import dragonclient.module.Module;
import dragonclient.module.Setting;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.FloatSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.module.settings.KeySetting;
import dragonclient.module.settings.ListSetting;
import dragonclient.util.Colors;
import dragonclient.util.RenderUtil;
import net.lax1dude.eaglercraft.Keyboard;
import net.lax1dude.eaglercraft.KeyboardConstants;
import net.lax1dude.eaglercraft.Mouse;
import net.lax1dude.eaglercraft.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class ModuleElement extends ButtonElement {
    private final Module module;

    private boolean showSettings;
    private boolean wasPressed;
    private boolean wasPressedRight;

    public int slowlySettingsYPos;
    public int slowlyFade = 0;

    private int expandedHeight = 0;

    private float settingsWidth;

    public int i = 0;

    public ModuleElement(Module module) {
        super(null);
        this.setSettingsWidth(this.getWidth());
        this.displayName = module.getName();
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float button, boolean last) {
        if (module.isEnabled()) {
            if (i % 2 == 0) {
                RenderUtil.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() + 2,
                        hoverColor(Colors.enabledColor, hoverTime));
            } else {
                RenderUtil.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() + 2,
                        hoverColor(Colors.enabledColor2, hoverTime));
            }
        } else {
            if (i % 2 == 0) {
                RenderUtil.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() + 2,
                        hoverColor(Colors.disabledColor, hoverTime));
            } else {
                RenderUtil.drawRect(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() + 2,
                        hoverColor(Colors.disabledColor2, hoverTime));
            }

        }

        i++;

        GlStateManager.resetColor();
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        font.drawStringWithShadow(displayName, getX() + 3, getY() + 7, Colors.textColor);

        // font2.drawString(displayName, getX() + 3, getY() + 7, Colors.textColor);

        List<Setting<?>> values = module.getSettings();
        if (!values.isEmpty()) {
            if (this.isShowSettings()) {
                font.drawString("-", getX() + getWidth() - 9, getY() + getHeight() / 10, Colors.specialTextColor);
                int ypos = getY() + getHeight();
                for (Setting<?> value : values) {

                    int color = i % 2 == 0 ? Colors.disabledColor : Colors.disabledColor2;
                    if (!value.getCanDisplay().get())
                        continue;
                    if (value instanceof BooleanSetting) {
                        String text = value.getName();
                        float textWidth = font.getStringWidth(text);
                        if (textWidth > getSettingsWidth() - 8) {
                            text = text.substring(0, (int) ((getSettingsWidth() - 8) / font.getCharWidth('a'))) + "...";
                            textWidth = font.getStringWidth(text);
                        }
                        if (getSettingsWidth() < textWidth + 8) {
                            setSettingsWidth(textWidth + 8);
                        }

                        RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                color);

                        if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                            if (Mouse.isButtonDown(0) && isntPressed()) {
                                ((BooleanSetting) value).set(!((BooleanSetting) value).get());
                            }
                        }
                        GlStateManager.resetColor();
                        GlStateManager.enableBlend();
                        font.drawString(text, getX() + 2, ypos + 4,
                                ((BooleanSetting) value).get() ? Colors.enabledTextColor : Colors.disabledTextColor);
                        GlStateManager.disableBlend();
                        ypos += 12;
                    } else if (value instanceof ListSetting) {
                        String text = value.getName();
                        float textWidth = font.getStringWidth(text);
                        if (getSettingsWidth() < textWidth + 8) {
                            setSettingsWidth(textWidth + 8);
                        }
                        RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                color);
                        GlStateManager.resetColor();
                        font.drawString(text + ":§b " + (String) value.get(), getX() + 2, ypos + 4, Colors.textColor);
                        font.drawString(((ListSetting) value).openList ? "-" : "+",
                                (int) (getX() + getSettingsWidth() - 6),
                                ypos + 4,
                                Colors.specialTextColor);

                        if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                            if (Mouse.isButtonDown(1) && isntPressedRight()) {
                                ((ListSetting) value).openList = !((ListSetting) value).openList;
                            }

                            if (Mouse.isButtonDown(0) && isntPressed()) {
                                System.out.println("click");
                                ((ListSetting) value).nextValue();
                            }
                        }
                        ypos += 12;
                        int j = i + 1;
                        for (String s : ((ListSetting) value).getValues()) {
                            color = j % 2 == 0 ? Colors.disabledColor : Colors.disabledColor2;
                            float textWidth2 = font.getStringWidth(s);
                            if (getSettingsWidth() < textWidth2 + 8)
                                setSettingsWidth(textWidth2 + 8);
                            if (((ListSetting) value).openList) {
                                RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                        color);

                                if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                                    if (Mouse.isButtonDown(0) && isntPressed()) {
                                        ((ListSetting) value).changeValue(s);
                                    }
                                }

                                GlStateManager.resetColor();
                                font.drawString(">", getX() + 2, ypos + 4, Colors.specialTextColor);
                                boolean selected = ((ListSetting) value).get().equals(s);
                                float textStart = getSettingsWidth() - textWidth2 - 4;
                                if (textStart < 0)
                                    textStart = 0;
                                GlStateManager.enableBlend();
                                font.drawString(selected ? "§b" + s : s, (int) (getX() + textStart), ypos + 4, selected ? 0xffffffff : Colors.disabledTextColor);
                                GlStateManager.disableBlend();
                                ypos += 12;
                                j++;
                            }
                        }
                        i = j - 1;
                        color = i % 2 == 0 ? Colors.disabledColor : Colors.disabledColor2;
                    } else if (value instanceof FloatSetting) {
                        String text = value.getName() + "§f: §c"
                                + Math.round(((FloatSetting) value).get() * 100) / 100.0;
                        float textWidth = font.getStringWidth(text);
                        if (getSettingsWidth() < textWidth + 8) {
                            setSettingsWidth(textWidth + 8);
                        }
                        RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                color);

                        float sliderValue = getX() + 1 + (getSettingsWidth() - 6)
                                * (((FloatSetting) value).get() - ((FloatSetting) value).getMinimum())
                                / (((FloatSetting) value).getMaximum() - ((FloatSetting) value).getMinimum());
                        if (sliderValue < getX() + 1)
                            sliderValue = getX() + 1;
                        if (sliderValue > getX() + getSettingsWidth() - 6)
                            sliderValue = getX() + getSettingsWidth() - 6;
                        GlStateManager.enableBlend();
                        RenderUtil.drawRect(sliderValue, ypos + 2, sliderValue + 6, ypos + 14, Colors.sliderColor);
                        GlStateManager.disableBlend();
                        if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                            if (Mouse.isButtonDown(0)) {
                                float newValue = ((FloatSetting) value).getMinimum()
                                        + (((FloatSetting) value).getMaximum() - ((FloatSetting) value).getMinimum())
                                                * (mouseX - getX()) / getSettingsWidth();
                                ((FloatSetting) value).set(newValue);
                            }
                        }

                        text = value.getName() + "§f: §c" + Math.round(((FloatSetting) value).get() * 100) / 100.0;

                        GlStateManager.resetColor();
                        font.drawString(text, getX() + 2, ypos + 4, Colors.textColor);
                        ypos += 12;
                    } else if (value instanceof IntegerSetting) {
                        String text = value.getName() + "§f: §c" + ((IntegerSetting) value).get();
                        float textWidth = font.getStringWidth(text);
                        if (getSettingsWidth() < textWidth + 8) {
                            setSettingsWidth(textWidth + 8);
                        }
                        RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                color);

                        float sliderValue = getX() + 1 + (getSettingsWidth() - 6)
                                * (((IntegerSetting) value).get() - ((IntegerSetting) value).getMinimum())
                                / (((IntegerSetting) value).getMaximum() - ((IntegerSetting) value).getMinimum());
                        if (sliderValue < getX() + 1)
                            sliderValue = getX() + 1;
                        if (sliderValue > getX() + getSettingsWidth() - 6)
                            sliderValue = getX() + getSettingsWidth() - 6;
                        GlStateManager.enableBlend();
                        RenderUtil.drawRect(sliderValue, ypos + 2, sliderValue + 6, ypos + 14, Colors.sliderColor);
                        GlStateManager.disableBlend();
                        if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                            if (Mouse.isButtonDown(0)) {
                                float newValue = ((IntegerSetting) value).getMinimum()
                                        + (((IntegerSetting) value).getMaximum()
                                                - ((IntegerSetting) value).getMinimum())
                                                * (mouseX - getX()) / getSettingsWidth();
                                ((IntegerSetting) value).set((int) newValue);
                            }
                        }

                        text = value.getName() + "§f: §c" + ((IntegerSetting) value).get();

                        GlStateManager.resetColor();
                        font.drawString(text, getX() + 2, ypos + 4, Colors.textColor);
                        ypos += 12;
                    } else if (value instanceof KeySetting) {
                        String text = value.getName() + "§f: §c";
                        if (((KeySetting) value).get() == KeyboardConstants.KEY_NONE) {
                            text += "NONE";
                        } else {
                            text += Keyboard.getKeyName(((KeySetting) value).get());
                        }

                        float textWidth = font.getStringWidth(text);
                        if (getSettingsWidth() < textWidth + 8) {
                            setSettingsWidth(textWidth + 8);
                        }

                        RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                color);
                        
                        if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                            if (Mouse.isButtonDown(0) && isntPressed() && !((KeySetting) value).isListening()) {
                                ((KeySetting) value).setListening(true);
                            } else if (Mouse.isButtonDown(0) && isntPressed() && ((KeySetting) value).isListening()) {
                                ((KeySetting) value).setListening(false);
                            }
                        }

                        if (((KeySetting) value).isListening()) {
                            if (Keyboard.isCreated()) {
                                if (Keyboard.next()) {
                                    if (Keyboard.getEventKeyState()) {
                                        if (Keyboard.getEventKey() == KeyboardConstants.KEY_ESCAPE) {
                                            ((KeySetting) value).set(KeyboardConstants.KEY_NONE);
                                            ((KeySetting) value).setListening(false);
                                        } else {
                                            ((KeySetting) value).set(Keyboard.getEventKey());
                                            ((KeySetting) value).setListening(false);
                                        }
                                    }
                                }
                            }
                        }

                        GlStateManager.resetColor();
                        if (((KeySetting) value).isListening()) {
                            font.drawString("Listening...", getX() + 2, ypos + 4, Colors.specialTextColor);
                        } else {
                            font.drawString(text, getX() + 2, ypos + 4, Colors.textColor);
                        }

                        ypos += 12;
                    }
                    i++;
                }
                updatePressed();

                expandedHeight = ypos - getY() - getHeight();

            } else {
                expandedHeight = 0;
                font.drawString("+", getX() + getWidth() - 9, getY() + getHeight() / 10, Colors.specialTextColor);
            }
        }

    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovering(mouseX, mouseY) && isVisible()) {
            module.toggle();

            return true;
        }
        if (mouseButton == 1 && isVisible()) {
            if (isHovering(mouseX, mouseY))
                showSettings = !showSettings;
            return false;
        }
        return false;
    }

    public Module getModule() {
        return module;
    }

    public boolean isShowSettings() {
        return showSettings;
    }

    public boolean isntPressed() {
        return !wasPressed;
    }

    public boolean isntPressedRight() {
        return !wasPressedRight;
    }

    public void updatePressed() {
        wasPressed = Mouse.isButtonDown(0);
        wasPressedRight = Mouse.isButtonDown(1);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        setSettingsWidth(width);
    }

    public float getSettingsWidth() {
        return settingsWidth - 1;
    }

    public int getExpandedHeight() {
        return expandedHeight;
    }

    public void setSettingsWidth(float settingsWidth) {
        this.settingsWidth = settingsWidth;
    }

    private boolean isHovering(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
