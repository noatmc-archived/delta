package delta.module.modules;

import delta.module.Category;
import delta.module.Module;
import net.minecraftforge.common.MinecraftForge;

public class HUD extends Module {
    public HUD() {
        super("HUD", "Heads up display", Category.HUD);
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
