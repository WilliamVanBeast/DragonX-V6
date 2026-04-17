package dragonclient.module.impl.combat;

import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.FloatSetting;
import dragonclient.module.settings.ListSetting;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;


public class Criticals extends Module {

    private ListSetting mode = new ListSetting("Mode", new String[]{
        "minijump",
        "legitjump",
        "customjump"
    }, "legitjump");

    private FloatSetting heighty = new FloatSetting("jumpheight", 0.10F, 0.01F, 2.00F,
            () -> mode.get().equalsIgnoreCase("customjump"));
    
    public Criticals() {
        super("Criticals", Category.COMBAT);
        addSettings(mode, heighty);
        Dragon.eventManager.registerListener(this, UpdateEvent.class);
    }

    public void onUpdateEvent(UpdateEvent event) {
            if(mode.get().equals("minijump")) {
                if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase) {
                doJumpCriticals();
                }
            } else if(mode.get().equals("legitjump")) {
                if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase) {
                dolegitCriticals();
                }
            } else if(mode.get().equals("customjump")) {
                 if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase) {
                    doCustomCriticals();
                    }
            }
        }

        public void doJumpCriticals() {
        if (!Minecraft.getMinecraft().player.isInWater() && !Minecraft.getMinecraft().player.isInsideOfMaterial(Material.LAVA) && Minecraft.getMinecraft().player.onGround) {
            Minecraft.getMinecraft().player.motionY = 0.1f;
            Minecraft.getMinecraft().player.fallDistance = 0.1f;
            Minecraft.getMinecraft().player.onGround = false;
        }
        }
        
        public void dolegitCriticals() {
            if (!Minecraft.getMinecraft().player.isInWater() && !Minecraft.getMinecraft().player.isInsideOfMaterial(Material.LAVA) && Minecraft.getMinecraft().player.onGround) {
                Minecraft.getMinecraft().player.jump();
            }
    }

    public void doCustomCriticals() {
        if (!Minecraft.getMinecraft().player.isInWater() && !Minecraft.getMinecraft().player.isInsideOfMaterial(Material.LAVA) && Minecraft.getMinecraft().player.onGround) {
            Minecraft.getMinecraft().player.motionY = heighty.get();
            Minecraft.getMinecraft().player.fallDistance = heighty.get();
            Minecraft.getMinecraft().player.onGround = false;
         }
}
}
