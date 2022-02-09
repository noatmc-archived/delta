package delta.module.modules;

import delta.DeltaCore;
import delta.module.Category;
import delta.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

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

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event) {
        int y = 520 - ((mc.fontRenderer.FONT_HEIGHT + 4) * getSorted().size());
        for (Module module : getSorted()) {
            mc.fontRenderer.drawString(module.getFullString(), 910 - mc.fontRenderer.getStringWidth(module.getFullString()), y, new Color(255, 255, 255).hashCode());
            y = y + mc.fontRenderer.FONT_HEIGHT + 4;
        }
//                GlStateManager.scale(2, 2, 2);
//        mc.fontRenderer.drawString("Delta", 10, 10, new Color(255,255,255).hashCode());
//        GlStateManager.scale(2.5, 2.5, 2.5);
//        mc.fontRenderer.drawString("Delta", 7, 7, new Color(0,0,0).hashCode());
//        GlStateManager.scale(0.5, 0.5, 1);
        mc.fontRenderer.drawString("[ Delta - welcome to the botnet " + mc.session.getUsername() + " ]", 10, 10, new Color(255, 255, 255).hashCode());
    }

    public ArrayList<Module> getSorted() {
        return (ArrayList<Module>) DeltaCore.moduleManager.getEnabledModules().stream().sorted(Comparator.comparing(m -> mc.fontRenderer.getStringWidth(m.getFullString()))).collect(Collectors.toList());
    }
}
