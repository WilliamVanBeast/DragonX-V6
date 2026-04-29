package dragonclient.module.impl.player;

import dragonclient.Dragon;
import dragonclient.event.Events.PostMotionEvent;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.util.MovementUtil;
import dragonclient.util.TimeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {


    private DescriptionSetting description = new DescriptionSetting("Description", "Automatically equips armor for you.");
    private final IntegerSetting delay = new IntegerSetting("Delay", 150, 0, 300);
    private final BooleanSetting onlyWhileNotMoving = new BooleanSetting("Only while not moving", true);
    private final BooleanSetting invOnly = new BooleanSetting("Inventory only", true);

    private final TimeUtil timer = new TimeUtil();

    public AutoArmor() {
        super("AutoArmor", Category.PLAYER);
        addSettings(description, delay, onlyWhileNotMoving, invOnly);
        Dragon.eventManager.registerListener(this, PostMotionEvent.class);
    }

    public void onPostMotionEvent(PostMotionEvent event) {
        if ((invOnly.get() && !(mc.currentScreen instanceof GuiInventory)) || (onlyWhileNotMoving.get() && MovementUtil.isMoving())) {
            return;
        }
        if (mc.player.openContainer instanceof ContainerChest) {
            // so it doesn't put on armor immediately after closing a chest
            timer.reset();
        }
        if (timer.hasReached(delay.get())) {
            for (int armorSlot = 5; armorSlot < 9; armorSlot++) {
                if (equipBest(armorSlot)) {
                    timer.reset();
                    break;
                }
            }
        }
    }

      private boolean equipBest(int armorSlot) {
        int equipSlot = -1, currProt = -1;
        ItemArmor currItem = null;
        ItemStack slotStack = mc.player.inventoryContainer.getSlot(armorSlot).getStack();
        if (slotStack != null && slotStack.getItem() instanceof ItemArmor) {
            currItem = (ItemArmor) slotStack.getItem();
            currProt = currItem.damageReduceAmount
                    + EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), mc.player.inventoryContainer.getSlot(armorSlot).getStack());
        }
        // find best piece
        for (int i = 9; i < 45; i++) {
            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
            if (is != null && is.getItem() instanceof ItemArmor) {
                int prot = ((ItemArmor) is.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(0), is);
                if ((currItem == null || currProt < prot) && isValidPiece(armorSlot, (ItemArmor) is.getItem())) {
                    currItem = (ItemArmor) is.getItem();
                    equipSlot = i;
                    currProt = prot;
                }
            }
        }
        // equip best piece (if there is a better one)
        if (equipSlot != -1) {
            if (slotStack != null) {
                drop(armorSlot);
            } else {
                click(equipSlot, 0, true);
            }
            return true;
        }
        return false;
    }

    private boolean isValidPiece(int armorSlot, ItemArmor item) {
        String unlocalizedName = item.getUnlocalizedName();
        return armorSlot == 5 && unlocalizedName.startsWith("item.helmet")
                || armorSlot == 6 && unlocalizedName.startsWith("item.chestplate")
                || armorSlot == 7 && unlocalizedName.startsWith("item.leggings")
                || armorSlot == 8 && unlocalizedName.startsWith("item.boots");
    }

    public static void click(int slot, int mouseButton, boolean shiftClick) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot, mouseButton, ClickType.QUICK_MOVE, Minecraft.getMinecraft().player);
    }

    public static void drop(int slot) {
        Minecraft.getMinecraft().playerController.windowClick(0, slot, 1, ClickType.THROW, Minecraft.getMinecraft().player);
    }

    public static void swap(int slot, int hSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot, hSlot, ClickType.SWAP, Minecraft.getMinecraft().player);
    }
    
}
