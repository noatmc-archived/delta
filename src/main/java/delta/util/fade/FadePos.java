package delta.util.fade;

import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class FadePos {
    public Color color;
    public BlockPos fadePos;
    public int alpha;
    public FadePos(BlockPos pos, Color color) {
        fadePos = pos;
        this.color = color;
        alpha = 190;
    }
    public void reduce() {
        alpha = alpha - 2;
    }
}
