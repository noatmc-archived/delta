package delta.module.modules;

import delta.module.Category;
import delta.module.Module;
import delta.setting.Setting;

public class ModifyCrystal extends Module {
    static ModifyCrystal INSTANCE;
    public ModifyCrystal() {
        super("Modify Crystal", "crystal modification :^", Category.MISC);
        INSTANCE = this;
    }
    public Setting scale = setting("Scale", 1.0, 0.1, 1.0, false);
    public Setting spinSpeed = setting("Spin Speed", 1.0, 0.1, 10.0, false);
    public Setting bounce = setting("Bounce", 1.0, 0.1, 10.0, false);

    public static ModifyCrystal getINSTANCE() {
        return INSTANCE;
    }
}
