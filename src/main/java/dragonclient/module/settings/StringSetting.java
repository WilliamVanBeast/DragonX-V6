package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class StringSetting extends Setting<String> {
    private final int maxLength;

    public StringSetting(String name, String value, int maxLength, Supplier<Boolean> displayable) {
        super(name, value, displayable);
        this.maxLength = maxLength;
    }

    public StringSetting(String name, String value, int maxLength) {
        this(name, value, maxLength, () -> true);
    }

    public StringSetting(String name, String value, Supplier<Boolean> displayable) {
        this(name, value, 255, displayable);
    }

    public StringSetting(String name, String value) {
        this(name, value, 255, () -> true);
    }

    public int getMaxLength() {
        return maxLength;
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
