package dragonclient.module.impl.combat;


import dragonclient.Dragon;
import dragonclient.event.Events.UpdateEvent;
import dragonclient.module.Category;
import dragonclient.module.settings.DescriptionSetting;
import dragonclient.module.settings.FloatSetting;
import dragonclient.module.settings.IntegerSetting;
import dragonclient.util.TimeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.EnumHand;

public class AntiFireball extends dragonclient.module.Module {

   private DescriptionSetting description = new DescriptionSetting("Description", "Attacks fireballs around you.");
   public static EntityFireball target = null;
   public TimeUtil timeUtil = new TimeUtil();

   private FloatSetting range = new FloatSetting("Range", 6.0f, 3.0f, 6.0f);
   private IntegerSetting cps = new IntegerSetting("Cps", 17, 1, 20);

   public AntiFireball() {
      super("AntiFireball", Category.COMBAT);
      addSettings(range, cps, description);
      Dragon.eventManager.registerListener(this, UpdateEvent.class);
   }

   @Override
   public void onDisable() {
      this.timeUtil.reset();
      super.onDisable();
   }

   @Override
   public void onUpdateEvent(UpdateEvent event) {
        float CPS = cps.get();
         target = this.searchTargets();
         if (target != null && this.timeUtil.hasReached((long)(1000.0F / CPS))) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.playerController.attackEntity(mc.player, target);
            this.timeUtil.reset();
         }
   }

   public EntityFireball searchTargets() {
      float Range = range.get();
      EntityFireball player = null;
      double closestDist = 100000.0;

      for(Entity o : mc.world.loadedEntityList) {
         if (!o.getName().equals(mc.player.getName()) && o instanceof EntityFireball && mc.player.getDistanceToEntity(o) < Range) {
            double dist = (double)mc.player.getDistanceToEntity(o);
            if (dist < closestDist) {
               closestDist = dist;
               player = (EntityFireball)o;
            }
         }
      }

      return player;
   }
}