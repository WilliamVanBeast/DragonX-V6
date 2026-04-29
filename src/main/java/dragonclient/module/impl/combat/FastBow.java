package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.FloatSetting;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class FastBow extends Module {

    private DescriptionSetting  description = new DescriptionSetting("Description", "Allows you to shoot your bow faster.");
    private final FloatSetting delay = new FloatSetting("Delay", 3.0f, 1.0f, 10.0f);
    public FastBow() {
        super("FastBow", Category.COMBAT);
        addSettings(description, delay);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }

        public void onUpdateEvent(UpdateEvent event) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive()
                && mc.player.getItemInUseMaxCount() >= delay.get()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
        }
    }

    
}
