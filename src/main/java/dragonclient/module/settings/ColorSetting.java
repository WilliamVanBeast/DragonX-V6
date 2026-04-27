package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A setting for storing and managing color values in ARGB format.
 * Color is stored as a single integer: (A << 24) | (R << 16) | (G << 8) | B
 */
public class ColorSetting extends Setting<Integer> {
    private final int defaultValue;
    public boolean pickerOpen = false;

    public ColorSetting(String name, int value, Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.defaultValue = value;
    }

    public ColorSetting(String name, int value) {
        this(name, value, () -> true);
    }

    public ColorSetting(String name, int red, int green, int blue, int alpha, Supplier<Boolean> displayable) {
        this(name, toARGB(alpha, red, green, blue), displayable);
    }

    public ColorSetting(String name, int red, int green, int blue, int alpha) {
        this(name, toARGB(alpha, red, green, blue));
    }

    public ColorSetting(String name, int red, int green, int blue, Supplier<Boolean> displayable) {
        this(name, toARGB(255, red, green, blue), displayable);
    }

    public ColorSetting(String name, int red, int green, int blue) {
        this(name, toARGB(255, red, green, blue));
    }

    /**
     * Convert RGBA components to ARGB integer format
     */
    public static int toARGB(int alpha, int red, int green, int blue) {
        return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }

    /**
     * Convert ARGB integer to RGB integer (without alpha)
     */
    public int getRGB() {
        return value & 0xFFFFFF;
    }

    /**
     * Get the red component (0-255)
     */
    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    /**
     * Get the green component (0-255)
     */
    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    /**
     * Get the blue component (0-255)
     */
    public int getBlue() {
        return value & 0xFF;
    }

    /**
     * Get the alpha component (0-255)
     */
    public int getAlpha() {
        return (value >> 24) & 0xFF;
    }

    /**
     * Set color from RGBA components
     */
    public void setColor(int red, int green, int blue, int alpha) {
        set(toARGB(alpha, red, green, blue));
    }

    /**
     * Set color from RGB components (keeps current alpha)
     */
    public void setRGB(int red, int green, int blue) {
        setColor(red, green, blue, getAlpha());
    }

    /**
     * Set color from hex string (e.g., "FF0000" for red)
     */
    public void setHex(String hexColor) {
        try {
            if (hexColor.startsWith("#")) {
                hexColor = hexColor.substring(1);
            }
            int color = Integer.parseInt(hexColor, 16);
            set(color | 0xFF000000); // Ensure alpha is 255
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get color as hex string (e.g., "FF0000FF" for red with full alpha)
     */
    public String getHex() {
        return String.format("%08X", value);
    }

    /**
     * Reset color to default value
     */
    public void reset() {
        set(defaultValue);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(name)) {
            value = nbt.getInteger(name);
        }
    }
}
