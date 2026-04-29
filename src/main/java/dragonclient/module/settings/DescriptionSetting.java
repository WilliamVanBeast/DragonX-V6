package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class DescriptionSetting extends Setting<String> {

    public DescriptionSetting(String name, String value, Supplier<Boolean> displayable) {
        super(name, value, displayable);
    }

    public DescriptionSetting(String name, String value) {
        this(name, value, () -> true);
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
