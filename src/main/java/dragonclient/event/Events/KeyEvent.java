package dragonclient.event.Events;

import dragonclient.event.Event;

public class KeyEvent extends Event {
    public final int key;

    public KeyEvent(int key) {
        this.key = key;
    }
}
