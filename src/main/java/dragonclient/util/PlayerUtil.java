package dragonclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;

public class PlayerUtil {
        public static boolean isBlockUnder() {
        Minecraft mc = Minecraft.getMinecraft();
        for (int offset = 0; offset < mc.player.posY + mc.player.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = mc.player.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.world.getCollisionBoxes(mc.player, boundingBox).isEmpty())
                return true;
        }
        return false;
    }
}
