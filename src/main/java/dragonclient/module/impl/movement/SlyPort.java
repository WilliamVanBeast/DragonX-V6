package dragonclient.module.impl.movement;

import java.util.Iterator;

import dragonclient.module.Category;
import dragonclient.module.Module;
import dragonclient.module.settings.BooleanSetting;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.DoubleSetting;
import dragonclient.util.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SlyPort extends Module  {
    private DescriptionSetting description = new DescriptionSetting("Description", "Teleport behind enemies.");
    private DoubleSetting range = new DoubleSetting("Range", 6.0D, 1.0D, 15.0D);
    private BooleanSetting aim = new BooleanSetting("Aim", true);
    private BooleanSetting sound = new BooleanSetting("Sound Effect", true);

    public SlyPort() {
        super("SlyPort", Category.MOVEMENT);
        addSettings(description, range, aim, sound);
    }

        public void onEnable() {
        Entity en = this.getEntity();
        if (en != null) {
            this.tp(en);
        }

        this.setEnabled(false);
    }

    
    private void tp(Entity en) {

        Vec3d vec = en.getLookVec();
        double x = en.posX - vec.xCoord * 2.5D;
        double z = en.posZ - vec.zCoord * 2.5D;
        mc.player.setPosition(x, mc.player.posY, z);
        if (aim.get()) {
            aim(en, 0.0F, false);
        }
        if(sound.get()){
            mc.player.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);

        }
    }

    public void aim(Entity en, float ps, boolean pc) {
         if (en != null) {
            float[] t = getTargetRotations(en);
            if (t != null) {
               float y = t[0];
               float p = t[1] + 4.0F + ps;
               if (pc) {
                  PacketUtil.sendPacket(new CPacketPlayer.Rotation(y, p, mc.player.onGround));
               } else {
                  mc.player.rotationYaw = y;
                  mc.player.rotationPitch = p;
               }
            }

         }
      }

        public float[] getTargetRotations(Entity q) {
         if (q == null) {
            return null;
         } else {
            double diffX = q.posX - mc.player.posX;
            double diffY;
            if (q instanceof EntityLivingBase) {
               EntityLivingBase en = (EntityLivingBase)q;
               diffY = en.posY + (double)en.getEyeHeight() * 0.9D - (mc.player.posY + (double)mc.player.getEyeHeight());
            } else {
               diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0D - (mc.player.posY + (double)mc.player.getEyeHeight());
            }

            double diffZ = q.posZ - mc.player.posZ;
            double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
            float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
            return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
         }
      }

    private Entity getEntity() {
        Entity en = null;
        double r = Math.pow(range.get(), 2.0D);
        double dist = r + 1.0D;
        Iterator var6 = mc.world.loadedEntityList.iterator();

        while(true) {
            Entity ent;
            do {
                do {
                    do {
                        do {
                            if (!var6.hasNext()) {
                                return en;
                            }

                            ent = (Entity)var6.next();
                        } while(ent == mc.player);
                    } while(!(ent instanceof EntityLivingBase));
                } while(((EntityLivingBase)ent).deathTime != 0);
            } while(!(ent instanceof EntityPlayer));

                double d = mc.player.getDistanceSqToEntity(ent);
                if (!(d > r) && !(dist < d)) {
                    dist = d;
                    en = ent;
                }
        }
    }

}
