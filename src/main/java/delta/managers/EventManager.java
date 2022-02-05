package delta.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import delta.util.MessageUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class EventManager {
    public void onChat(ClientChatReceivedEvent event) {
        if (event.getMessage().getFormattedText().contains("Catuquei")) {
            event.setCanceled(true);
            MessageUtils.sendMessage(event.getMessage().getFormattedText().replaceAll("Catuquei", ChatFormatting.BOLD + "Catuquei" + ChatFormatting.RESET));
         }
    }
}
