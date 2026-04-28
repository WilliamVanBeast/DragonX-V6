package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class IntegerSetting extends Setting<Integer> {
    private final int minimum;
    private final int maximum;
    private final String suffix;

    public IntegerSetting(String name, Integer value, int minimum, int maximum, String suffix,
            Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
    }

    public IntegerSetting(String name, Integer value, int minimum, int maximum, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, "", displayable);
    }

    public IntegerSetting(String name, Integer value, int minimum, int maximum, String suffix) {
        this(name, value, minimum, maximum, suffix, () -> true);
    }

    public IntegerSetting(String name, Integer value, int minimum, int maximum) {
        this(name, value, minimum, maximum, () -> true);
    }

    public void set(Number newValue) {
        set(newValue.intValue());
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        value = nbt.getInteger(name);
    }
}
