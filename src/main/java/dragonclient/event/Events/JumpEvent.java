package dragonclient.event.Events;

import dragonclient.event.CancellableEvent;

public class JumpEvent extends CancellableEvent {
    public float motion;
    public float yaw;

    public JumpEvent(float motion, float yaw) {
        this.motion = motion;
        this.yaw = yaw;
    }
}
