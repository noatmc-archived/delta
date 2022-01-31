package delta.module.modules;

import com.mojang.authlib.GameProfile;
import delta.module.Category;
import delta.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", "pain endurance test", Category.MISC);
    }

    private EntityOtherPlayerMP fake_player;

    @Override
    public void onEnable() {

        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("a07208c2-01e5-4eac-a3cf-a5f5ef2a4700"), "travis"));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, fake_player);

    }

    @Override
    public void onDisable() {
        try {
            mc.world.removeEntity(fake_player);
        } catch (Exception ignored) {}
    }
}
