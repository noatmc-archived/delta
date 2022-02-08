package delta.macro

import delta.util.Wrapper.mc
import org.lwjgl.input.Keyboard

class Macro(key: String, command: String) {
    private val bind = key
    private val toExecute = command
    fun getKey(): Int {
        return Keyboard.getKeyIndex(bind)
    }
    fun execute() {
        mc.player?.sendChatMessage(toExecute)
    }
}