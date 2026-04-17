package dragonclient.module.settings;

import java.util.function.Supplier;

import dragonclient.module.Setting;
import net.minecraft.nbt.NBTTagCompound;

public class KeySetting extends Setting<Integer> {
    private boolean listening;

    public KeySetting(String name, Integer value, Supplier<Boolean> displayable) {
        super(name, value, displayable);
    }

    public KeySetting(String name, Integer value) {
        super(name, value, () -> true);
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }

    public boolean isListening() {
        return listening;
    }

    public void set(Number newValue) {
        set(newValue.intValue());
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
