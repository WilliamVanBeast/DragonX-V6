package dragonclient.module.impl.render;

import java.awt.Color;
import java.util.ArrayList;

import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.ListSetting;
import net.minecraft.util.EnumParticleTypes;


public class Trails extends Module {
    private final ListSetting type = new ListSetting("Type", new String[]{"Heart", "Lava", "Smoke", "Cloud", "Flame", "Slime", "Water", "Firework"}, "Heart");
    public Trails() {
        super("Trails", Category.RENDER);
        addSettings(type);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }

    public void onUpdateEvent(UpdateEvent event) {
        if (mc.player.isSprinting()) {
            if(type.get().equalsIgnoreCase("Heart")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.HEART);
            } else if(type.get().equalsIgnoreCase("Lava")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.LAVA);
            } else if(type.get().equalsIgnoreCase("Smoke")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.REDSTONE);
            } else if(type.get().equalsIgnoreCase("Cloud")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.CLOUD);
            } else if(type.get().equalsIgnoreCase("Flame")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.FLAME);
            } else if(type.get().equalsIgnoreCase("Slime")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.SLIME);
            } else if(type.get().equalsIgnoreCase("Water")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.WATER_SPLASH);
            } else if(type.get().equalsIgnoreCase("FireWork")) {
                mc.effectRenderer.emitParticleAtEntity(mc.player, EnumParticleTypes.FIREWORKS_SPARK);
            }
         }
    }
}