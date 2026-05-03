package dragonclient.event.Events;

import dragonclient.event.Event;

public class Render3DEvent extends Event {
    public final float partialTicks;
    private float ticks;

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getTicks() {
        return ticks;
    }

    public void setTicks(float ticks) {
        this.ticks = ticks;
    }

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
