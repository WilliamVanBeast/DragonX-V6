package dragonclient.event.Events;

import dragonclient.event.Event;

public class PreMotionEvent extends Event {
    public float yaw;
    public float pitch;
    public boolean ground;
    public double x;
    public double y;
    public double z;

    public PreMotionEvent(float yaw, float pitch, boolean ground, double x, double y, double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
