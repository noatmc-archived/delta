package delta.mixin.mixins;

import delta.DeltaCore;
import delta.event.Render3DEvent;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Inject(method = "renderWorld", at = @At("HEAD"))
    public void onRender3D(float partialTicks, long finishTimeNano, CallbackInfo ci) {
        DeltaCore.EVENT_BUS.post(new Render3DEvent());
    }
}
