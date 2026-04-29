package dragonclient.module.impl.render;

import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import net.minecraft.potion.Potion;

public class AntiInvis extends Module {
    private DescriptionSetting description = new DescriptionSetting("Description","Removes invisibility from other players.");
    public AntiInvis() {
        super("AntiInvis", Category.RENDER);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
        addSettings(description);
    }

    public void onUpdateEvent(UpdateEvent event) {
                mc.world.playerEntities.stream()
                .filter(player -> player != mc.player && player.isPotionActive(Potion.getPotionById(14)))
                .forEach(player -> {
                    player.removePotionEffect(Potion.getPotionById(14));
                    player.setInvisible(false);
                });
    }
    
}
