package delta.util.packets;

import delta.util.Wrapper;
import net.minecraft.network.Packet;

public class PacketUtils implements Wrapper {

    public static void sendPacket(final Packet<?> packet) {
        if (mc.getConnection() != null) {
            mc.getConnection().sendPacket(packet);
        }
    }

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
}
