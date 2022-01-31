package delta.command.commands;

import delta.DeltaCore;
import delta.command.Command;
import delta.module.Module;
import delta.util.MessageUtils;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("bind", "bind <Module Name> <Key>", 2, true, true);
    }

    public void execute(String[] args) {
        for (Module module : DeltaCore.moduleManager.getModules()) if (args[1].replaceAll("-", " ").equalsIgnoreCase(module.getName().trim())) {
            module.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
            MessageUtils.sendRainbowMessage("Binded " + module.getName() + " To " + Keyboard.getKeyName(module.getKey()));
        }
    }
}
