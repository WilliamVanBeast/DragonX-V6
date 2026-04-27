package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.PacketSendEvent;
import dragonclient.event.Events.TickEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.module.settings.ListSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;

public class AutoTotem extends Module {
    private IntegerSetting health = new IntegerSetting("Health", 10, 1, 36);
    private ListSetting itemMode = new ListSetting("Item", new String[] {"Crystal", "Totem", "Sword", "Gapple"}, "Totem");
    private IntegerSetting delay = new IntegerSetting("Delay", 20, 0, 70);
    public AutoTotem() {
        super("Auto Totem", Category.COMBAT);
        addSettings(health, itemMode, delay);
        Dragon.eventManager.registerListener(this, TickEvent.class);
        Dragon.eventManager.registerListener(this, PacketSendEvent.class);
    }

        private float totemTime;

          public void onTickEvent(TickEvent event) {
        if (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory))
            return;
        int totemslot = health.get().floatValue() >= mc.player.getHealth() ? getItemSlot(Items.TOTEM_OF_UNDYING) : getItemSlot(switchTo());
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && totemslot != -1 &&
                System.currentTimeMillis() - totemTime > delay.get().floatValue()) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
            totemTime = System.currentTimeMillis();
        }else if(mc.player.getHeldItemOffhand().getItem() != switchTo() && totemslot != -1 &&
                System.currentTimeMillis() - totemTime > delay.get().floatValue()){
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
            totemTime = System.currentTimeMillis();
        }
    }

        public void onPacketSendEvent(PacketSendEvent event1) {
        if (event1.getPacket() instanceof CPacketClickWindow) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }
    public Item switchTo(){
        switch (itemMode.get()){
            case "Crystal":
                return Items.END_CRYSTAL;
            case "Gapple":
                return Items.GOLDEN_APPLE;
        }
        return Items.TOTEM_OF_UNDYING;
    }

        public static int getItemSlot(Item items) {
        for (int i = 0; i < 36; ++i) {
            Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item == items) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

}
