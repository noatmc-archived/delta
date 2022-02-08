package delta.managers;


import delta.event.PacketEvent;
import delta.mixin.mixins.accessor.ICPacketPlayer;
import delta.util.Wrapper;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.network.play.client.CPacketPlayer;

public class RotationManager implements Wrapper {
    private float yaw, pitch;
    private boolean shouldRotate = false;

    public void setYaw(float newYaw) {
        yaw = newYaw;
    }
    public void setRotate(boolean rotate) { shouldRotate = rotate; }
    public void setPitch(float newPItch) {
        pitch = newPItch;
    }

    @EventListener
    public void onPacketSent(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && shouldRotate) {
            ((ICPacketPlayer) event.getPacket()).setYaw(this.yaw);
            ((ICPacketPlayer) event.getPacket()).setPitch(this.pitch);
        }
    }
}
