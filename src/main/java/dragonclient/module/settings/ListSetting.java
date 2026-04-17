package dragonclient.module.settings;

import java.util.Arrays;
import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class ListSetting extends Setting<String> {
    private final String[] values;
    public boolean openList = false;

    public ListSetting(String name, String[] values, String value, Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.values = values;
    }

    public ListSetting(String name, String[] values, String value) {
        this(name, values, value, () -> true);
    }

    public boolean contains(String string) {
        return Arrays.stream(values).anyMatch(s -> s.equalsIgnoreCase(string));
    }

    @Override
    public void changeValue(String value) {
        for (String element : values) {
            if (element.equalsIgnoreCase(value)) {
                this.value = element;
                break;
            }
        }
    }

    public void nextValue() {
        int index = Arrays.asList(values).indexOf(value) + 1;
        if (index > values.length - 1)
            index = 0;
        value = values[index];
    }

    public String[] getValues() {
        return values;
    }

    public boolean isAny(String... values) {
        for (String value : values) {
            if (this.value.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        value = nbt.getString(name);
    }
}
