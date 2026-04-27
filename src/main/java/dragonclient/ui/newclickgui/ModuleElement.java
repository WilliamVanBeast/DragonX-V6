package dragonclient.ui.newclickgui;

import java.util.List;

import dragonclient.module.Module;
import dragonclient.module.Setting;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.ColorSetting;
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
                    } else if (value instanceof ColorSetting) {
                        ColorSetting colorSetting = (ColorSetting) value;
                        String text = value.getName() + "§f: §c" + colorSetting.getHex().substring(0, 6);
                        float textWidth = font.getStringWidth(text);
                        if (getSettingsWidth() < textWidth + 8) {
                            setSettingsWidth(textWidth + 8);
                        }

                        RenderUtil.drawRect(getX() + 1, ypos + 2, getX() + getSettingsWidth(), ypos + 14,
                                color);

                        // Draw color swatch
                        int swatchX = (int) (getX() + getSettingsWidth() - 15);
                        RenderUtil.drawRect(swatchX, ypos + 3, swatchX + 12, ypos + 13, colorSetting.get());
                        // Draw darker border using the same color
                        int borderColor = darkenColor(colorSetting.get());
                        RenderUtil.drawRect(swatchX - 1, ypos + 2, swatchX + 13, ypos + 14, borderColor);

                        // Toggle picker open
                        if (isHovering(mouseX, mouseY, getX(), ypos + 2, (int) getSettingsWidth(), 11)) {
                            if (Mouse.isButtonDown(1) && isntPressedRight()) {
                                colorSetting.pickerOpen = !colorSetting.pickerOpen;
                            }
                        }

                        GlStateManager.resetColor();
                        font.drawString(text, getX() + 2, ypos + 4, Colors.textColor);
                        font.drawString(colorSetting.pickerOpen ? "-" : "+",
                                (int) (getX() + getSettingsWidth() - 6),
                                ypos + 4,
                                Colors.specialTextColor);
                        ypos += 12;

                        // Draw inline color picker if open
                        if (colorSetting.pickerOpen) {
                            ypos += drawColorPickerInline(colorSetting, getX() + 1, ypos, (int) getSettingsWidth() - 2, mouseX, mouseY, i);
                        }
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

    private int drawColorPickerInline(ColorSetting colorSetting, int x, int y, int width, int mouseX, int mouseY, int iCounter) {
        int startY = y;
        int gradientSize = Math.min(width - 4, 120);
        int padding = 2;
        int yPos = y + padding;
        
        // Extract HSV from current color
        int argb = colorSetting.get();
        int alpha = colorSetting.getAlpha();
        int red = colorSetting.getRed();
        int green = colorSetting.getGreen();
        int blue = colorSetting.getBlue();
        
        float[] hsv = rgbToHsv(red, green, blue);
        float currentHue = hsv[0];
        float currentSaturation = hsv[1];
        float currentValue = hsv[2];

        // Draw SV gradient (saturation/value picker)
        int gradientX = x + padding;
        int gradientY = yPos;
        
        for (int i = 0; i < gradientSize; i++) {
            for (int j = 0; j < gradientSize; j++) {
                float sat = (float) i / gradientSize;
                float val = 1.0f - (float) j / gradientSize;
                
                int[] rgb = hsvToRgb(currentHue, sat, val);
                int pixelColor = 0xFF000000 | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
                
                RenderUtil.drawRect(gradientX + i, gradientY + j, gradientX + i + 1, gradientY + j + 1, pixelColor);
            }
        }
        
        // Draw border around gradient
        RenderUtil.drawRect(gradientX - 1, gradientY - 1, gradientX + gradientSize + 1, gradientY, Colors.borderColor); // top
        RenderUtil.drawRect(gradientX - 1, gradientY + gradientSize, gradientX + gradientSize + 1, gradientY + gradientSize + 1, Colors.borderColor); // bottom
        RenderUtil.drawRect(gradientX - 1, gradientY, gradientX, gradientY + gradientSize, Colors.borderColor); // left
        RenderUtil.drawRect(gradientX + gradientSize, gradientY, gradientX + gradientSize + 1, gradientY + gradientSize, Colors.borderColor); // right
        
        // Draw crosshair for current selection
        int crossX = (int) (gradientX + gradientSize * currentSaturation);
        int crossY = (int) (gradientY + gradientSize * (1 - currentValue));
        RenderUtil.drawRect(crossX - 2, crossY, crossX + 2, crossY + 1, 0xFFFFFFFF);
        RenderUtil.drawRect(crossX, crossY - 2, crossX + 1, crossY + 2, 0xFFFFFFFF);
        
        // Handle SV picker click
        if (Mouse.isButtonDown(0) && isHoveringRect(mouseX, mouseY, gradientX, gradientY, gradientSize, gradientSize)) {
            currentSaturation = (float) (mouseX - gradientX) / gradientSize;
            currentValue = 1.0f - (float) (mouseY - gradientY) / gradientSize;
            currentSaturation = Math.max(0, Math.min(1, currentSaturation));
            currentValue = Math.max(0, Math.min(1, currentValue));
            updateColorFromHSV(colorSetting, currentHue, currentSaturation, currentValue, alpha);
        }
        
        yPos += gradientSize + 6;
        
        // Draw Hue gradient bar
        int hueHeight = 15;
        int hueX = gradientX;
        int hueY = yPos;
        
        for (int i = 0; i < gradientSize; i++) {
            float hue = (float) i / gradientSize * 360;
            int[] rgb = hsvToRgb(hue, 1, 1);
            int pixelColor = 0xFF000000 | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
            
            RenderUtil.drawRect(hueX + i, hueY, hueX + i + 1, hueY + hueHeight, pixelColor);
        }
        
        // Draw border around hue bar
        RenderUtil.drawRect(hueX - 1, hueY - 1, hueX + gradientSize + 1, hueY, Colors.borderColor); // top
        RenderUtil.drawRect(hueX - 1, hueY + hueHeight, hueX + gradientSize + 1, hueY + hueHeight + 1, Colors.borderColor); // bottom
        RenderUtil.drawRect(hueX - 1, hueY, hueX, hueY + hueHeight, Colors.borderColor); // left
        RenderUtil.drawRect(hueX + gradientSize, hueY, hueX + gradientSize + 1, hueY + hueHeight, Colors.borderColor); // right
        
        // Draw hue indicator
        int hueIndicatorX = (int) (hueX + (currentHue / 360) * gradientSize);
        RenderUtil.drawRect(hueIndicatorX - 1, hueY - 2, hueIndicatorX + 2, hueY + hueHeight + 2, 0xFFFFFFFF);
        
        // Handle hue click
        if (Mouse.isButtonDown(0) && isHoveringRect(mouseX, mouseY, hueX, hueY, gradientSize, hueHeight)) {
            currentHue = ((float) (mouseX - hueX) / gradientSize) * 360;
            currentHue = Math.max(0, Math.min(360, currentHue));
            updateColorFromHSV(colorSetting, currentHue, currentSaturation, currentValue, alpha);
        }
        
        yPos += hueHeight + 6;
        
        // Draw Alpha slider
        int alphaHeight = 15;
        int alphaX = hueX;
        int alphaY = yPos;
        
        for (int i = 0; i < gradientSize; i++) {
            float alphaF = (float) i / gradientSize;
            int alphaInt = (int) (alphaF * 255);
            int pixelColor = (alphaInt << 24) | 0xFFFFFFFF;
            
            RenderUtil.drawRect(alphaX + i, alphaY, alphaX + i + 1, alphaY + alphaHeight, pixelColor);
        }
        
        // Draw border around alpha bar
        RenderUtil.drawRect(alphaX - 1, alphaY - 1, alphaX + gradientSize + 1, alphaY, Colors.borderColor); // top
        RenderUtil.drawRect(alphaX - 1, alphaY + alphaHeight, alphaX + gradientSize + 1, alphaY + alphaHeight + 1, Colors.borderColor); // bottom
        RenderUtil.drawRect(alphaX - 1, alphaY, alphaX, alphaY + alphaHeight, Colors.borderColor); // left
        RenderUtil.drawRect(alphaX + gradientSize, alphaY, alphaX + gradientSize + 1, alphaY + alphaHeight, Colors.borderColor); // right
        
        // Draw alpha indicator
        int alphaIndicatorX = (int) (alphaX + (alpha / 255.0f) * gradientSize);
        RenderUtil.drawRect(alphaIndicatorX - 1, alphaY - 2, alphaIndicatorX + 2, alphaY + alphaHeight + 2, 0xFFFFFFFF);
        
        // Handle alpha click
        if (Mouse.isButtonDown(0) && isHoveringRect(mouseX, mouseY, alphaX, alphaY, gradientSize, alphaHeight)) {
            alpha = (int) ((mouseX - alphaX) / (float) gradientSize * 255);
            alpha = Math.max(0, Math.min(255, alpha));
            updateColorFromHSV(colorSetting, currentHue, currentSaturation, currentValue, alpha);
        }
        
        yPos += alphaHeight + 6;
        
        // Return height used
        return yPos - startY;
    }

    private boolean isHoveringRect(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private void updateColorFromHSV(ColorSetting colorSetting, float hue, float saturation, float value, int alpha) {
        int[] rgb = hsvToRgb(hue, saturation, value);
        colorSetting.setColor(rgb[0], rgb[1], rgb[2], alpha);
    }

    private float[] rgbToHsv(int r, int g, int b) {
        float rf = r / 255.0f;
        float gf = g / 255.0f;
        float bf = b / 255.0f;
        
        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float delta = max - min;
        
        float hue = 0;
        if (delta != 0) {
            if (max == rf) {
                hue = 60 * (((gf - bf) / delta) % 6);
            } else if (max == gf) {
                hue = 60 * (((bf - rf) / delta) + 2);
            } else {
                hue = 60 * (((rf - gf) / delta) + 4);
            }
        }
        if (hue < 0) hue += 360;
        
        float saturation = max == 0 ? 0 : delta / max;
        float value_out = max;
        
        return new float[] { hue, saturation, value_out };
    }

    private int[] hsvToRgb(float hue, float saturation, float value) {
        float c = value * saturation;
        float hPrime = hue / 60;
        float x = c * (1 - Math.abs(hPrime % 2 - 1));
        
        float r, g, b;
        if (hPrime < 1) {
            r = c; g = x; b = 0;
        } else if (hPrime < 2) {
            r = x; g = c; b = 0;
        } else if (hPrime < 3) {
            r = 0; g = c; b = x;
        } else if (hPrime < 4) {
            r = 0; g = x; b = c;
        } else if (hPrime < 5) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }
        
        float m = value - c;
        return new int[] {
            (int) ((r + m) * 255),
            (int) ((g + m) * 255),
            (int) ((b + m) * 255)
        };
    }

    private int darkenColor(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        
        // Darken by 40%
        red = (int) (red * 0.6f);
        green = (int) (green * 0.6f);
        blue = (int) (blue * 0.6f);
        
        return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
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
