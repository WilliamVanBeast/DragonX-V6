package dragonclient.event.Events;

import dragonclient.event.Event;
import net.minecraft.world.World;

public class WorldChangedEvent extends Event {
    public World oldWorld, newWorld;

    public WorldChangedEvent(World oldWorld, World newWorld) {
        this.oldWorld = oldWorld;
        this.newWorld = newWorld;
    }
}
