package delta.util.autocrystal

import net.minecraft.entity.item.EntityEnderCrystal

class Crystal(damage: Double, crystal: EntityEnderCrystal) {
    private val bD = damage
    private val bP = crystal

    fun getDamage(): Double {
        return bD
    }

    fun getCrystal(): EntityEnderCrystal {
        return bP
    }
}