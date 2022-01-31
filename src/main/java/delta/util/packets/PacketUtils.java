package delta.util.packets;

import delta.util.Timer;
import delta.util.Wrapper;
import net.minecraft.network.Packet;
import net.minecraft.client.Minecraft;

public class PacketUtils implements Wrapper {
    /**
     * Confirms that {@link Minecraft#getConnection()} is not null, and then sends the packet to the server.
     *
     * @param packet The packet to send.
     */
    public static void sendPacket(final Packet<?> packet) {
        if (mc.getConnection() != null) {
            mc.getConnection().sendPacket(packet);
        }
    }

    /**
     * Confirms that {@link Minecraft#getConnection()} is not null, and then instantly sends the packet to the server.
     * This method does not flag any packet events, and skips any existing packet queue.
     * May not be thread safe. Only use for packets that are crucial to be dispatched as fast as possible, or those that shouldn't flag an event.
     *
     * @param packet The packet to send.
     */
    public static void sendPacketInstantly(final Packet<?> packet) {
        if (mc.getConnection() != null) {
            mc.getConnection().getNetworkManager().channel().writeAndFlush(packet);
        }
    }

    public static void sendPlayerPacket(Packet<?> packet) {
        if (mc.player != null) {
            mc.player.connection.sendPacket(packet);
        }
    }

    public static void sendPacket(PacketType type, Packet<?> packet) {
        if (type == PacketType.Normal) {
            sendPacket(packet);
        } else if (type == PacketType.Player) {
            sendPlayerPacket(packet);
        } else if (type == PacketType.Instant) {
            sendPacketInstantly(packet);
        }
    }

    public static void sendDelayPacket(PacketType type, Packet<?> packet, int delayMs) {
        Timer timer = new Timer();
        if (timer.hasReached(delayMs)) {
            if (type == PacketType.Normal) {
                sendPacket(packet);
            } else if (type == PacketType.Player) {
                sendPlayerPacket(packet);
            } else if (type == PacketType.Instant) {
                sendPacketInstantly(packet);
            }
        }
    }
}
