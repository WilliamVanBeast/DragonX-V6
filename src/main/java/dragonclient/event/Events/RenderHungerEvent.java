package dragonclient.event.Events;

import dragonclient.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class RenderHungerEvent extends Event {

    private ScaledResolution scaledResolution;

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

}
