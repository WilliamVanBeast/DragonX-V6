package dragonclient.event.Events;

import dragonclient.event.Event;

public class ActionEvent extends Event {
    public boolean sprinting;
    public boolean sneaking;

    public ActionEvent(boolean sprinting, boolean sneaking) {
        this.sprinting = sprinting;
        this.sneaking = sneaking;
    }
}
