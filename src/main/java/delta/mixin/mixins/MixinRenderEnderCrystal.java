package delta.mixin.mixins;

import delta.DeltaCore;
import delta.module.modules.ModifyCrystal;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEnderCrystal.class)
public class MixinRenderEnderCrystal {
    @Shadow
    public ModelBase modelEnderCrystal;
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;


    @Redirect(method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void bottomRenderRedirect(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (ModifyCrystal.getINSTANCE().isToggled()) {
            GlStateManager.scale(ModifyCrystal.getINSTANCE().scale.getDVal(), ModifyCrystal.getINSTANCE().scale.getDVal(), ModifyCrystal.getINSTANCE().scale.getDVal());
            modelBase.render(entityIn, limbSwing, (float) (limbSwingAmount * ModifyCrystal.getINSTANCE().spinSpeed.getDVal()), (float) (ageInTicks * ModifyCrystal.getINSTANCE().bounce.getDVal()), netHeadYaw, headPitch, scale);
        } else {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    @Inject(method = {"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at = {@At("RETURN")}, cancellable = true)
    public void doRenderCrystal(EntityEnderCrystal entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (ModifyCrystal.getINSTANCE().isToggled()) {
            final float f3 = entity.innerRotation + partialTicks;
            float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
            f4 += f4 * f4;
            if (entity.shouldShowBottom()) {
                this.modelEnderCrystal.render(entity, 0.0f, (float) (f3 * 3.0f * ModifyCrystal.getINSTANCE().spinSpeed.getDVal()), (float) (f4 * 0.2f * ModifyCrystal.getINSTANCE().bounce.getDVal()), 0.0f, 0.0f, 0.0625f);
            }
        } else {
            final float f3 = entity.innerRotation + partialTicks;
            float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
            f4 += f4 * f4;
            if (entity.shouldShowBottom()) {
                this.modelEnderCrystal.render(entity, 0.0f, (f3 * 3.0f), (f4 * 0.2f), 0.0f, 0.0f, 0.0625f);
            }
        }
    }
}
