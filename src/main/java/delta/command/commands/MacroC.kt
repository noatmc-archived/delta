package delta.command.commands

import delta.command.Command
import delta.macro.Macro
import delta.util.MessageUtils
import delta.util.friends.Friends
import delta.DeltaCore.macroManager as core

open class MacroC: Command("macro") {
    override fun command(msg: String) {
        val args = msg.split(" ")
        if (args.size >= 4) {
            if (args[1].equals("add", true)) {
                core.addMacro(Macro(args[2], msg.replace("=macro add " + args[2] + " ", "")))
            } else if (args[1].equals("del", true)) {
                Friends.delFriend(args[2])
                core.delMacro(Macro(args[2], msg.replace("=macro del " + args[2] + " ", "")))
            }
        } else {
            MessageUtils.sendMessage("usage: =macro <add/del> <key> <message>")
        }
    }
}
