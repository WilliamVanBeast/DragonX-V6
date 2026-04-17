package dragonclient.util;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockUtil {

    public static boolean isOkBlock(final BlockPos blockPos) {
        final Block block = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid)
                && !(block instanceof BlockAir)
                && !(block instanceof BlockChest)
                && !(block instanceof BlockFurnace);
    }

    public static BlockPos getAimBlockPos() {
        final BlockPos playerPos = new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 1.0, Minecraft.getMinecraft().player.posZ);

        if ((Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() || !Minecraft.getMinecraft().player.onGround)
                && Minecraft.getMinecraft().player.moveForward == 0.0f
                && Minecraft.getMinecraft().player.moveStrafing == 0.0f
                && isOkBlock(playerPos.add(0, -1, 0))) {
            return playerPos.add(0, -1, 0);
        }

        BlockPos blockPos = null;
        final List<BlockPos> blockPosList = getBlockPos();

        if (!blockPosList.isEmpty()) {
            blockPosList.sort(Comparator.comparingDouble(BlockUtil::getDistanceToBlockPos));
            blockPos = blockPosList.get(0);
        }

        return blockPos;
    }

    public static List<BlockPos> getBlockPos() {
        final BlockPos playerPos = new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 1.0, Minecraft.getMinecraft().player.posZ);
        final List<BlockPos> blockPosList = new ArrayList<>();

        for (int x = playerPos.getX() - 2; x <= playerPos.getX() + 2; ++x) {
            for (int y = playerPos.getY() - 1; y <= playerPos.getY(); ++y) {
                for (int z = playerPos.getZ() - 2; z <= playerPos.getZ() + 2; ++z) {
                    final BlockPos currentPos = new BlockPos(x, y, z);

                    if (isOkBlock(currentPos)) {
                        blockPosList.add(currentPos);
                    }
                }
            }
        }

        if (!blockPosList.isEmpty()) {
            blockPosList.sort(Comparator.comparingDouble(blockPos -> Minecraft.getMinecraft().player.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)));
        }

        return blockPosList;
    }

    public static double getDistanceToBlockPos(final BlockPos blockPos) {
        double distance = 1337.0;

        for (float x = (float) blockPos.getX(); x <= blockPos.getX() + 1; x += (float) 0.2) {
            for (float y = (float) blockPos.getY(); y <= blockPos.getY() + 1; y += (float) 0.2) {
                for (float z = (float) blockPos.getZ(); z <= blockPos.getZ() + 1; z += (float) 0.2) {
                    final double d0 = Minecraft.getMinecraft().player.getDistance(x, y, z);

                    if (d0 < distance) {
                        distance = d0;
                    }
                }
            }
        }

        return distance;
    }

    public static boolean isBlockUnderPlayer() {
        for (int offset = 0; offset < Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight(); offset += 2) {

            AxisAlignedBB boundingBox = Minecraft.getMinecraft().player.getEntityBoundingBox().offset(0.0D, -offset, 0.0D);

            if (!Minecraft.getMinecraft().world.getCollisionBoxes(Minecraft.getMinecraft().player, boundingBox).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCollidingOnGround(BlockPos pos, double size) {
        for(double x = -size; x < size * 2; x += size) {
            for(double z = -size; z < size * 2; z += size) {
                Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z)).getBlock();

                if(block.isFullBlock(Minecraft.getMinecraft().world.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z))) && block != Blocks.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

}
