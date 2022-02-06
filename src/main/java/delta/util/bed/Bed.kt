package delta.util.bed

import net.minecraft.util.math.BlockPos

class Bed(damage: Double, pos: BlockPos) {
    public val bD = damage
    public val bP = pos

    fun getDamage(): Double {
        return bD
    }

    fun getPos(): BlockPos {
        return bP
    }
}