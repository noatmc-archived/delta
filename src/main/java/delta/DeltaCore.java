package delta;

import delta.command.CommandManager;
import delta.gui.click.ClickGui;
import delta.managers.ConfigManager;
import delta.managers.ModuleManager;
import delta.managers.RotationManager;
import delta.managers.ThreadManager;
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

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        mc = Minecraft.getMinecraft();
        EVENT_BUS = new EventBus();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        threadManager = new ThreadManager();
        configManager = new ConfigManager();
        rotationManager = new RotationManager();
        clickGui = new ClickGui();
        EVENT_BUS.subscribe(moduleManager);
        EVENT_BUS.subscribe(commandManager);
        EVENT_BUS.subscribe(rotationManager);
        try {
            configManager.loadModule();
        } catch (Exception exc) {
            // cope
        }
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }
}