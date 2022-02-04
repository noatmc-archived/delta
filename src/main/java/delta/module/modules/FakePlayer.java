package delta.module.modules;

import com.mojang.authlib.GameProfile;
import delta.event.PacketEvent;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import delta.util.CrystalUtils;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.UUID;

public class FakePlayer extends Module {
    Setting pop = setting("Pop", false);
    public FakePlayer() {
        super("FakePlayer", "pain endurance test", Category.MISC);
    }

    private EntityOtherPlayerMP fakePlayer;

    @Override
    public void onEnable() {

        fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("a07208c2-01e5-4eac-a3cf-a5f5ef2a4700"), "travis"));
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
        mc.world.addEntityToWorld(-100, fakePlayer);

    }

    @EventListener
    public void onPacketReceive(PacketEvent.Receive event) {
        double damage;
        SPacketExplosion explosion;
        if (this.fakePlayer == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketExplosion && this.fakePlayer.getDistance((explosion = (SPacketExplosion) event.getPacket()).getX(), explosion.getY(), explosion.getZ()) <= 15.0 && (damage = CrystalUtils.calculateDamagePhobos(    explosion.getX(), explosion.getY(), explosion.getZ(), this.fakePlayer)) > 0.0 && this.pop.getBVal()) {
            DamageSource source = DamageSource.causeExplosionDamage(new Explosion(mc.world, mc.player, ((SPacketExplosion) event.getPacket()).getX(), ((SPacketExplosion) event.getPacket()).getY(), ((SPacketExplosion) event.getPacket()).getZ(), ((SPacketExplosion) event.getPacket()).getStrength(), false, true));
            fakePlayer.attackEntityFrom(source, (float)damage);
        }
    }

    @Override
    public void onDisable() {
        try {
            mc.world.removeEntity(fakePlayer);
        } catch (Exception ignored) {}
    }
}
