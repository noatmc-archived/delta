package delta.mixin.mixins.accessor;

import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiShulkerBox.class)
public interface IGuiShulkerBox {
    @Accessor("inventory")
    IInventory getInventory();
}
