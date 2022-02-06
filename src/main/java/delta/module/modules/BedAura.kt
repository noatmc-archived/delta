package delta.module.modules

import delta.event.TickEvent
import delta.event.TickEvent.Pre
import delta.module.Category
import delta.module.Module
import delta.util.CrystalUtils
import delta.util.PlayerUtils
import delta.util.bed.Bed
import delta.util.packets.PacketUtils
import me.bush.eventbus.annotation.EventListener
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.tileentity.TileEntityBed
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import delta.util.BedUtils as bedHelper

/*
    hi this is noat
    shout out to bush and perry
    - perry for telling me how to add kotlin
    - bush for telling me how kotlin works and letting me read 711 code
 */
class BedAura : Module("Bed Aura", "Sleeping in nether on steroids", Category.COMBAT) {
    val range = setting("Range", 6.0, 0.0, 6.0, false)
    val minDamage = setting("Damage", 6.0, 0.0, 36.0, false)

    companion object {
        @JvmStatic
        fun getBedAura(): BedAura {
            return BedAura()
        }
    }
    @EventListener
    fun onPlayerUpdate(tickEvent: TickEvent.Pre) {
        if (fullNullCheck()) return
        bedPlace()
    }

//    @EventListener
//    fun onPlayerPostUpdate(tickEvent: TickEvent.Post) {
//        if (fullNullCheck()) return;
//        bedBreak();
//    }

//    fun onBedSpawned(bedEntity: TileEntityBed) {
//        f
//    }


    fun bedPlace() {
        val bed = getOptimalPlacePosition()
        if (bed != null) {
//            bedHelper.placeBed(bed, EnumFacing.DOWN)
            mc.playerController?.processRightClickBlock(
                mc.player,
                mc.world,
                bed,
                EnumFacing.UP,
                Vec3d(bed),
                EnumHand.MAIN_HAND
            )
        }
    }

    private fun getOptimalPlacePosition(): BlockPos? {
        var entityPlayer: EntityPlayer?
        val list = mutableListOf<Bed>()
        for (entity in mc.world.playerEntities) {
            if (entity != mc.player && entity.getDistance(mc.player) <= 11 && !entity.isDead) {
                entityPlayer = entity
                for (pos in CrystalUtils.getSphere(
                    PlayerUtils.getPlayerPos(mc.player),
                    range.dVal.toFloat(),
                    range.dVal.toInt(),
                    false,
                    true,
                    0
                )) {
                    if (bedHelper.isBedPlaceable(pos) && bedHelper.calculateBedDamage(
                            pos,
                            entityPlayer
                        ) >= minDamage.dVal
                    ) {
                        list.add(Bed(bedHelper.calculateBedDamage(pos, entityPlayer).toDouble(), pos))
                    }
                }
            }
        }
        if (list.isEmpty()) return null
        list.sortBy { it.bD }
        return list[0].bP

    }
}