package delta.managers;

import delta.command.Command;
import delta.command.commands.FontC;
import delta.command.commands.Friend;
import delta.command.commands.MacroC;
import delta.event.PacketEvent;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;

public class CommandManager {
    ArrayList<Command> commands = new ArrayList<>();
    public CommandManager() {
        commands.add(new Friend());
        commands.add(new MacroC());
        commands.add(new FontC());
    }
    @EventListener
    public void onChat(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage) event.getPacket()).message.startsWith("=")) {
                event.setCancelled(true);
                for (Command command : commands) {
                    if (((CPacketChatMessage) event.getPacket()).message.contains(command.getName())) {
                        command.command(((CPacketChatMessage) event.getPacket()).message);
                    }
                }
            }
        }
    }
}
