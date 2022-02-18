package delta.module.modules

import delta.DeltaCore
import delta.module.Category
import delta.module.Module
import delta.setting.Setting
import delta.util.RenderUtils
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

class HUD: Module("HUD", "wtf hud!", Category.RENDER) {
    var x: Setting = setting("X", 1000.0, 0.0, 1000.0, true)
    var lowercase: Setting = setting("Lowercase", true)
    var line = setting("Line", true)
    var r = setting("Red", 1.0, 1.0, 255.0, true)
    var g = setting("Green", 1.0, 1.0, 255.0, true)
    var b = setting("Blue", 1.0, 1.0, 255.0, true)
    var alpha = setting("Alpha", 1.0, 1.0, 255.0, true)
    override fun onEnable() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this)
    }

    @SubscribeEvent
    fun onRender2D(event: RenderGameOverlayEvent.Text) {
        val startColor = Color(r.dVal.toInt(), g.dVal.toInt(), b.dVal.toInt())
        val endColor = Color(r.dVal.toInt(), g.dVal.toInt(), b.dVal.toInt())
        var k = 0
        for (module in DeltaCore.moduleManager.sorted()) {
            for (i in 0..k) {
                endColor.darker()
            }
            RenderUtils.drawRect(
                (x.dVal - DeltaCore.fontRenderer.getStringWidth(module.fullString) - 10).toInt(),
                (10 + ((DeltaCore.fontRenderer.height + 2 ) * k)),
                DeltaCore.fontRenderer.getStringWidth(module.fullString) + 11,
                DeltaCore.fontRenderer.height + 2,
                Color(0, 0, 0, alpha.dVal.toInt())
            )
            DeltaCore.fontRenderer.drawString(
                lowcase(module.name),
                (x.dVal - DeltaCore.fontRenderer.getStringWidth(module.fullString)).toFloat(),
                (10 + ((DeltaCore.fontRenderer.height + 2 ) * k)).toFloat(),
                endColor.hashCode()
            )
            module.hudString?.let {
                DeltaCore.fontRenderer.drawString(
                    lowcase(" " + module.hudString),
                    (x.dVal - DeltaCore.fontRenderer.getStringWidth(module.hudString) - 2).toFloat(),
                    (10 + ((DeltaCore.fontRenderer.height + 2 ) * k)).toFloat(),
                    Color(255, 255 , 255).hashCode()
                )
            }
            k++
        }
    }

    fun lowcase(string: String): String {
        if (lowercase.bVal) {
            return string.toLowerCase()
        }
        return string
    }
}