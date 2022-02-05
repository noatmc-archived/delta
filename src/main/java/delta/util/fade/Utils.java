package delta.util.fade;

import delta.util.BlockUtils;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<FadePos> getRemoved(ArrayList<FadePos> pos ) {
        ArrayList<FadePos> temp = new ArrayList<>();
        ArrayList<BlockPos> temp2 = new ArrayList<>();
        for (FadePos fadePos : pos) {
            if (!temp2.contains(BlockUtils.getFlooredPos(fadePos.fadePos))) {
                temp.add(new FadePos(BlockUtils.getFlooredPos(fadePos.fadePos), fadePos.color));
                temp2.add(BlockUtils.getFlooredPos(fadePos.fadePos));
            }
        }
        return temp;
    }
}
