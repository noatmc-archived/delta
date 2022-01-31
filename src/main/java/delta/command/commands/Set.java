package delta.command.commands;

import delta.DeltaCore;
import delta.command.Command;
import delta.module.Module;
import delta.setting.Setting;

public class Set extends Command {
    public Set() {
        super("set", "set <module> <setting> <value>", 3, true, false);
    }

    public void execute(String[] args) {
        for (Module module : DeltaCore.moduleManager.getModules()) if (args[1].replace('-', ' ').equalsIgnoreCase(module.getName().trim())) for (Setting setting : module.getSettings()) if (args[2].equalsIgnoreCase(setting.getName())) setSetting(setting, args[3]);
    }

    public void setSetting(Setting setting, String value) {
        if (setting.isMVal()) setting.setMode(value);
        if (setting.isBVal() && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) setting.setBVal(Boolean.parseBoolean(value));
        if (setting.isDVal() || setting.isIntVal()) setting.setDVal(Double.parseDouble(value));
    }
}
