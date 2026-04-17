package dragonclient.event.Events;

import dragonclient.event.CancellableEvent;

public class ChatEvent extends CancellableEvent {
    public String message;

    public ChatEvent(String message) {
        this.message = message;
    }
}
