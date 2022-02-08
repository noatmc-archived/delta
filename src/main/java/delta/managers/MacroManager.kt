package delta.managers

import delta.macro.Macro
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard

class MacroManager {
    init {
        MinecraftForge.EVENT_BUS.register(this)
    }
    private var macros = mutableListOf<Macro>()
    fun addMacro(macro: Macro) {
        macros.add(macro)
    }
    fun delMacro(macro: Macro) {
        if (macros.contains(macro)) {
            macros.remove(macro)
        }
    }
    @SubscribeEvent
    fun onKey(key: InputEvent.KeyInputEvent) {
        for (macro in macros) {
            if (Keyboard.isKeyDown(macro.getKey())) {
                macro.execute()
            }
        }
    }
}