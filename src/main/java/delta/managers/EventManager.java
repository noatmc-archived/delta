package delta.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import delta.util.MessageUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventManager {
    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.getMessage().getFormattedText().contains("Catuquei")) {
            event.setCanceled(true);
            MessageUtils.sendMessage(event.getMessage().getFormattedText().replaceAll("Catuquei", ChatFormatting.BOLD + "Catuquei" + ChatFormatting.RESET));
         }
    }
}
