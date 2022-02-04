package delta.mixin.mixins;

import delta.DeltaCore;
import delta.event.PlayerDeathEvent;
import delta.util.Wrapper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {
    @Inject(method={"handleEntityMetadata"}, at={@At(value="RETURN")})
    public void handleEntityMetadataMixin(SPacketEntityMetadata packetIn, CallbackInfo ci) {
        try {
            if (Wrapper.mc.world != null && Wrapper.mc.world.getEntityByID(packetIn.getEntityId()) instanceof EntityPlayer && ((EntityPlayer) Objects.requireNonNull(Wrapper.mc.world.getEntityByID(packetIn.getEntityId()))).getHealth() <= 0.0f) {
                DeltaCore.EVENT_BUS.post(new PlayerDeathEvent((EntityPlayer) Wrapper.mc.world.getEntityByID(packetIn.getEntityId())));
            }
        } catch (Exception e) {
            // e cope harder
        }
    }
}
