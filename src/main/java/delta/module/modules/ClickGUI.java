package delta.module.modules;

import delta.DeltaCore;
import delta.module.Category;
import delta.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

    public ClickGUI() {
        super("ClickGUI", "Graphical User Interface for Configuring the modules", Category.CLIENT, Keyboard.KEY_O);
    }

    public void onEnable() {
        mc.displayGuiScreen(DeltaCore.clickGui);
        toggle();
    }
}
