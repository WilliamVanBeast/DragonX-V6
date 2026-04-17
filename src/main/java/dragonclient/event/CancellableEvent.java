package dragonclient.event;

public class CancellableEvent extends Event {
    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancelEvent() {
        cancelled = true;
    }
}