package delta.util.rotation;

import delta.util.Wrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil implements Wrapper {

    public static float[] getRotations(Vec3d vec) {
        return getRotations(vec.x, vec.y, vec.z);
    }

    public static float[] getRotations(double x, double y, double z) {
        double xSize = x - mc.player.posX;
        double ySize = y - (mc.player.posY + mc.player.getEyeHeight());
        double zSize = z - mc.player.posZ;
        double theta = MathHelper.sqrt(xSize * xSize + zSize * zSize);
        float yaw = (float) (Math.atan2(zSize, xSize) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(ySize, theta) * 180.0D / Math.PI));
        return new float[]{
                (mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw)) % 360F,
                (mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)) % 360F,
        };
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double) RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - RotationUtil.mc.player.rotationYaw), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - RotationUtil.mc.player.rotationPitch)};
    }
}
