package delta.module.modules

import com.mojang.realmsclient.gui.ChatFormatting
import delta.event.PacketEvent
import delta.module.Category
import delta.module.Module
import delta.util.MessageUtils
import delta.util.PlayerUtils
import me.bush.eventbus.annotation.EventListener
import net.minecraft.network.play.server.SPacketBlockBreakAnim

class CityAlert: Module("City Alert", "Alert you if you are being citied", Category.COMBAT) {
    @EventListener
    fun onPacketReceived(event: PacketEvent.Receive) {
        if (event.packet is SPacketBlockBreakAnim) {
            val packet: SPacketBlockBreakAnim = event.packet as SPacketBlockBreakAnim
            if (PlayerUtils.getCityableBlocks(mc.player).contains(packet.position)) {
                MessageUtils.sendMessage("" + ChatFormatting.BLACK + "[" + ChatFormatting.DARK_GRAY + "Delta" + ChatFormatting.BLACK + "] " + ChatFormatting.RESET + "Your city block is being broken")
            }
        }
    }
}