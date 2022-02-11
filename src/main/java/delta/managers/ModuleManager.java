package delta.managers;

import delta.DeltaCore;
import delta.event.TickEvent;
import delta.module.Module;
import delta.module.modules.*;
import me.bush.eventbus.annotation.EventListener;
import me.bush.eventbus.annotation.ListenerPriority;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ModuleManager {
    ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new AutoCrystal());
        modules.add(new AutoDuper());
        modules.add(new ClickGUI());
        modules.add(new ConfigSave());
        modules.add(new Effects());
        modules.add(new FakePlayer());
        modules.add(new HUD());
        modules.add(new BedAura());
        modules.add(new ModifyCrystal());
        modules.add(new RPC());
        modules.add(new TotemPopCounter());
        modules.add(new Velocity());
        modules.add(new CityAlert());
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> a = new ArrayList<>();
        for (Module mod : modules) {
            if (mod.isToggled()) a.add(mod);
        }
        return a;
    }

    public ArrayList<Module> sorted() {
        return (ArrayList<Module>) getEnabledModules().stream().sorted(Comparator.comparing(t-> DeltaCore.fontRenderer.getStringWidth(t.getFullString()))).collect(Collectors.toList());
    }

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void onTick(TickEvent.Pre event) {
        for (Module module : modules) {
            module.tick();
        }
    }

    public void onEntityAdded(Entity entity) {
        for (Module module : modules) {
            if (module.isToggled()) {
                module.onEntitySpawn(entity);
            }
        }
    }

    public void onEntityRemoved(Entity entity) {
        for (Module module : modules) {
            if (module.isToggled()) {
                module.onEntityRemoved(entity);
            }
        }
    }

    public void onMotion() {
        for (Module module : modules) {
            if (module.isToggled()) {
                module.onMotion();
            }
        }
    }

    public void onBlockDestroyed(BlockPos pos) {
        for (Module module : modules) {
            if (module.isToggled()) {
                module.onBlockDestroyed(pos);
            }
        }
    }



    public void onThread() {
        for (Module module : modules) {
            module.onThread();
        }
    }
}
