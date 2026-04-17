package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class FloatSetting extends Setting<Float> {
    private final float minimum;
    private final float maximum;
    private final String suffix;

    public FloatSetting(String name, Float value, float minimum, float maximum, String suffix,
            Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, "", displayable);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, String suffix) {
        this(name, value, minimum, maximum, suffix, () -> true);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum) {
        this(name, value, minimum, maximum, () -> true);
    }

    public void set(Number newValue) {
        set(newValue.floatValue());
    }

    public float getMinimum() {
        return minimum;
    }

    public float getMaximum() {
        return maximum;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setFloat(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        value = nbt.getFloat(name);
    }
}
