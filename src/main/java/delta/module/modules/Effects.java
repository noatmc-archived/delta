package delta.module.modules;

import delta.event.PlayerDeathEvent;
import delta.event.TotemPopEvent;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.entity.effect.EntityLightningBolt;

public class Effects extends Module {
    Setting totemPop = setting("Totem Pop", true);
    Setting death = setting("Death", true);
    public Effects() {
        super("Effects", "idk", Category.RENDER);
    }

    @EventListener
    public void onTotemPop(TotemPopEvent event) {
        if (event.getEntity() != mc.player && totemPop.getBVal()) {
            mc.world.spawnEntity(new EntityLightningBolt(mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
        }
    }

    @EventListener
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() != mc.player && death.getBVal()) {
            mc.world.spawnEntity(new EntityLightningBolt(mc.world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, true));
        }
     }
}
