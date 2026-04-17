package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean value, Supplier<Boolean> displayable) {
        super(name, value, displayable);
    }

    public BooleanSetting(String name, Boolean value) {
        this(name, value, () -> true);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean(name, value);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        value = nbt.getBoolean(name);
    }
}
