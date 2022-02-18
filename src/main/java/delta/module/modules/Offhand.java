package delta.module.modules;

import delta.event.TotemPopEvent;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import delta.util.BlockUtils;
import delta.util.InventoryUtils;
import delta.util.Timer;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

public class Offhand extends Module {
    protected final Setting item = setting("Item", new String[]{
            "Crystal",
            "Totem"
    });
    protected final Setting crystalOnSword = setting("Sword Crystal", false);
    protected final Setting crystalOnPickaxe = setting("Pickaxe Crystal", false);
    protected final Setting totemHealth = setting("Totem Health", 10.0, 0.0, 20.0, false);
    protected final Setting holeHealth = setting("Totem Hole Health", 10.0, 0.0, 20.0, false);
    protected final Setting switchDelay = setting("Switch Delay", 50, 0, 200, true);
    protected final Setting postPopForceTotem = setting("Post Pop Force Totem", false);
    protected final Setting forceTime = setting("Force Time", 1000, 0, 3000, true);
    protected final Setting fallDistance = setting("Fall Distance Check", false);
    protected final Setting minDistance = setting("Min Distance", 10.0, 1.0, 100.0, false);
    protected final Setting fallBack = setting("FallBack", false);
    protected final Setting gapple = setting("Gapple Switch", false);
    protected final Setting rightClick = setting("Right Click Only", false);

    protected final Timer switchTimer = new Timer(), postPopTimer = new Timer();
    protected int offhandSlot = -1;

    public Offhand() {
        super("Offhand", "Swaps items to your offhand automatically", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen == null) {
            offhandSlot = InventoryUtils.getItemSlot(getOffhandItem());
            execute();
        }
    }

    public void execute() {
        if (mc.player.getHeldItemOffhand().getItem() != getOffhandItem() && offhandSlot != -1 && switchTimer.hasReached((long) switchDelay.getDVal())) {
            int slot = offhandSlot < 9 ? offhandSlot + 36 : offhandSlot;
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
            switchTimer.reset();
        }
    }

    public Item getOffhandItem() {
        boolean safeToSwap = safeToSwap();
        if (postPopForceTotem.getBVal() && !postPopTimer.hasReached((long) forceTime.getDVal()))
            return Items.TOTEM_OF_UNDYING;
        switch (item.getMVal()) {
            case 0:
                if (safeToSwap) {
                    if (gapple.getBVal() && ((rightClick.getBVal() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD && mc.gameSettings.keyBindUseItem.isKeyDown()) || (!rightClick.getBVal() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD)))
                        return Items.GOLDEN_APPLE;

                    if (crystalOnSword.getBVal() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD)
                        return Items.END_CRYSTAL;

                    if (crystalOnPickaxe.getBVal() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE)
                        return Items.END_CRYSTAL;

                    if (fallBack.getBVal() && InventoryUtils.getStackCount(Items.TOTEM_OF_UNDYING) == 0)
                        return Items.END_CRYSTAL;
                }
                return Items.TOTEM_OF_UNDYING;
            case 1:
                if (fallBack.getBVal() && InventoryUtils.getStackCount(Items.END_CRYSTAL) == 0)
                    return Items.TOTEM_OF_UNDYING;

                if (fallDistance.getBVal() && mc.player.fallDistance > minDistance.getDVal())
                    return Items.TOTEM_OF_UNDYING;
                if (safeToSwap) {
                    if (gapple.getBVal() && ((rightClick.getBVal() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD && mc.gameSettings.keyBindUseItem.isKeyDown()) || (!rightClick.getBVal() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD)))
                        return Items.GOLDEN_APPLE;

                    return Items.END_CRYSTAL;
                }
                return Items.TOTEM_OF_UNDYING;
        }
        return null;
    }

    public boolean safeToSwap() {
        if (BlockUtils.isPlayerSafe(mc.player) && mc.player.onGround && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) < holeHealth.getDVal())
            return false;
        return !((mc.player.getHealth() + mc.player.getAbsorptionAmount()) < totemHealth.getDVal());
    }

    @EventListener
    public void onTotemPop(TotemPopEvent event) {
        if (fullNullCheck() || !postPopForceTotem.getBVal() || !event.entity.equals(mc.player))
            return;
        postPopTimer.reset();
    }
}