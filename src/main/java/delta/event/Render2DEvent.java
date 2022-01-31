package delta.event;

import me.bush.eventbus.event.Event;

public class Render2DEvent extends Event {
    public Render2DEvent() { }

    @Override
    protected boolean isCancellable() {
        return false;
    }
}
