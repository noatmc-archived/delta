package delta.module.modules

import delta.DeltaCore
import delta.module.Category
import delta.module.Module
import delta.setting.Setting
import delta.util.BlockUtils
import delta.util.InventoryUtils
import delta.util.PlayerUtils
import delta.util.rotation.RotationUtil
import net.minecraft.init.Items
import net.minecraft.util.math.Vec3d

class AntiRegear: Module("Anti Regear", "shulker breaking simulator", Category.COMBAT){
    private val range: Setting = setting("Range", 6.0, 0.1, 6.0, false)
    private val switch: Setting = setting("Switch", true)
    private val silent: Setting = setting("Silent", true)
    private val rotate: Setting = setting("Rotate", true)

    override fun onEnable() {
        if (fullNullCheck()) return;
        for (pos in BlockUtils.getSphere(PlayerUtils.getPlayerPos(mc.player), range.dVal.toFloat(), range.dVal.toInt(), false, true, 0)) {
            if (BlockUtils.isShulkerBox(mc.world.getBlockState(pos).block)) {
                if (rotate.bVal) {
                    DeltaCore.rotationManager.setYaw(RotationUtil.getRotations(Vec3d(pos))[0])
                    DeltaCore.rotationManager.setPitch(RotationUtil.getRotations(Vec3d(pos))[1])
                    DeltaCore.rotationManager.setRotate(true)
                }
                val oldSlot = mc.player.inventory.currentItem
                BlockUtils.doPacketMine(pos)
                if (switch.bVal) {
                    InventoryUtils.switchToItem(Items.DIAMOND_PICKAXE, silent.bVal)
                }
                if (silent.bVal) {
                    InventoryUtils.switchToSlot(oldSlot, silent.bVal)
                }
                if (rotate.bVal) {
                    DeltaCore.rotationManager.restoreRotations()
                }
            }
        }
        toggle()
    }
}