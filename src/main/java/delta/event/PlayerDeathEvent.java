package delta.event;

import me.bush.eventbus.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerDeathEvent extends Event {
    @Override
    protected boolean isCancellable() {
        return false;
    }

    EntityPlayer entity;

    public PlayerDeathEvent(EntityPlayer entity) {
        this.entity = entity;
    }

    public EntityPlayer getEntity() {
        return entity;
    }
}
