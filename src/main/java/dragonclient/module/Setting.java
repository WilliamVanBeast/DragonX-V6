package dragonclient.module;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

public abstract class Setting<T> {
    protected final String name;
    protected T value;
    protected Supplier<Boolean> canDisplay;

    public Setting(String name, T value, Supplier<Boolean> canDisplay) {
        this.name = name;
        this.value = value;
        this.canDisplay = canDisplay;
    }

    public void set(T newValue) {
        if (newValue.equals(value))
            return;

        T oldValue = get();

        try {
            onChange(oldValue, newValue);
            changeValue(newValue);
            onChanged(oldValue, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T get() {
        return value;
    }

    public void changeValue(T value) {
        this.value = value;
    }

    public abstract void writeToNBT(NBTTagCompound nbt);

    public abstract void readFromNBT(NBTTagCompound nbt);

    protected void onChange(T oldValue, T newValue) {
    }

    protected void onChanged(T oldValue, T newValue) {
    }
    
    public String getName() {
        return name;
    }

    public Supplier<Boolean> getCanDisplay() {
        return canDisplay;
    }
}
