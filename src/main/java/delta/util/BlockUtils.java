package delta.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static delta.util.Wrapper.mc;

public class BlockUtils {


    public static boolean isPlayerSafe(EntityPlayer entityPlayer) {
        BlockPos pos = getFlooredPlayerPos(entityPlayer);
        if (isNotIntersecting(entityPlayer)) {
            return isBedrockOrObsidianOrEchest(pos.north()) && isBedrockOrObsidianOrEchest(pos.east()) && isBedrockOrObsidianOrEchest(pos.south()) && isBedrockOrObsidianOrEchest(pos.west()) && isBedrockOrObsidianOrEchest(pos.down());
        } else {
            return isIntersectingSafe(entityPlayer);
        }
    }

    public static boolean isNotIntersecting(EntityPlayer entityPlayer) {
        BlockPos pos = getFlooredPlayerPos(entityPlayer);
        AxisAlignedBB bb = entityPlayer.getEntityBoundingBox();
        return (!isAir(pos.north()) || !bb.intersects(new AxisAlignedBB(pos.north()))) && (!isAir(pos.east()) || !bb.intersects(new AxisAlignedBB(pos.east()))) && (!isAir(pos.south()) || !bb.intersects(new AxisAlignedBB(pos.south()))) && (!isAir(pos.west()) || !bb.intersects(new AxisAlignedBB(pos.west())));
    }

    public static boolean isIntersectingSafe(EntityPlayer entityPlayer) {
        BlockPos pos = getFlooredPlayerPos(entityPlayer);
        AxisAlignedBB bb = entityPlayer.getEntityBoundingBox();
        if (isAir(pos.north()) && bb.intersects(new AxisAlignedBB(pos.north()))) {
            BlockPos pos1 = pos.north();
            if (!isBedrockOrObsidianOrEchest(pos1.north()) || !isBedrockOrObsidianOrEchest(pos1.east()) || !isBedrockOrObsidianOrEchest(pos1.west()) || !isBedrockOrObsidianOrEchest(pos1.down()))
                return false;
        }
        if (isAir(pos.east()) && bb.intersects(new AxisAlignedBB(pos.east()))) {
            BlockPos pos1 = pos.east();
            if (!isBedrockOrObsidianOrEchest(pos1.north()) || !isBedrockOrObsidianOrEchest(pos1.east()) || !isBedrockOrObsidianOrEchest(pos1.south()) || !isBedrockOrObsidianOrEchest(pos1.down()))
                return false;
        }
        if (isAir(pos.south()) && bb.intersects(new AxisAlignedBB(pos.south()))) {
            BlockPos pos1 = pos.south();
            if (!isBedrockOrObsidianOrEchest(pos1.east()) || !isBedrockOrObsidianOrEchest(pos1.south()) || !isBedrockOrObsidianOrEchest(pos1.west()) || !isBedrockOrObsidianOrEchest(pos1.down()))
                return false;
        }
        if (isAir(pos.west()) && bb.intersects(new AxisAlignedBB(pos.west()))) {
            BlockPos pos1 = pos.west();
            return isBedrockOrObsidianOrEchest(pos1.north()) && isBedrockOrObsidianOrEchest(pos1.south()) && isBedrockOrObsidianOrEchest(pos1.west()) && isBedrockOrObsidianOrEchest(pos1.down());
        }
        return true;
    }

    public static boolean isBedrockOrObsidianOrEchest(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(pos).getBlock().equals(Blocks.ENDER_CHEST);
    }

    public static boolean isAir(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR);
    }

    public static BlockPos getFlooredPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static List<BlockPos> getBlocksInRadius(double range) {
        List<BlockPos> posses = new ArrayList<>();
        float xRange = Math.round(range);
        float yRange = Math.round(range);
        float zRange = Math.round(range);
        for (float x = -xRange; x <= xRange; x++) {
            for (float y = -yRange; y <= yRange; y++) {
                for (float z = -zRange; z <= zRange; z++) {
                    BlockPos position = mc.player.getPosition().add(x, y, z);
                    if (mc.player.getDistance(position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5) <= range) {
                        posses.add(position);
                    }
                }
            }
        }
        return posses;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static BlockPos getFlooredPos(BlockPos pos) {
        return new BlockPos(Math.floor(pos.getX()), Math.floor(pos.getY()), Math.floor(pos.getZ()));
    }

    public static boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<>();
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            IBlockState blockState;
            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false) || (blockState = mc.world.getBlockState(neighbour)).getMaterial().isReplaceable())
                continue;
            facings.add(side);
        }
        return facings;
    }

    public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
        return new Vec3d[]{new Vec3d(vec3d.x, vec3d.y - 1.0, vec3d.z), new Vec3d(vec3d.x != 0.0 ? vec3d.x * 2.0 : vec3d.x, vec3d.y, vec3d.x != 0.0 ? vec3d.z : vec3d.z * 2.0), new Vec3d(vec3d.x == 0.0 ? vec3d.x + 1.0 : vec3d.x, vec3d.y, vec3d.x == 0.0 ? vec3d.z : vec3d.z + 1.0), new Vec3d(vec3d.x == 0.0 ? vec3d.x - 1.0 : vec3d.x, vec3d.y, vec3d.x == 0.0 ? vec3d.z : vec3d.z - 1.0), new Vec3d(vec3d.x, vec3d.y + 1.0, vec3d.z)};
    }
}
