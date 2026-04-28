package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class DoubleSetting extends Setting<Double> {
    private final double minimum;
    private final double maximum;
    private final String suffix;

    public DoubleSetting(String name, Double value, double minimum, double maximum, String suffix,
            Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.suffix = suffix;
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, "", displayable);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, String suffix) {
        this(name, value, minimum, maximum, suffix, () -> true);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum) {
        this(name, value, minimum, maximum, () -> true);
    }

    public void set(Number newValue) {
        set(newValue.doubleValue());
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setDouble(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        value = nbt.getDouble(name);
    }
}
