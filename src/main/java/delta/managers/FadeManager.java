package delta.managers;

import delta.util.RenderUtils;
import delta.util.Wrapper;
import delta.util.fade.FadePos;
import delta.util.fade.Utils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;


/*
    concept of this is from wallhacks's spark client
    the code isn't skidded from it.
 */
public class FadeManager {
    public FadeManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    ArrayList<FadePos> fadePos = new ArrayList<>();

    public void addFadePos(FadePos pos) {
        fadePos.add(pos);
    }

    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
//        if (Wrapper.nullCheck) return;
        fadePos = Utils.getRemoved(fadePos);
        fadePos.forEach(FadePos::reduce);
        fadePos.removeIf(pos -> pos.alpha == 0);
        for (FadePos pos : fadePos) {
            RenderUtils.drawBoxESP(pos.fadePos, new Color(pos.color.getRed(), pos.color.getGreen(), pos.color.getBlue(), pos.alpha), 1.0f, true, true, pos.alpha, 0.0);
        }
    }
}
