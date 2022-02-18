package delta.module.modules;

import delta.event.PacketEvent;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import delta.util.*;
import delta.util.Timer;
import me.bush.eventbus.annotation.EventListener;
import me.bush.eventbus.annotation.ListenerPriority;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * PrestigeCrystal
 * @Author zPrestige_
 * @since 18/02/2022
 */

// Havent tested yet
public class PrestigeCrystal extends Module {
    protected final Setting targetMode = setting("Target Mode", new String[]{
            "Range",
            "Health",
            "Unsafe",
            "Fov"
    });
    protected final Setting targetRange = setting("Target Range", 5.0, 0.1, 6.0, false);
    protected final Setting placeCalculations = setting("Place Calculations", new String[]{
            "Greatest",
            "Simple"
    });
    protected final Setting breakCalculations = setting("Break Calculations", new String[]{
            "Greatest",
            "Simple"
    });
    protected final Setting placeCalculationsType = setting("Place Best Calculations Type", new String[]{
            "TargetDamage",
            "SelfDamage",
            "TargetDamage - SelfDamage",
            "SelfHealth - SelfDamage"
    });
    protected final Setting breakCalculationsType = setting("Break Best Calculations Type", new String[]{
            "TargetDamage",
            "SelfDamage",
            "TargetDamage - SelfDamage",
            "SelfHealth - SelfDamage"
    });
    protected final Setting range = setting("Range", 5.0, 0.1, 6.0, false);
    protected final Setting raytraceRange = setting("Raytrace Range", 5.0, 0.1, 6.0, false);
    protected final Setting placeDelay = setting("Place Delay", 0, 0, 500, true);
    protected final Setting breakDelay = setting("Break Delay", 50, 0, 500, true);
    protected final Setting minimumDamage = setting("Minimum Damage", 6.0, 0.1, 15.0, false);
    protected final Setting maximumSelfDamage = setting("Maximum Self Damage", 8.0, 0.1, 15.0, false);
    protected final Setting dynamicMaximumSelf = setting("Dynamic Maximum Self Damage", false);
    protected final Setting silentSwitch = setting("Silent Switch", false);
    protected final Setting antiWeakness = setting("Anti Weakness", false);
    protected final Setting setDead = setting("Set Dead", false);
    protected final Setting predict = setting("Predict", false);
    protected final Setting placeSwing = setting("Place Swing", new String[]{
            "None",
            "MainHand",
            "Offhand",
            "Packet",
            "Auto"
    });
    protected final Setting breakSwing = setting("Break Swing", new String[]{
            "None",
            "MainHand",
            "Offhand",
            "Packet",
            "Auto"
    });
    protected final Setting render = setting("Render", false);
    protected final Setting red = setting("Red", 255, 0, 255, true);
    protected final Setting green = setting("Green", 255, 0, 255, true);
    protected final Setting blue = setting("Blue", 255, 0, 255, true);
    protected final Setting alpha = setting("Alpha", 255, 0, 255, true);
    protected final Setting lineWidth = setting("Line Width", 1.0, 0.1, 5.0, false);

    protected final ArrayList<Long> crystalsPerSecond = new ArrayList<>();
    protected final Timer placeTimer = new Timer();
    protected final Timer breakTimer = new Timer();
    protected BlockPos placePos = null;
    protected EntityEnderCrystal targetCrystal = null;

    public PrestigeCrystal() {
        super("PrestigeCrystal", "AutoCrystal by zPrestige_", Category.COMBAT);
    }

    protected BlockPos getPlacePosition(EntityPlayer entityPlayer) {
        TreeMap<Double, BlockPos> posses = new TreeMap<>();
        for (BlockPos pos : BlockUtils.getBlocksInRadius(range.getDVal())) {
            if (rayTraceCheckPos(pos) && mc.player.getDistanceSq(pos) / 2 > raytraceRange.getDVal() || !canPlace(pos)) {
                continue;
            }
            double targetDamage = CrystalUtils.calculateDamage(pos, entityPlayer, false);
            double selfDamage = CrystalUtils.calculateDamage(pos, mc.player, false);
            double selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            if (targetDamage >= minimumDamage.getDVal() && (dynamicMaximumSelf.getBVal() ? selfHealth / selfDamage < maximumSelfDamage.getDVal() : selfDamage < maximumSelfDamage.getDVal())) {
                switch (placeCalculations.getMVal()) {
                    case 0:
                        double damage = 0;
                        switch (placeCalculationsType.getMVal()) {
                            case 0:
                                damage = targetDamage;
                                break;
                            case 1:
                                damage = -selfDamage;
                                break;
                            case 2:
                                damage = targetDamage - selfDamage;
                                break;
                            case 3:
                                damage = selfHealth - selfDamage;
                                break;
                        }
                        posses.put(damage, pos);
                        break;
                    case 1:
                        return pos;
                }
            }
        }
        if (!posses.isEmpty()) {
            return posses.lastEntry().getValue();
        }
        return null;
    }

    public EntityEnderCrystal getTargetCrystal(EntityPlayer entityPlayer) {
        TreeMap<Double, EntityEnderCrystal> entities = new TreeMap<>();
        for (Entity entity1 : mc.world.loadedEntityList) {
            if (!(entity1 instanceof EntityEnderCrystal))
                continue;
            EntityEnderCrystal entity = (EntityEnderCrystal) entity1;
            BlockPos pos = entity.getPosition();
            if (!(mc.player.getDistance(entity) <= range.getDVal()) || (rayTraceCheckPos(pos) && mc.player.getDistanceSq(pos) / 2 > raytraceRange.getDVal())) {
                continue;
            }
            double targetDamage = CrystalUtils.calculateDamage(entity, entityPlayer, false);
            double selfDamage = CrystalUtils.calculateDamage(entity, mc.player, false);
            double selfHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            if (targetDamage >= minimumDamage.getDVal() && (dynamicMaximumSelf.getBVal() ? selfHealth / selfDamage < maximumSelfDamage.getDVal() : selfDamage < maximumSelfDamage.getDVal())) {
                switch (breakCalculations.getMVal()) {
                    case 0:
                        double damage = 0;
                        switch (breakCalculationsType.getMVal()) {
                            case 0:
                                damage = targetDamage;
                                break;
                            case 1:
                                damage = -selfDamage;
                                break;
                            case 2:
                                damage = targetDamage - selfDamage;
                                break;
                            case 3:
                                damage = selfHealth - selfDamage;
                                break;
                        }
                        entities.put(damage, entity);
                        break;
                    case 1:
                        return entity;
                }
            }
        }
        if (!entities.isEmpty()) {
            return entities.lastEntry().getValue();
        }
        return null;
    }

    @Override
    public void onTick() {
        setup();
        if (placePos != null && placeTimer.hasReached((long) placeDelay.getDVal())) {
            performPlace(placePos);
            breakTimer.reset();
        }
        if (targetCrystal != null && breakTimer.hasReached((long) breakDelay.getDVal())) {
            performBreak(targetCrystal);
            breakTimer.reset();
        }
    }

    public void performPlace(BlockPos pos) {
        boolean needsSilentSwitch = silentSwitch.getBVal() && !mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) && !mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL);
        int slot = InventoryUtils.getHotbarItemSlot(Items.END_CRYSTAL);
        int currentItem = mc.player.inventory.currentItem;
        if (needsSilentSwitch && slot != -1) {
            InventoryUtils.switchToSlot(slot, true);
        }
        EnumFacing facing = pos.getY() > 254 ? EnumFacing.NORTH : getClosestEnumFacing(pos);
        EnumHand enumHand = mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : null;
        if (enumHand != null && mc.getConnection() != null) {
            mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketPlayerTryUseItemOnBlock(pos, facing != null ? facing : EnumFacing.UP, enumHand, 0.5f, 0.5f, 0.5f));
        }
        if (placeSwing.getMVal() != 0) {
            swingArm(true);
        }
        if (needsSilentSwitch && slot != -1) {
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
        }
    }

    protected void performBreak(EntityEnderCrystal entityEnderCrystal) {
        boolean switched = false;
        int currentItem = -1;
        PotionEffect weakness = mc.player.getActivePotionEffect(MobEffects.WEAKNESS);
        if (antiWeakness.getBVal() && weakness != null && !mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) {
            int swordSlot = InventoryUtils.getHotbarItemSlot(Items.DIAMOND_SWORD);
            currentItem = mc.player.inventory.currentItem;
            InventoryUtils.switchToSlot(swordSlot, true);
            switched = true;
        }
        if (mc.getConnection() != null) {
            mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketUseEntity(entityEnderCrystal));
        }
        if (breakSwing.getMVal() != 0) {
            swingArm(false);
        }
        if (setDead.getBVal()) {
            entityEnderCrystal.setDead();
        }
        if (switched) {
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
        }
        crystalsPerSecond.add(System.currentTimeMillis() + 1000L);
    }

    protected void setup() {
        EntityPlayer entityPlayer = getClosestTarget(targetMode.getMVal(), targetRange.getDVal());
        if (entityPlayer != null) {
            placePos = getPlacePosition(entityPlayer);
            targetCrystal = getTargetCrystal(entityPlayer);
        }
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void onPacketSend(PacketEvent.Send event) {
        if (!predict.getBVal() || !(event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock))
            return;
        EntityEnderCrystal entityEnderCrystal = null;
        for (BlockPos pos : BlockUtils.getBlocksInRadius(range.getDVal())) {
            if (!mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(pos.up().up()).getBlock().equals(Blocks.AIR))
                continue;
            try {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(pos.up()) < 1.0f) {
                        entityEnderCrystal = (EntityEnderCrystal) entity;
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            }
        }
        if (entityEnderCrystal == null) {
            return;
        }
        boolean switched = false;
        int currentItem = -1;
        PotionEffect weakness = mc.player.getActivePotionEffect(MobEffects.WEAKNESS);
        if (antiWeakness.getBVal() && weakness != null && !mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_SWORD)) {
            int swordSlot = InventoryUtils.getHotbarItemSlot(Items.DIAMOND_SWORD);
            currentItem = mc.player.inventory.currentItem;
            InventoryUtils.switchToSlot(swordSlot, true);
            switched = true;
        }
        if (mc.getConnection() != null) {
            mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketUseEntity(entityEnderCrystal));
        }
        if (breakSwing.getMVal() != 0) {
            swingArm(false);
        }
        if (setDead.getBVal()) {
            entityEnderCrystal.setDead();
        }
        if (switched) {
            mc.player.inventory.currentItem = currentItem;
            mc.playerController.updateController();
        }
    }

    protected EntityPlayer getClosestTarget(int targetPriority, double range) {
        TreeMap<Float, EntityPlayer> entityPlayerFloatTreeMap = new TreeMap<>();
        TreeMap<Double, EntityPlayer> entityPlayerFloatTreeMap2 = new TreeMap<>();
        TreeMap<Integer, Boolean> entityPlayerFloatTreeMap3 = new TreeMap<>();
        TreeMap<Integer, Boolean> entityPlayerFloatTreeMap4 = new TreeMap<>();
        mc.world.playerEntities.stream().filter(entityPlayer -> !entityPlayer.equals(mc.player)).forEach(entityPlayer -> {
            float distance = entityPlayer.getDistance(mc.player);
            if (distance < range) {
                entityPlayerFloatTreeMap.put(distance, entityPlayer);
                entityPlayerFloatTreeMap2.put((double) (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()), entityPlayer);
                entityPlayerFloatTreeMap3.put(entityPlayer.entityId, BlockUtils.isPlayerSafe(entityPlayer));
                ICamera camera = new Frustum();
                camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
                entityPlayerFloatTreeMap4.put(entityPlayer.entityId, camera.isBoundingBoxInFrustum(entityPlayer.getEntityBoundingBox()));
            }
        });
        switch (targetPriority) {
            case 0:
                if (!entityPlayerFloatTreeMap.isEmpty()) {
                    return entityPlayerFloatTreeMap.firstEntry().getValue();
                }
                break;
            case 1:
                if (!entityPlayerFloatTreeMap2.isEmpty()) {
                    return entityPlayerFloatTreeMap2.firstEntry().getValue();
                }
                break;
            case 2:
                TreeMap<Float, EntityPlayer> entityPlayerTreeMap = new TreeMap<>();
                for (Map.Entry<Integer, Boolean> entry : entityPlayerFloatTreeMap3.entrySet()) {
                    EntityPlayer entityPlayer = (EntityPlayer) mc.world.getEntityByID(entry.getKey());
                    if (entityPlayer != null) {
                        entityPlayerTreeMap.put(mc.player.getDistance(entityPlayer), entityPlayer);
                    }
                }
                if (!entityPlayerTreeMap.isEmpty()) {
                    return entityPlayerTreeMap.firstEntry().getValue();
                } else if (!entityPlayerFloatTreeMap.isEmpty()) {
                    return entityPlayerFloatTreeMap.firstEntry().getValue();
                }
                break;
            case 3:
                TreeMap<Float, EntityPlayer> entityPlayerTreeMap2 = new TreeMap<>();
                for (Map.Entry<Integer, Boolean> entry : entityPlayerFloatTreeMap4.entrySet()) {
                    EntityPlayer entityPlayer = (EntityPlayer) mc.world.getEntityByID(entry.getKey());
                    if (entityPlayer != null) {
                        entityPlayerTreeMap2.put(mc.player.getDistance(entityPlayer), entityPlayer);
                    }
                }

                if (!entityPlayerTreeMap2.isEmpty()) {
                    return entityPlayerTreeMap2.firstEntry().getValue();
                } else if (!entityPlayerFloatTreeMap.isEmpty()) {
                    return entityPlayerFloatTreeMap.firstEntry().getValue();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + targetPriority);
        }
        return null;
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        if (render.getBVal() && placePos != null){
            Long currentTime = System.currentTimeMillis();
            new ArrayList<>(crystalsPerSecond).stream().filter(currentTimeMillis -> currentTimeMillis < currentTime).forEach(crystalsPerSecond::remove);
            Color color = new Color((int) red.getDVal() / 255.0f, (int)green.getDVal() / 255.0f, (int) blue.getDVal() / 255.0f, (int) alpha.getDVal() / 255.0f);
            RenderUtils.drawBox(placePos, color, false);
            RenderUtils.drawBlockOutline(placePos, color, (float) lineWidth.getDVal(), false);
            RenderUtils.drawText(placePos, crystalsPerSecond.size() + "", new Color(1, 1, 1, 1), true);
        }
    }

    protected boolean rayTraceCheckPos(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) != null;
    }

    protected boolean canPlace(BlockPos pos) {
        ArrayList<Entity> intersecting = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.up()).shrink(0.2)).stream().filter(entity -> entity instanceof EntityPlayer).collect(Collectors.toCollection(ArrayList::new));
        return intersecting.isEmpty() && (mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK) || mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)) && BlockUtils.isAir(pos.up()) && BlockUtils.isAir(pos.up().up());
    }

    protected EnumFacing getClosestEnumFacing(BlockPos pos) {
        TreeMap<Double, EnumFacing> facingTreeMap = new TreeMap<>();
        if (BlockUtils.isAir(pos.up())) {
            facingTreeMap.put(mc.player.getDistanceSq(pos.up()), EnumFacing.UP);
        }
        if (BlockUtils.isAir(pos.down())) {
            facingTreeMap.put(mc.player.getDistanceSq(pos.down()), EnumFacing.DOWN);
        }
        if (BlockUtils.isAir(pos.north())) {
            facingTreeMap.put(mc.player.getDistanceSq(pos.north()), EnumFacing.NORTH);
        }
        if (BlockUtils.isAir(pos.east())) {
            facingTreeMap.put(mc.player.getDistanceSq(pos.east()), EnumFacing.EAST);
        }
        if (BlockUtils.isAir(pos.south())) {
            facingTreeMap.put(mc.player.getDistanceSq(pos.south()), EnumFacing.SOUTH);
        }
        if (BlockUtils.isAir(pos.west())) {
            facingTreeMap.put(mc.player.getDistanceSq(pos.west()), EnumFacing.WEST);
        }
        if (!facingTreeMap.isEmpty())
            return facingTreeMap.firstEntry().getValue();
        return null;
    }


    protected void swingArm(boolean place) {
        EnumHand autoHand = mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        switch (place ? placeSwing.getMVal() : breakSwing.getMVal()) {
            case 1:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            case 2:
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case 3:
                if (mc.getConnection() != null) {
                    mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketAnimation(autoHand));
                }
                break;
            case 4:
                mc.player.swingArm(autoHand);
                break;
        }
    }
}
