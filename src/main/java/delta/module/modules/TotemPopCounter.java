package delta.module.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import delta.DeltaCore;
import delta.event.PlayerDeathEvent;
import delta.event.TotemPopEvent;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import delta.util.MessageUtils;
import me.bush.eventbus.annotation.EventListener;

public class TotemPopCounter extends Module {
    Setting countSelf = setting("Count Self", false);
    public TotemPopCounter() {
        super("Totem Pop Counter", "idk man it counts pop", Category.COMBAT);
    }

//    @EventListener
//    public void onTotemPop(PacketEvent.Receive event) {
////        if (nullCheck) return;
//        if (event.getPacket() instanceof SPacketEntityStatus) {
//            if (((SPacketEntityStatus) event.getPacket()).getOpCode() == 35) {
//                if (DeltaCore.totemPopManager.getTotemMap().containsKey(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world))) {
//                    int times = DeltaCore.totemPopManager.getTotemMap().get(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world)) + 1;
//                    DeltaCore.EVENT_BUS.post(new TotemPopEvent(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world), times));
//                    DeltaCore.totemPopManager.getTotemMap().remove(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world));
//                    DeltaCore.totemPopManager.getTotemMap().put(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world), times);
//                } else {
//                    DeltaCore.EVENT_BUS.post(new TotemPopEvent(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world),  1));
//                    DeltaCore.totemPopManager.getTotemMap().put(((SPacketEntityStatus)event.getPacket()).getEntity(mc.world), 1);
//                }
//            }
//        }
//    }

    @EventListener
    public void onTotemPopped(TotemPopEvent event) {
        if (event.getEntity() == mc.player && countSelf.getBVal()) MessageUtils.sendMessage( "bro u just popped " + ChatFormatting.RED + event.getCount() + ChatFormatting.RESET +   " totems...");
        else if (event.getEntity() != mc.player) MessageUtils.sendMessage( ChatFormatting.BOLD +  "THIS BOY " + event.getEntity().getName() + " JUST POPPED " + ChatFormatting.GREEN + event.getCount() + ChatFormatting.RESET +  " TOTEMS LMAO");
    }

    @EventListener
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (DeltaCore.totemPopManager.getTotemMap().containsKey(event.getEntity())) {
            if (event.getEntity() != mc.player) MessageUtils.sendRainbowMessage(ChatFormatting.BOLD + "THIS BOZO " + event.getEntity().getName() + " JUST DIED LOLOLOLOLOLOL");
            DeltaCore.totemPopManager.getTotemMap().remove(event.getEntity());
        }
    }
}
