package dragonclient.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
 
public class MovementUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return mc.player != null && mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f;
    }

    private static float getMoveYaw() {
        EntityPlayerSP player = mc.player;
        float moveYaw = player.rotationYaw;

        if (player.moveForward != 0F && player.moveStrafing == 0F) {
            moveYaw += (player.moveForward > 0) ? 0 : 180;
        } else if (player.moveForward != 0F) {
            if (player.moveForward > 0) {
                moveYaw += (player.moveStrafing > 0) ? -45 : 45;
            } else {
                moveYaw -= (player.moveStrafing > 0) ? -45 : 45;
            }
            moveYaw += (player.moveForward > 0) ? 0 : 180;
        } else if (player.moveStrafing != 0F) {
            moveYaw += (player.moveStrafing > 0) ? -90 : 90;
        }

        return moveYaw;
    }

    public static double getSpeed() {
        return Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }

    public static void strafe() {
        strafe((float) getSpeed());
    }


    public static void strafe(final float speed) {
        double shotSpeed = Math.sqrt((mc.player.motionX * mc.player.motionX) + (mc.player.motionZ * mc.player.motionZ));
        double fixSpeed = (shotSpeed * 1);
        double motionX = (mc.player.motionX * (0));
        double motionZ = (mc.player.motionZ * (0));

        if (!isMoving()) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
            return;
        }

        float yaw = getMoveYaw();
        mc.player.motionX = (((-Math.sin(Math.toRadians(yaw)) * fixSpeed) + motionX));
        mc.player.motionZ = (((Math.cos(Math.toRadians(yaw)) * fixSpeed) + motionZ));

        mc.player.motionX = -Math.sin(getDirection()) * speed;
        mc.player.motionZ = Math.cos(getDirection()) * speed;
    }

    public static double getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc.player.moveForward < 0F)
            forward = -0.5F;
        else if (mc.player.moveForward > 0F)
            forward = 0.5F;

        if (mc.player.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc.player.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public static boolean isOnIce() {
        final EntityPlayerSP player = mc.player;
        final Block blockUnder = mc.world.getBlockState(new BlockPos(player.posX, player.posY - 1.0, player.posZ)).getBlock();
        return blockUnder instanceof BlockIce || blockUnder instanceof BlockPackedIce;
    }


    public static double getBaseMoveSpeed(double customSpeed) {
        double baseSpeed = isOnIce() ? 0.258977700006 : customSpeed;
        if (mc.player.isPotionActive(Potion.getPotionById(getSpeedEffect()))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(getSpeedEffect())).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static int getSpeedEffect() {
        return mc.player.isPotionActive(Potion.getPotionById(getSpeedEffect())) ? mc.player.getActivePotionEffect(Potion.getPotionById(getSpeedEffect())).getAmplifier() + 1 : 0;
    }

    public static void accelerate(final float speed) {
        if (!isMoving())
            return;

        final double yaw = getDirection();
        mc.player.motionX += -Math.sin(yaw) * speed;
        mc.player.motionZ += Math.cos(yaw) * speed;
    }
}