package delta.mixin.mixins;

import delta.DeltaCore;
import delta.module.modules.BedAura;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    public void onEntityAdded(Entity entityIn, CallbackInfo cbi) {
        DeltaCore.moduleManager.onEntityAdded(entityIn);
    }

//    @Inject(method = "addTileEntity", at=@At("HEAD"))
//    public void onTileEntitySpawned(TileEntity tile, CallbackInfoReturnable<Boolean> cir) {
//        BedAura bedAura = BedAura.getBedAura();
//        if (bedAura.isToggled() && tile instanceof TileEntityBed) {
//            bedAura.onBedSpawned((TileEntityBed) tile);
//        }
//    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    public void onEntityRemoved(Entity entityIn, CallbackInfo info) {
        DeltaCore.moduleManager.onEntityRemoved(entityIn);
    }

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    public void onBlockDestroyed(BlockPos pos, boolean dropBlock, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("what");
        DeltaCore.moduleManager.onBlockDestroyed(pos);
    }
}
