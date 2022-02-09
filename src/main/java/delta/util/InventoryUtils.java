package delta.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;

public class InventoryUtils implements Wrapper {
    public static EnumHand getEnumHand(Item item) {
        if (mc.player.getHeldItemMainhand().item == item) {
            return EnumHand.MAIN_HAND;
        } else if (mc.player.getHeldItemOffhand().item == item) {
            return EnumHand.OFF_HAND;
        }
        return null;
    }

    public static int getHotbarItemSlot(Item item) {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item)
                return i;
        }

        return -1;
    }

    public static void switchToSlot(int slot, boolean silent) {
        if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
        } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    }

    public static void switchToItem(Item item, boolean silent) {
        if (getHotbarItemSlot(item) != -1) {
            switchToSlot(getHotbarItemSlot(item), silent);
        } else {
            MessageUtils.sendMessage(ChatFormatting.RED + "ERROR - Item not found on hotbar.", true);
        }
    }
}
