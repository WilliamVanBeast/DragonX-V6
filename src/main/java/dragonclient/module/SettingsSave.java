package dragonclient.module;

import java.io.IOException;

import dragonclient.Dragon;
import dragonclient.ui.newclickgui.Panel;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.EaglerInputStream;
import net.lax1dude.eaglercraft.EaglerOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class SettingsSave {
    private static NBTTagCompound saveModuleToNBT(Module module) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", module.getName());
        NBTTagCompound settings = new NBTTagCompound();
        for (Setting<?> setting : module.getSettings()) {
            NBTTagCompound settingNBT = new NBTTagCompound();
            setting.writeToNBT(settingNBT);
            settings.setTag(setting.getName(), settingNBT);
        }
        nbt.setTag("settings", settings);
        return nbt;
    }

    private static void loadModuleFromNBT(Module module, NBTTagCompound nbt) {
        NBTTagCompound settings = nbt.getCompoundTag("settings");
        for (Setting<?> setting : module.getSettings()) {
            if (settings.hasKey(setting.getName())) {
                setting.readFromNBT(settings.getCompoundTag(setting.getName()));
            }
        }

        module.onLoad();
    }

    private static void read(byte[] storage) {
        if (storage == null) {
            return;
        }
        NBTTagCompound nbt;
        try {
            nbt = CompressedStreamTools.readCompressed(new EaglerInputStream(storage));
        } catch (IOException e) {
            return;
        }

        if (nbt == null || nbt.hasNoTags()) {
            return;
        }

        for (Module module : Dragon.moduleManager.getModules()) {
            if (nbt.hasKey(module.getName())) {
                loadModuleFromNBT(module, nbt.getCompoundTag(module.getName()));
            }
        }

        for (Panel panel : Dragon.clickGui.panels) {
            NBTTagCompound panelNBT = nbt.getCompoundTag(panel.getName());
            if (panelNBT.hasKey("open")) {
                panel.setOpen(panelNBT.getBoolean("open"));
            }

            if (panelNBT.hasKey("x")) {
                panel.setX(panelNBT.getInteger("x"));
            }

            if (panelNBT.hasKey("y")) {
                panel.setY(panelNBT.getInteger("y"));
            }
        }
    }

    private static byte[] write() {
        NBTTagCompound nbt = new NBTTagCompound();
        for (Module module : Dragon.moduleManager.getModules()) {
            nbt.setTag(module.getName(), saveModuleToNBT(module));
        }
        
        for (Panel panel : Dragon.clickGui.panels) {
            NBTTagCompound panelNBT = new NBTTagCompound();
            panelNBT.setBoolean("open", panel.getOpen());
            panelNBT.setInteger("x", panel.getX());
            panelNBT.setInteger("y", panel.getY());
            nbt.setTag(panel.getName(), panelNBT);
        }

        EaglerOutputStream bao = new EaglerOutputStream();
        try {
            CompressedStreamTools.writeCompressed(nbt, bao);
        } catch (IOException e) {
            return null;
        }

        return bao.toByteArray();
    }

    public static void read() {
        read(EagRuntime.getStorage("settings"));
    }

    public static void save() {
        byte[] storage = write();
        if (storage != null) {
            EagRuntime.setStorage("settings", storage);
        }
    }
}
