package delta.module.modules;

import delta.DeltaCore;
import delta.event.PacketEvent;
import delta.event.TickEvent;
import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;
import delta.util.*;
import delta.util.autocrystal.Position;
import delta.util.autocrystal.Sorter;
import delta.util.fade.FadePos;
import delta.util.packets.PacketType;
import delta.util.rotation.RotationUtil;
import me.bush.eventbus.annotation.EventListener;
import me.bush.eventbus.annotation.ListenerPriority;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AutoCrystal extends Module {

    Setting placeCrystal = setting("Place Crystal", true);
    Setting breakCrystal = setting("Break Crystal", true);
    Setting placeRange = setting("Place Range", 6.0, 0.0, 6.0, false);
    Setting breakRange = setting("Break Range", 6.0, 0.0, 6.0, false);
    Setting packetType = setting("Packet Type", new String[]{
            "Instant",
            "Normal",
            "Player"
    });
    Setting replace = setting("Replace", false);
    Setting silent = setting("Silent", true);
    Setting sync = setting("Sync Attack", true);
    Setting swing = setting("Swing", true);
    Setting rotate = setting("Rotation", false);
    Setting wallRange = setting("Wall Range", 4.0, 0.1 ,6.0, false);
    Setting antiSuicide = setting("Anti Suicide", false);
    Setting maxSelfDmg = setting("Self Damage", 36.0f, 0.0f, 1000, false);
    Setting ignoreSelfDmg = setting("Ignore Self Damage", true);
    Setting placeSpeed = setting("Place Speed", 20.0, 0.1, 20.0, false);
    Setting minDamage = setting("Min Damage", 6.0f, 0.0f, 36.0f, false);
    Setting fade = setting("Fade", true);
    Setting r = setting("Red", 255, 0, 255, true);
    Setting g = setting("Green", 255, 0, 255, true);
    Setting b = setting("Blue", 255, 0, 255, true);
    Setting noDelay = setting("No Delay", true);

    ArrayList<EntityEnderCrystal> attackedCrystal = new ArrayList<>();
    Position position = null;
    EntityPlayer target = null;
    int speed = 0;
    Timer timer = new Timer();
    Timer placeTimer = new Timer();

    public AutoCrystal() {
        super("AutoCrystal", "2b2t australia bombing campaign", Category.COMBAT);
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
        return !shouldCheck || mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), (float) pos.getY() + height, pos.getZ()), false, true, false) == null;
    }

    public static AutoCrystal getInstance() {
        return new AutoCrystal();
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), 255 / 255.0f, 198 / 255.0f);
    }

    @EventListener
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            if (((SPacketSoundEffect) event.getPacket()).getCategory() == SoundCategory.BLOCKS && ((SPacketSoundEffect) event.getPacket()).getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(((SPacketSoundEffect) event.getPacket()).getX(), ((SPacketSoundEffect) event.getPacket()).getY(), ((SPacketSoundEffect) event.getPacket()).getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onThread() {

//        System.out.println(speed);
        if (timer.hasReached(500)) {
            attackedCrystal.clear();
            timer.reset();
        }
    }

    @Override
    public void onEnable() {
        position = null;
        attackedCrystal.clear();
        timer.reset();
        placeTimer.reset();
        speed = 0;
        target = null;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void onEntitySpawn(Entity crystal) {
        if (this.position != null) {
            if (crystal instanceof EntityEnderCrystal && mc.player.getDistance(crystal) <= breakRange.getDVal()) {
                if (breakCrystal.getBVal()) {
                    if (rotate.getBVal()) {
                        DeltaCore.rotationManager.setRotate(true);
                        DeltaCore.rotationManager.setYaw(RotationUtil.getRotations(crystal.getPositionVector())[0]);
                        DeltaCore.rotationManager.setPitch(RotationUtil.getRotations(crystal.getPositionVector())[1]);
                    }
                    mc.playerController.attackEntity(mc.player, crystal);
                    if (sync.getBVal()) crystal.setDead();
                    if (swing.getBVal()) mc.player.swingArm(EnumHand.OFF_HAND);
                    attackedCrystal.add((EntityEnderCrystal) crystal);
                    DeltaCore.rotationManager.setRotate(false);
                }
            }
        }
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void onPlayerUpdate(TickEvent.Pre event) {
        if (fullNullCheck()) return;
        if (!event.isCancelled()) {
            if (noDelay.getBVal()) {
                if (!rotate.getBVal() && placeCrystal.getBVal()) {
                    placeCrystal();
                }
            } else {
                if (!rotate.getBVal() && placeCrystal.getBVal() && placeTimer.hasReached((long) (500 / placeSpeed.getDVal()))) {
                    placeCrystal();
                    placeTimer.reset();
                }
            }
        }
    }

    @Override
    public String getHudString() {
        return "" + attackedCrystal.size();
    }

    @Override
    public void onMotion() {
        if (fullNullCheck()) return;
        if (noDelay.getBVal()) {
            if (rotate.getBVal() && placeCrystal.getBVal()) {
                placeCrystal();
            }
        } else {
            if (rotate.getBVal() && placeCrystal.getBVal() && placeTimer.hasReached((long) (500 / placeSpeed.getDVal()))) {
                placeCrystal();
                placeTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        if (this.position != null && !fade.getBVal()) {
            RenderUtils.drawBoxESP(position.pos, new Color(rainbow(50).getRed(), rainbow(50).getGreen(), rainbow(50).getBlue(), (int) 190), 1.0f, true, true, (int) 190, 0.0);
        }
    }

    @Override
    public void onEntityRemoved(Entity removed) {
        if (removed instanceof EntityEnderCrystal) {
            if (attackedCrystal.contains(removed)) {
                if (replace.getBVal()) {
                    try {
                        int slot = mc.player.inventory.currentItem;
                        if (silent.getBVal()) InventoryUtils.switchToItem(Items.END_CRYSTAL, true);
                        CrystalUtils.placeCrystalOnBlock(PacketType.valueOf(packetType.getMode()), removed.getPosition().down(1), silent.getBVal());
                        if (silent.getBVal()) InventoryUtils.switchToSlot(slot, false);
                    } catch (Exception exc) {
                        // exc
                    }
                }
            }
        }
    }


    public Position getCrystalPlacePos() {
        ArrayList<Position> position = new ArrayList<>();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player) continue;
            if (player.getDistance(mc.player) >= 11) continue; // stops lag
            target = player;
            for (BlockPos pos : CrystalUtils.possiblePlacePosPhobos((float) placeRange.getDVal(), true, false)) {
                double targetDamage = CrystalUtils.calculateDamagePhobos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, player);
                double selfDamage = CrystalUtils.calculateDamagePhobos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player);
//                if (CrystalUtils.isCrystalStuck(pos) != null) {
//                    mc.playerController.attackEntity(mc.player, Objects.requireNonNull(CrystalUtils.isCrystalStuck(pos)));
//                }
//                if (antiSuicide.getBVal()) {
//                    if (selfDamage < getSuicideHealth()) continue;
//                }
                if (targetDamage > minDamage.getDVal() && selfDamage < getSuicideHealth()) {
                    position.add(new Position(targetDamage, pos));
                }
            }
        }
        if (position.isEmpty()) return null;
        position.sort(new Sorter());
        return position.get(0);
    }

    double getSuicideHealth() {
        if (antiSuicide.getBVal()) {
            return mc.player.getHealth() + mc.player.getAbsorptionAmount();
        }
        if (ignoreSelfDmg.getBVal()) {
            return 69420.0;
        }
        return maxSelfDmg.getDVal();
    }

    public void placeCrystal() {
        this.position = getCrystalPlacePos();
        if (position == null) return;
        try {
            if (rotate.getBVal()) {
                DeltaCore.rotationManager.setRotate(true);
                DeltaCore.rotationManager.setYaw(RotationUtil.getLegitRotations(new Vec3d(position.pos))[0]);
                DeltaCore.rotationManager.setPitch(RotationUtil.getLegitRotations(new Vec3d(position.pos))[1]);
            }
            int slot = mc.player.inventory.currentItem;
            if (silent.getBVal()) InventoryUtils.switchToItem(Items.END_CRYSTAL, true);
            CrystalUtils.placeCrystalOnBlock(PacketType.valueOf(packetType.getMode()), position.pos, silent.getBVal());
            if (silent.getBVal()) InventoryUtils.switchToSlot(slot, true);
            if (fade.getBVal()) DeltaCore.fadeManager.addFadePos(new FadePos(position.pos, new Color((int) r.getDVal(), (int) g.getDVal(), (int) b.getDVal())));
        } catch (Exception e) {
            // cope
        }
    }
}