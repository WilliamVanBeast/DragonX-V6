package dragonclient.util;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;


public class PlayerUtil {
    public static boolean isHoldingSword() {
        return Minecraft.getMinecraft().player.ticksExisted > 3 && Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND) != null && Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword;
    }

    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = Minecraft.getMinecraft().timer.field_194148_c;
        final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        RayTraceResult objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && Minecraft.getMinecraft().world != null) {

            final double d0 = Minecraft.getMinecraft().playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3d Vec3d = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(Vec3d);
            }

            final Vec3d Vec3d1 = Minecraft.getMinecraft().player.getVectorForRotation(pitch, yaw);
            final Vec3d Vec3d2 = Vec3d.addVector(Vec3d1.xCoord * d0, Vec3d1.yCoord * d0, Vec3d1.zCoord * d0);
            Entity pointedEntity = null;
            Vec3d Vec3d3 = null;
            final float f = 1.0F;
            final List<Entity> list = Minecraft.getMinecraft().world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(Vec3d1.xCoord * d0, Vec3d1.yCoord * d0, Vec3d1.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final RayTraceResult RayTraceResult = axisalignedbb.calculateIntercept(Vec3d, Vec3d2);

                if (axisalignedbb.isVecInside(Vec3d)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        Vec3d3 = RayTraceResult == null ? Vec3d : RayTraceResult.hitVec;
                        d2 = 0.0D;
                    }
                } else if (RayTraceResult != null) {
                    final double d3 = Vec3d.distanceTo(RayTraceResult.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        Vec3d3 = RayTraceResult.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && Vec3d.distanceTo(Vec3d3) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, Vec3d3, null, new BlockPos(Vec3d3));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new RayTraceResult(pointedEntity, Vec3d3);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            assert objectMouseOver != null;
            return mcPointedEntity == target;
        }

        return false;
    }

    public static RayTraceResult getMouseOver(final float yaw, final float pitch, final float range) {
        final float partialTicks = Minecraft.getMinecraft().timer.field_194148_c;
        final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        RayTraceResult objectMouseOver;
        Entity mmcPointedEntity = null;

        if (entity != null && Minecraft.getMinecraft().world != null) {

            final double d0 = Minecraft.getMinecraft().playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3d Vec3d = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(Vec3d);
            }

            final Vec3d Vec3d1 = Minecraft.getMinecraft().player.getVectorForRotation(pitch, yaw);
            final Vec3d Vec3d2 = Vec3d.addVector(Vec3d1.xCoord * d0, Vec3d1.yCoord * d0, Vec3d1.zCoord * d0);
            Entity pointedEntity = null;
            Vec3d Vec3d3 = null;
            final float f = 1.0F;
            final List<Entity> list = Minecraft.getMinecraft().world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(Vec3d1.xCoord * d0, Vec3d1.yCoord * d0, Vec3d1.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final RayTraceResult RayTraceResult = axisalignedbb.calculateIntercept(Vec3d, Vec3d2);

                if (axisalignedbb.isVecInside(Vec3d)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        Vec3d3 = RayTraceResult == null ? Vec3d : RayTraceResult.hitVec;
                        d2 = 0.0D;
                    }
                } else if (RayTraceResult != null) {
                    final double d3 = Vec3d.distanceTo(RayTraceResult.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        Vec3d3 = RayTraceResult.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && Vec3d.distanceTo(Vec3d3) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, Vec3d3, null, new BlockPos(Vec3d3));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new RayTraceResult(pointedEntity, Vec3d3);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    Entity mcPointedEntity = pointedEntity;
                }
            }

            assert objectMouseOver != null;
            return objectMouseOver;
        }

        return null;
    }
}
