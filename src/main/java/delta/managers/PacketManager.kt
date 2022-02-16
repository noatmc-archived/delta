package delta.managers

import delta.util.packets.PacketUtils
import delta.util.packets.QueuePacket
import net.minecraft.network.Packet

class PacketManager {
    private var packets = mutableListOf<QueuePacket>()
    fun onThread() {
        for (packet in packets) {
            if (packet.shouldSend()) {
                try {
                    PacketUtils.sendPacketInstantly(packet.funny)
                    packets.remove(packet)
                } catch (exc: Exception) {
                    // cope
                }
            }
        }
    }
    fun queuePacket(packet: Packet<*>, delay: Int) {
        packets.add(QueuePacket(packet, delay))
    }
}