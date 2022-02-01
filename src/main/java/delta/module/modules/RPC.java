package delta.module.modules;

import delta.DeltaRPC;
import delta.module.Category;
import delta.module.Module;

public class RPC extends Module {
    public RPC() {
        super("RPC", "please stop having orgasm catuquei", Category.MISC);
    }

    @Override
    public void onEnable() {
        DeltaRPC.startRPC();
    }

    @Override
    public void onDisable() {
        DeltaRPC.stopRPC();
    }
}
