package delta.event;

import me.bush.eventbus.event.Event;

public class Render3DEvent extends Event {
    public Render3DEvent() { }

    @Override
    protected boolean isCancellable() {
        return false;
    }
}
