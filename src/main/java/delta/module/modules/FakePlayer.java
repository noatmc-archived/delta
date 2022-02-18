package delta.module.modules;

import com.mojang.authlib.GameProfile;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.UUID;

public class FakePlayer extends Module {
    Setting pop = setting("Move", false);
    public FakePlayer() {
        super("FakePlayer", "pain endurance test", Category.MISC);
    }

    private EntityOtherPlayerMP fakePlayer;

    @Override
    public void onEnable() {
        fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("283dbc01-a1b4-4158-a174-4d5e14c6ace4"), "Catuquei"));
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
        mc.world.addEntityToWorld(-100, fakePlayer);
    }

    @Override
    public void onUpdate() {
        if (fakePlayer != null) {
            Random random = new Random();
            fakePlayer.moveForward = mc.player.moveForward + (random.nextInt(5) / 10F);
            fakePlayer.moveStrafing = mc.player.moveStrafing + (random.nextInt(5) / 10F);
            if (pop.getBVal()) travel(fakePlayer.moveStrafing, fakePlayer.moveVertical, fakePlayer.moveForward);
        }
    }

    public void travel(float strafe, float vertical, float forward) {
        double d0 = fakePlayer.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = (float) EnchantmentHelper.getDepthStriderModifier(fakePlayer);

        if (f3 > 3.0F) {
            f3 = 3.0F;
        }

        if (!fakePlayer.onGround) {
            f3 *= 0.5F;
        }

        if (f3 > 0.0F) {
            f1 += (0.54600006F - f1) * f3 / 3.0F;
            f2 += (fakePlayer.getAIMoveSpeed() - f2) * f3 / 4.0F;
        }

        fakePlayer.moveRelative(strafe, vertical, forward, f2);
        fakePlayer.move(MoverType.SELF, fakePlayer.motionX, fakePlayer.motionY, fakePlayer.motionZ);
        fakePlayer.motionX *= f1;
        fakePlayer.motionY *= 0.800000011920929D;
        fakePlayer.motionZ *= f1;

        if (!fakePlayer.hasNoGravity()) {
            fakePlayer.motionY -= 0.02D;
        }

        if (fakePlayer.collidedHorizontally && fakePlayer.isOffsetPositionInLiquid(fakePlayer.motionX, fakePlayer.motionY + 0.6000000238418579D - fakePlayer.posY + d0, fakePlayer.motionZ)) {
            fakePlayer.motionY = 0.30000001192092896D;
        }
    }

    @Override
    public void onDisable() {
        try {
            mc.world.removeEntity(fakePlayer);
        } catch (Exception ignored) {}
    }
}
