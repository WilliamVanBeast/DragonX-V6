package dragonclient.util.killaura;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

public class RotationsUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    private float yaw, pitch;
    private float lastYaw, lastPitch;

    public RotationsUtil(float rotationYaw, float rotationPitch) {
        lastYaw = yaw = rotationYaw;
        lastPitch = pitch = rotationPitch;
    }

    public void updateRotations(float rotationYaw, float rotationPitch) {
        lastYaw = yaw;
        lastPitch = pitch;

        float yawDiff = (rotationYaw - yaw);
        float pitchDiff = (rotationPitch - pitch);
        float gcd = (float) (Math.pow(mc.gameSettings.mouseSensitivity * 0.6 + 0.2, 3) * 1.2);

        float fixedYawDiff = yawDiff - (yawDiff % gcd);
        float fixedPitchDiff = pitchDiff - (pitchDiff % gcd);

        yaw += fixedYawDiff;
        pitch += fixedPitchDiff;

        pitch = Math.max(-90, Math.min(90, pitch));
    }

        public float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {
        final Minecraft mc = Minecraft.getMinecraft();

        final float yaw = rotations[0];
        final float pitch = rotations[1];

        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];

        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 1.2F;

        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;

        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);

        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;

        return new float[]{fixedYaw, fixedPitch};
    }
    
    public static float[] getRotationsFromPosition(double x, double y, double z) {
        double deltaX = x - mc.player.posX;
        double deltaY = y - mc.player.posY - mc.player.getEyeHeight();
        double deltaZ = z - mc.player.posZ;

        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float yaw = (float) Math.toDegrees(-Math.atan2(deltaX, deltaZ));
        float pitch = (float) Math.toDegrees(-Math.atan2(deltaY, horizontalDistance));

        return new float[]{yaw, pitch};
    }

    public static float[] getRotationToBlock(BlockPos blockPos) {
        double deltaX = blockPos.getX() + 0.5 - mc.player.posX;
        double deltaY = blockPos.getY() + 0.5 - 3.5 - mc.player.posY + mc.player.getEyeHeight();
        double deltaZ = blockPos.getZ() + 0.5 - mc.player.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) (Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else {
            if (deltaX > 0 && deltaZ < 0) {
                yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
            }
        }
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsToEntity(EntityLivingBase entity) {
        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;

        double finalY = mc.player.posY - y >= 0 ? y + entity.getEyeHeight() :
                -mc.player.posY - y < mc.player.getEyeHeight() ?
                        mc.player.posY + mc.player.getEyeHeight() : y;

        return getRotationsFromPosition(x, finalY, z);
    }
}