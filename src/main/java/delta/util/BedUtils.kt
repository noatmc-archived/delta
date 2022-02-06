package delta.util

import delta.util.Wrapper.mc
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i


class BedUtils  {
    companion object {
        @JvmStatic
        fun isBedPlaceable(pos: BlockPos): Boolean {
            if (!mc.world.isAirBlock(pos)) {
                if (!mc.world.isAirBlock(pos.east(1))) return true
                else if (!mc.world.isAirBlock(pos.north(1))) return true
                else if (!mc.world.isAirBlock(pos.south(1))) return true
                else if (!mc.world.isAirBlock(pos.west(1))) return true
            }
            return false
        }

        @JvmStatic
        fun calculateBedDamage(pos: BlockPos, target: EntityPlayer): Float {
            return CrystalUtils.calculateDamagePhobos(pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, target)
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
            mc.player.swingArm(EnumHand.MAIN_HAND)
        }
    }
}