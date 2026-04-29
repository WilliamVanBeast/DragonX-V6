package dragonclient.module.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import dragonclient.Dragon;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.util.TimeUtil;

public class ChestStealer extends Module {

    private final IntegerSetting delay = new IntegerSetting("Delay", 80, 0, 300);
    public static BooleanSetting titleCheck = new BooleanSetting("Title Check", true);
    public static BooleanSetting freeLook = new BooleanSetting("Free Look", true);
    private final BooleanSetting reverse = new BooleanSetting("Reverse", false);
    private DescriptionSetting description = new DescriptionSetting("Description", "Automatically steals items from chests.");

    private final TimeUtil timer = new TimeUtil();

    public ChestStealer() {
        super("ChestStealer", Category.PLAYER);
        addSettings(delay, titleCheck, freeLook, reverse, description);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);

    }

    public void onPreMotionEvent(PreMotionEvent event) {
        if (mc.player.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.player.openContainer;
            String chestName = chest.getLowerChestInventory().getName();
            if (titleCheck.get() && !((chestName.contains("Chest") && !chestName.equals("Ender Chest")) || chestName.equals("LOW")))
                return;

            List<Integer> slots = Lists.newArrayList();
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                    slots.add(i);
                }
            }

            if (reverse.get()) Collections.reverse(slots);

            for (int slot : slots) {
                if (delay.get() == 0 || timer.hasReached( delay.get())) {
                    mc.playerController.windowClick(chest.windowId, slot, 0, ClickType.QUICK_MOVE, mc.player);
                }
            }

            if (slots.isEmpty() || this.isInventoryFull()) {
                mc.player.closeScreen();
            }
        }
    }

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldFreeLook() {
        if (freeLook.get() && Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            ContainerChest chest = (ContainerChest) Minecraft.getMinecraft().player.openContainer;
            String chestName = chest.getLowerChestInventory().getName();
            return !titleCheck.get() || (chestName.contains("Chest") && !chestName.equals("Ender Chest")) || chestName.equals("LOW");
        }
        return false;
    }

}