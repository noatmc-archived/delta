package delta.util

import com.mojang.realmsclient.gui.ChatFormatting
import delta.util.Wrapper.mc
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.Style
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.event.HoverEvent

class MessageUtils {
    companion object {
        @JvmStatic
        fun sendMessage(msg: String) {
            mc.player?.let {
                val itc: ITextComponent = TextComponentString(msg).setStyle(Style().setHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponentString("a block game enthusiast"))));
                mc.ingameGUI.chatGUI.printChatMessageWithOptionalDeletion(itc, 5936);
            }
        }
        @JvmStatic
        fun sendWatermarkString(msg: String) {
            sendMessage("" + ChatFormatting.BLACK + "[" + ChatFormatting.DARK_GRAY + "Delta" + ChatFormatting.BLACK + "] " + ChatFormatting.RESET + msg)
        }
        @JvmStatic
        fun error() {
            sendMessage("" + ChatFormatting.RED + ChatFormatting.BOLD + "[Delta] - Error caught!")
        }
    }
}