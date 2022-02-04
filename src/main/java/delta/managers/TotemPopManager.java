package delta.managers;

import delta.DeltaCore;
import delta.event.PacketEvent;
import delta.event.TickEvent;
import delta.event.TotemPopEvent;
import delta.util.Wrapper;
import me.bush.eventbus.annotation.EventListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.HashMap;

@SuppressWarnings("all")
public class TotemPopManager implements Wrapper {
    public static HashMap<Entity, Integer> totemMap = new HashMap<>();

    private static void update() {
        for(Entity entity : totemMap.keySet()) {
            if(!mc.world.loadedEntityList.contains(entity)) {
                totemMap.remove(entity);
            }
        }
    }

    @EventListener
    public void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) {
            totemMap.clear();
            return;
        }
        update();
    }

    @EventListener
    public void onTotemPop(PacketEvent.Receive event) {
//        if (nullCheck) return;
        if (event.getPacket() instanceof SPacketEntityStatus) {
            if (((SPacketEntityStatus) event.getPacket()).getOpCode() == 0x23 && ((SPacketEntityStatus) event.getPacket()).getEntity(mc.world) instanceof EntityPlayer) {
                if (totemMap.containsKey(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world))) {
                    int times = totemMap.get(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world)) + 1;
                    DeltaCore.EVENT_BUS.post(new TotemPopEvent(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world), times));
                    totemMap.remove(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world));
                    totemMap.put(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world), times);
                } else {
                    DeltaCore.EVENT_BUS.post(new TotemPopEvent(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world),  1));
                    totemMap.put(((SPacketEntityStatus)event.getPacket()).getEntity(mc.world), 1);
                }
            }
        }
    }

    public HashMap<Entity, Integer> getTotemMap() {
        return totemMap;
    }

    public void resetPops() {
        totemMap.clear();
    }
}
