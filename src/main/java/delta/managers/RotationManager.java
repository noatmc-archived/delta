package delta.managers;


import delta.event.PacketEvent;
import delta.util.Wrapper;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.network.play.client.CPacketPlayer;

public class RotationManager implements Wrapper {
    private float yaw, pitch;

    public void setYaw(float newYaw) {
        yaw = newYaw;
    }

    public void setPitch(float newPItch) {
        pitch = newPItch;
    }

    @EventListener
    public void onPacketSent(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer) event.getPacket()).yaw = this.yaw;
            ((CPacketPlayer) event.getPacket()).pitch = this.pitch;
        }
    }
}
