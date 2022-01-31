package delta.managers;

import delta.event.PacketEvent;
import delta.event.TickEvent;
import delta.util.Wrapper;
import delta.util.rotation.Rotation;
import delta.util.rotation.RotationUtil;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

public class RotationManager implements Wrapper {
    private float serverYaw;
    private float serverPitch;

    private float nextserverYaw;
    private float nextServerPitch;
    private float nextRotationPriority;
    private boolean isFakeRotating;

    public void rotateToNext(Rotation rotation){

        float[] req = RotationUtil.getRotations(rotation.pos.x,rotation.pos.y,rotation.pos.z);


        //only override the rotation if your priority is higher or equal
        if(rotation.priority >= nextRotationPriority){

            if(rotation.strict){
                req[0] = RotationUtil.limitAngle(serverYaw, req[0], rotation.yawstep);
            }
            nextserverYaw = req[0];
            nextServerPitch = req[1];

            nextRotationPriority = rotation.priority;
            isFakeRotating = true;
        }

    }

    public void rotateToNext(Vec3d target){
        float[] req = RotationUtil.getRotations(target.x, target.y, target.z);
        nextserverYaw = req[0];
        nextServerPitch = req[1];
        isFakeRotating = true;
    }


    @EventListener
    public void onUpdate(TickEvent.Pre event){
        if(mc.player == null || mc.world == null) return;

        if(isFakeRotating){
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(
                    mc.player.posX,
                    mc.player.posY,
                    mc.player.posZ,
                    mc.player.rotationYaw,
                    mc.player.rotationPitch,
                    mc.player.onGround ));
        }
    }

    @EventListener
    public void onPacket(PacketEvent event){

        if(event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof  CPacketPlayer.PositionRotation){
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();

            packet.yaw = nextserverYaw;
            packet.pitch = nextServerPitch;

            serverYaw = packet.yaw;
            serverPitch = packet.pitch;

            if(!isFakeRotating){
                nextserverYaw = mc.player.rotationYaw;
                nextServerPitch = mc.player.rotationPitch;
            }

            isFakeRotating = false;
        }
    }

    public float getServerYaw() {
        return serverYaw;
    }

    public float getServerPitch() {
        return serverPitch;
    }
}
