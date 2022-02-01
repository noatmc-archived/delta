package delta.event;

import me.bush.eventbus.event.Event;

public class UpdateWalkingPlayerEvent extends Event {
    @Override
    protected boolean isCancellable() {
        return false;
    }
}
