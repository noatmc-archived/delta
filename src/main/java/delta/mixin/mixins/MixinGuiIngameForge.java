package delta.mixin.mixins;

import delta.DeltaCore;
import delta.event.Render2DEvent;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class MixinGuiIngameForge {
//    @Inject(method="renderHUDText", at = @At("HEAD"))
//    public void renderText(int msg, int w, CallbackInfo ci) {
//        DeltaCore.EVENT_BUS.post(new Render2DEvent());
//    }
}
