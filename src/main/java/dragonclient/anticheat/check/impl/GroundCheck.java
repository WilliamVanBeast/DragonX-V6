package dragonclient.anticheat.check.impl;

import dragonclient.anticheat.check.Check;
import dragonclient.anticheat.data.PlayerData;
import dragonclient.util.BlockUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;


@Check.Info(name = "GroundSpoof")
public class GroundCheck extends Check {

    public GroundCheck(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet<?> event) {
        BlockPos underPlayer = new BlockPos(getData().getPlayer().posX, getData().getPlayer().posY - 1.0D, getData().getPlayer().posZ);

        boolean invalid = !BlockUtil.isCollidingOnGround(underPlayer, 0.8D) && getData().getMovementTracker().isOnGround();

        if(invalid) {
            if(increaseBuffer() > 4) {
                this.flag();
            }
        }
        reduceBuffer(0.05);
    }

}
