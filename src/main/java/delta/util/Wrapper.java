package delta.util;

import net.minecraft.client.Minecraft;

public interface Wrapper {
    Minecraft mc = Minecraft.getMinecraft();
    boolean nullCheck = mc.player == null || mc.world == null;
}
