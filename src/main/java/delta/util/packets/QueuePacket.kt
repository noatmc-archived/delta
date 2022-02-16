package delta.util.packets

import delta.util.Timer
import net.minecraft.network.Packet

class QueuePacket(packet: Packet<*>, delay: Int) {
    var funny = packet
    private var del = delay
    private var timer: Timer = Timer()
    fun shouldSend(): Boolean {
        return timer.hasReached(del.toLong())
    }
}