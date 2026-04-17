package dragonclient.event.Events;

import dragonclient.event.CancellableEvent;

public class MoveEvent extends CancellableEvent {
    public double x;
    public double y;
    public double z;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}