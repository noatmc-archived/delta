package delta.util.autocrystal;

import net.minecraft.util.math.BlockPos;

public class Position
{
    public Double damage;
    public BlockPos pos;
    public Position(double damage, BlockPos blockPos) {
        this.damage = damage;
        pos = blockPos;
    }
}
