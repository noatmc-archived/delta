package delta.managers;

import delta.command.Command;
import delta.command.commands.Friend;
import delta.command.commands.MacroC;
import delta.event.PacketEvent;
import delta.mixin.mixins.accessor.ICPacketChatMessage;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

public class CommandManager {
    ArrayList<Command> commands = new ArrayList<>();
    public CommandManager() {
        commands.add(new Friend());
        commands.add(new MacroC());
    }
    @EventListener
    public void onChat(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            if (((ICPacketChatMessage) packet).getMessage().startsWith("=")) {
                event.setCancelled(true);
                for (Command command : commands) {
                    if (((ICPacketChatMessage) packet).getMessage().contains(command.getName())) {
                        command.command(((ICPacketChatMessage) packet).getMessage());
                    }
                }
            }
        }
    }
}
