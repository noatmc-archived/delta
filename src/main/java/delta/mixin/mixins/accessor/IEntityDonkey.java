package delta.mixin.mixins.accessor;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityDonkey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractHorse.class)
public interface IEntityDonkey {
    @Accessor("horseChest")
    void getHorseChest();
}
