package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.AttackEvent;
import dragonclient.event.Events.PreMotionEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.util.PacketUtil;
import net.minecraft.network.play.client.CPacketEntityAction;

public class WTap extends Module {

    private final BooleanSetting legit = new BooleanSetting("Legit", false);

    public WTap() {
        super("WTap", Category.COMBAT);
        addSettings(legit);
        Dragon.eventManager.registerListener(this, AttackEvent.class);
        Dragon.eventManager.registerListener(this, PreMotionEvent.class);
    }

        public static int ticks;

    @Override
    public void onAttackEvent(final AttackEvent event) {
        ticks = 0;
    }

    @Override
    public void onPreMotionEvent(final PreMotionEvent event) {
        ++ticks;

        if (mc.player.isSprinting()) {
            if (legit.get()) {
                if (ticks == 2) mc.player.setSprinting(false);
                if (ticks == 3) mc.player.setSprinting(true);
            } else {
                if (ticks < 10) {
                    PacketUtil.sendPacketNoEvent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    PacketUtil.sendPacketNoEvent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
            }
        }
    }

    
    
}
