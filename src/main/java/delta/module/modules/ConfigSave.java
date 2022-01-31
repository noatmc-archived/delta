package delta.module.modules;

import delta.module.Category;
import delta.module.Module;

public class ConfigSave extends Module {
    public ConfigSave() {
        super("Config Save", "save current state of config", Category.MISC);
    }

    @Override
    public void onEnable() {
    }
}
