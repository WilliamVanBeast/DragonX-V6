package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class DoubleSetting extends Setting<Double> {
    private final double minimum;
    private final double maximum;
    private final double increment;
    private final String suffix;

    public DoubleSetting(String name, Double value, double minimum, double maximum, double increment, String suffix,
            Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
        this.suffix = suffix;
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, double increment, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, increment, "", displayable);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, double increment, String suffix) {
        this(name, value, minimum, maximum, increment, suffix, () -> true);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, double increment) {
        this(name, value, minimum, maximum, increment, () -> true);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, String suffix,
            Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, 0.1, suffix, displayable);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, Supplier<Boolean> displayable) {
        this(name, value, minimum, maximum, 0.1, "", displayable);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum, String suffix) {
        this(name, value, minimum, maximum, 0.1, suffix, () -> true);
    }

    public DoubleSetting(String name, Double value, double minimum, double maximum) {
        this(name, value, minimum, maximum, 0.1, () -> true);
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

    public double getIncrement() {
        return increment;
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
