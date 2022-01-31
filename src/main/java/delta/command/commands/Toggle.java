package delta.command.commands;

import delta.DeltaCore;
import delta.command.Command;
import delta.module.Module;
import delta.util.MessageUtils;

public class Toggle extends Command {
    public Toggle() {
        super("toggle", "toggle <Module Name>", 1, true, true);
    }

    public void execute(String[] args) {
        for (Module module : DeltaCore.moduleManager.getModules()) if (args[1].replaceAll("-", " ").equalsIgnoreCase(module.getName().trim())) {
            module.toggle();
            MessageUtils.sendRainbowMessage("Toggled " + module.getName());
        }
    }
}
