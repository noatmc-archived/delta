package delta.command.commands

import delta.DeltaCore
import delta.command.Command
import delta.util.MessageUtils
import delta.util.customfont.CFontRenderer
import java.awt.Font

class FontC: Command("font") {
    override fun command(msg: String) {
        val args = msg.split(" ")
        if (args.size >= 3) {
            if (args[1].equals("set", true)) {
                DeltaCore.fontRenderer = CFontRenderer(Font(args[2], Font.PLAIN, 18), true, true)
                MessageUtils.sendMessage("font setted to " + msg.replace("=font set ", ""))
            }
        } else {
            MessageUtils.sendMessage("usage: =font <set> <font>")
        }
    }
}