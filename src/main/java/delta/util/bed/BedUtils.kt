package delta.util.bed

import delta.util.BlockUtils
import delta.util.Wrapper.mc
import delta.util.fade.FadePos
import delta.util.CrystalUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import java.awt.Color
import delta.DeltaCore as Core


class BedUtils  {
    companion object {
        @JvmStatic
        fun isBedPlaceable(pos: BlockPos): Boolean {
            return (mc.world.getBlockState(pos).block === Blocks.AIR || mc.world.getBlockState(pos)
                .block === Blocks.BED) && !BlockUtils.intersectsWithEntity(pos) && (mc.world.getBlockState(pos.down(1)).block != Blocks.AIR || mc.world.getBlockState(pos.down(1)).block != Blocks.LAVA || mc.world.getBlockState(pos.down(1)).block != Blocks.WATER)
        }

        @JvmStatic
        fun calculateBedDamage(pos: BlockPos, target: EntityPlayer): Float {
            return CrystalUtils.calculateDamagePhobos(
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 0.5,
                pos.z.toDouble() + 0.5,
                target
            )
        }

        @JvmStatic
        fun placeBed(pos: BlockPos, side: EnumFacing) {
            val neighbour = pos.offset(side)
            val opposite = side.opposite
            val hitVec = Vec3d(neighbour as Vec3i).add(0.5, 0.5, 0.5).add(Vec3d(opposite.directionVec).scale(0.5))
            mc.playerController.processRightClickBlock(
                mc.player,
                mc.world,
                neighbour,
                opposite,
                hitVec,
                EnumHand.MAIN_HAND
            )
//            mc.player.swingArm(EnumHand.MAIN_HAND)
        }
    }
}