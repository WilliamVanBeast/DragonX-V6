package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class FloatSetting extends Setting<Float> {
    private final float minimum;
    private final float maximum;
    private final float increment;
    private final String suffix;

    public FloatSetting(String name, Float value, float minimum, float maximum, float increment, String suffix,
            Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.suffix = suffix;
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, float increment, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, increment, "", displayable);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, float increment, String suffix) {
        this(name, value, minimum, maximum, increment, suffix, () -> true);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, float increment) {
        this(name, value, minimum, maximum, increment, () -> true);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, String suffix,
            Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, 0.1f, suffix, displayable);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, 0.1f, "", displayable);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum, String suffix) {
        this(name, value, minimum, maximum, 0.1f, suffix, () -> true);
    }

    public FloatSetting(String name, Float value, float minimum, float maximum) {
        this(name, value, minimum, maximum, 0.1f, () -> true);
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

    public float getIncrement() {
        return increment;
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
