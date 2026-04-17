package dragonclient.event.Events;

import dragonclient.event.CancellableEvent;
import net.minecraft.util.math.BlockPos;

public class BlockBreakEvent extends CancellableEvent {
    public final BlockPos blockPos;

    public BlockBreakEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
