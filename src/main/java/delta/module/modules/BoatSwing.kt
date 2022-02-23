package delta.module.modules

import delta.module.Category
import delta.module.Module

class BoatSwing: Module("Boat Swing", "boat swing!?", Category.RENDER) {
    override fun onUpdate() {
        mc.player?.swingProgress = 1f;
    }
}