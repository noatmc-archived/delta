package delta.module.modules;

import delta.event.PacketEvent;
import delta.module.Category;
import delta.module.Module;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "fuck off ", Category.MOVEMENT);
    }

    @EventListener
    public void onPacketReceived(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion){
            event.setCancelled(true);
        }
    }
}
