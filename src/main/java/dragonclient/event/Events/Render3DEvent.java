package dragonclient.event.Events;

import dragonclient.event.Event;

public class Render3DEvent extends Event {
    public final float partialTicks;

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
