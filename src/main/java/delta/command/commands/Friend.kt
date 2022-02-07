package delta.command.commands

import delta.command.Command
import delta.util.MessageUtils
import delta.util.friends.Friends

open class Friend: Command("friend") {
    override fun command(msg: String) {
        val args = msg.split(" ")
        if (args.size == 3) {
            if (args[1].equals("add", true)) {
                Friends.addFriend(args[2])
            } else if (args[1].equals("del", true)) {
                Friends.delFriend(args[2])
            }
        } else {
            MessageUtils.sendMessage("usage: =friend <add/del> <username>")
        }
    }
}