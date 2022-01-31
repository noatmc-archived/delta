package delta.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import delta.DeltaCore;
import delta.setting.Setting;
import delta.util.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

public class Module {
    public static Minecraft mc = DeltaCore.mc;
    String name, description;
    boolean toggled, keyDown;
    Category category;
    ArrayList<Setting> settings = new ArrayList<>();
    int key;


    public Module(String name, String description, Category category, int key) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = key;
        settings.add(new Setting("Drawn", true));
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        key = Keyboard.KEY_NONE;
        settings.add(new Setting("Drawn", true));
    }

    public void addSetting(Setting... setting) {
        this.settings.addAll(Arrays.asList(setting));
    }

    public void enable() {
        DeltaCore.EVENT_BUS.subscribe(this);
        onEnable();
    }

    public void disable() {
        DeltaCore.EVENT_BUS.unsubscribe(this);
        onDisable();
    }

    public void onEnable() {}

    public void onDisable() {}


    public void tick() {
        if (key != -1) {
            boolean isDown = Keyboard.isKeyDown(key);
            if (isDown && !keyDown && mc.currentScreen == null) toggle();
            keyDown = isDown;
            if (isToggled()) onUpdate();
        }
        onTick();
    }

    public void onThread() {
        // THREAD - GAMING
    }

    public Setting setting(String name, boolean value) {
        Setting setting = new Setting(name, value);
        addSetting(setting);
        return setting;
    }

    public Setting setting(String name, double val, double min, double max, boolean integer) {
        Setting setting = new Setting(name, val, min, max, integer);
        addSetting(setting);
        return setting;
    }

    public Setting setting(String name, String[] mode) {
        Setting setting = new Setting(name, mode);
        addSetting(setting);
        return setting;
    }

    public void onTick() {

    }

    public void onUpdate() {

    }

    public void toggle() {
        toggled = !toggled;
        MessageUtils.sendMessage("Delta - " + this.getName() + " " + (toggled ? ChatFormatting.BOLD + "" + ChatFormatting.GREEN + "ENABLED" : ChatFormatting.BOLD + "" + ChatFormatting.RED + "DISABLED"), true);
        if (toggled) enable(); else disable();
    }

    public void setToggled(boolean toggle) {
//        toggled = !toggled;
//        MessageUtils.sendMessage("Delta - " + this.getName() + " " + (toggled ? ChatFormatting.BOLD + "" + ChatFormatting.GREEN + "ENABLED" : ChatFormatting.BOLD + "" + ChatFormatting.RED + "DISABLED"), true);
        this.toggled = toggle;
        if (toggled) enable(); else disable();
    }

    public boolean isToggled() {
        return toggled;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public boolean fullNullCheck() {
        return mc.player == null || mc.world == null;
    }

    public void onEntitySpawn(Entity entity) {
    }

    public void onEntityRemoved(Entity entity) {
    }

    public void onBlockDestroyed(BlockPos pos) {
    }
}
