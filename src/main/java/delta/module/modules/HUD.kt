package delta.module.modules

import delta.DeltaCore
import delta.module.Category
import delta.module.Module
import delta.util.customfont.CFontRenderer
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import java.awt.Font

class HUD: Module("HUD", "wtf hud!", Category.RENDER) {
    var x = setting("X", 1000.0, 0.0, 1000.0, true)
    override fun onEnable() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this)
    }

    @SubscribeEvent
    fun onRender2D(event: RenderGameOverlayEvent.Text) {
        var k = 0
        for (module in DeltaCore.moduleManager.sorted()) {
            DeltaCore.fontRenderer.drawString(
                module.name,
                (x.dVal - DeltaCore.fontRenderer.getStringWidth(module.fullString)).toFloat(),
                (10 + ((DeltaCore.fontRenderer.height + 2 ) * k)).toFloat(),
                Color(255, 255, 255).hashCode()
            )
            val boldCFontRenderer = CFontRenderer(Font(DeltaCore.fontRenderer.font.name, Font.BOLD, 18), true, true)
            boldCFontRenderer.drawString(
                " " + module.hudString,
                (x.dVal - DeltaCore.fontRenderer.getStringWidth(module.hudString)).toFloat(),
                (10 + ((DeltaCore.fontRenderer.height + 2 ) * k)).toFloat(),
                Color(200, 200 , 200).hashCode()
            )
            k++
        }
    }
}