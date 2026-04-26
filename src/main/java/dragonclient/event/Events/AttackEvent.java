package dragonclient.event.Events;

 import dragonclient.event.CancellableEvent;
import net.minecraft.entity.Entity;

public class AttackEvent extends CancellableEvent {
    public final Entity target;

    public AttackEvent(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }
}
