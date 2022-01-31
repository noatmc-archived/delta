package delta.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class PlayerUtils {
    static Minecraft mc = Minecraft.getMinecraft();

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }

    public static ArrayList<BlockPos> getCityableBlocks(EntityPlayer player) {
        ArrayList<BlockPos> temp =new ArrayList<>();
        ArrayList<BlockPos> temp2 = new ArrayList<>();
        temp.add(new BlockPos(getPlayerPos(player).add(1, 0, 0)));
        temp.add(new BlockPos(getPlayerPos(player).add(0, 0, 1)));
        temp.add(new BlockPos(getPlayerPos(player).add(0, 0, -1)));
        temp.add(new BlockPos(getPlayerPos(player).add(-1, 0, 0)));
        for (BlockPos pos : temp) {
            if (mc.world.isAirBlock(pos.up()) && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN)) {
                temp2.add(pos);
            }
        }
        return temp2;
    }


    public static BlockPos getBlock(Block block, double range) {
        BlockPos pos = null;
        double distance = range;
        for (BlockPos blockPos : BlockUtils.getSphere(PlayerUtils.getPlayerPos(mc.player), (float) range, (int) range,false, true, 0)) {
            double bDistance = mc.player.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            if (bDistance >= distance || mc.world.getBlockState(blockPos).getBlock() != block) continue;
            distance = bDistance;
            pos = blockPos;
        }
        return pos;
    }
}
