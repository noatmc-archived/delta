package delta;

import delta.command.CommandManager;
import delta.gui.click.ClickGui;
import delta.managers.*;
import me.bush.eventbus.bus.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@net.minecraftforge.fml.common.Mod(modid = DeltaCore.MOD_ID, name = DeltaCore.NAME, version = DeltaCore.VERSION)
public class DeltaCore {
    public static Minecraft mc;
    public static final String NAME = "Delta";
    public static final String MOD_ID = "delta";
    public static final String VERSION = "0.0.1";
    public static final String TITLE = NAME + " " + VERSION;

    public static EventBus EVENT_BUS;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static ClickGui clickGui;
    public static ThreadManager threadManager;
    public static RotationManager rotationManager;
    public static ConfigManager configManager;
    public static TotemPopManager totemPopManager;
    public static EventManager eventManager;
    public static FadeManager fadeManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        mc = Minecraft.getMinecraft();
        EVENT_BUS = new EventBus();
        moduleManager = new ModuleManager();
        totemPopManager = new TotemPopManager();
        commandManager = new CommandManager();
        threadManager = new ThreadManager();
        configManager = new ConfigManager();
        rotationManager = new RotationManager();
        eventManager = new EventManager();
        fadeManager = new FadeManager();
        clickGui = new ClickGui();
        EVENT_BUS.subscribe(totemPopManager);
        EVENT_BUS.subscribe(moduleManager);
        EVENT_BUS.subscribe(commandManager);
        EVENT_BUS.subscribe(rotationManager);
        EVENT_BUS.subscribe(eventManager);
        EVENT_BUS.subscribe(fadeManager);
        try {
            configManager.loadModule();
        } catch (Exception exc) {
            // cope
        }
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }
}