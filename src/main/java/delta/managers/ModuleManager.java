package delta.managers;

import delta.event.TickEvent;
import delta.module.Module;
import me.bush.eventbus.annotation.EventListener;
import me.bush.eventbus.annotation.ListenerPriority;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public class    ModuleManager {
    ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        Reflections reflections = new Reflections("delta.module.modules");
        Set<Class<? extends Module>> balls =  reflections.getSubTypesOf(Module.class);
        balls.forEach(f -> {
            try {
                Module mod = f.getConstructor().newInstance();
                modules.add(mod);
            } catch (Exception ignored) {

            }
        });
    }

    public ArrayList<Module> getModules() {
        return modules;
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
