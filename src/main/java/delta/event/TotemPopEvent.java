package delta.event;

import me.bush.eventbus.event.Event;
import net.minecraft.entity.Entity;

public class TotemPopEvent extends Event {
    public int count;
    public Entity entity;
    public TotemPopEvent(Entity entity, int count) {
        this.entity = entity;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    protected boolean isCancellable() {
        return false;
    }
}
