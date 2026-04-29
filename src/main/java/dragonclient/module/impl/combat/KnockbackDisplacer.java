package dragonclient.module.impl.combat;

import java.util.Random;

import dragonclient.Dragon;
import dragonclient.event.Events.AttackEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.FloatSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class KnockbackDisplacer extends Module {

    private DescriptionSetting  description = new DescriptionSetting("Description", "Displaces your knockback by changing your yaw when you get hit.");
    private static final Random randomGenerator = new Random();
    private FloatSetting angle = new FloatSetting("Angle", 180f, -180f, 180f);
    private FloatSetting randomRange = new FloatSetting("Angle", 0f, 0f, 180f);

    public KnockbackDisplacer() {
        super("KBDisplacer", Category.COMBAT);
        addSettings(description, angle, randomRange);
        Dragon.eventManager.registerListener(this, AttackEvent.class);
    }


    public void onAttackEvent(AttackEvent event) {
              Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        if (player == null || mc.world == null) {
            return;
        }
        
        if (!player.isSprinting()) {
            return;
        }
        
        float baseAngle = angle.get();
        float variation = randomRange.get();
        
        if (variation != 0) {
            baseAngle += (randomGenerator.nextFloat() * variation * 2.0f) - variation;
        }
        
        float originalYaw = player.rotationYaw;
        player.rotationYaw = originalYaw + baseAngle;

        if (player instanceof EntityPlayerSP) {
            ((EntityPlayerSP) player).onUpdateWalkingPlayer();
        }

        player.rotationYaw = originalYaw;
    }
}
