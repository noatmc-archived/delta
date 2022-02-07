package delta.util.friends

import com.mojang.realmsclient.gui.ChatFormatting
import delta.util.MessageUtils
import delta.util.Wrapper.mc
import net.minecraft.entity.player.EntityPlayer

class Friends {
    companion object {
        private var friends = mutableListOf<EntityPlayer>()
        @JvmStatic
        fun addFriend(name: String) {
            var check = false
            for (player in mc.world.playerEntities) {
                if (player.name.equals(name, true)) {
                    addFriend(player)
                    check = true
                }
            }
            if (!check) {
                MessageUtils.sendMessage("player can't be detected :(")
            }
        }
        @JvmStatic
        fun delFriend(name: String) {
            var check = false
            for (player in mc.world.playerEntities) {
                if (player.name.equals(name, true)) {
                    delFriend(player)
                    check = true
                }
            }
            if (!check) {
                MessageUtils.sendMessage("player can't be detected :(")
            }
        }
        @JvmStatic
        fun addFriend(player: EntityPlayer) {
            if (!friends.contains(player)) {
                friends.add(player)
                MessageUtils.sendMessage("Added " + ChatFormatting.BOLD + player.name + ChatFormatting.RESET + " as friend.")
            } else {
                MessageUtils.sendMessage(player.name + " is already friended!")
            }
        }
        @JvmStatic
        fun delFriend(player: EntityPlayer) {
            if (friends.contains(player)) {
                friends.remove(player)
                MessageUtils.sendMessage("Removed " + ChatFormatting.BOLD + player.name + ChatFormatting.RESET + " as friend.")
            } else {
                MessageUtils.sendMessage(player.name + " is already not friended!")
            }
        }
        @JvmStatic
        fun isFriend(player: EntityPlayer): Boolean {
            if (friends.contains(player)) {
                return true
            }
            return false
        }
    }
}