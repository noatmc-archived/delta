package delta.managers;


import delta.event.PacketEvent;
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
    public void restoreRotations() {
        yaw = mc.player.rotationYaw;
        pitch =mc.player.rotationPitch;
    }

    @EventListener
    public void onPacketSent(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && shouldRotate || (event.getPacket() instanceof CPacketPlayer && check())) {
            ((CPacketPlayer) event.getPacket()).yaw = this.yaw;
            ((CPacketPlayer) event.getPacket()).pitch = this.pitch;
            if  (check()) {
                setRotate(false);
            }
        }
    }

    boolean check() {
        if (mc.player == null) return true;
        return (yaw == mc.player.rotationYaw && pitch == mc.player.rotationPitch);
    }
}
