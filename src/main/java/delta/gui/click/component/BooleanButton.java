package delta.gui.click.component;

import delta.DeltaCore;
import delta.setting.Setting;
import delta.util.ColourUtils;
import net.minecraft.client.gui.Gui;

public class BooleanButton {
    Button button;
    Setting setting;
    int width, height, x, y;

    public BooleanButton(Button button, Setting setting, int x, int y, int width, int height) {
        this.button = button;
        this.setting = setting;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(int mouseX, int mouseY, int x, int y) {
        if (button.open) {
            this.x = x;
            this.y = y;
            if (setting.getBVal()) {
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, DeltaCore.clickGui.getEnabledButtonColour());
                DeltaCore.clickGui.drawString(DeltaCore.clickGui.mc.fontRenderer, setting.getName(), this.x + 3, (this.y + this.height / 2) + DeltaCore.clickGui.getYOffset() - DeltaCore.clickGui.getTextYOffset(), DeltaCore.clickGui.getEnabledButtonTextColour());
            } else {
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, DeltaCore.clickGui.getButtonColour());
                DeltaCore.clickGui.drawString(DeltaCore.clickGui.mc.fontRenderer, setting.getName(), this.x + 3, (this.y + this.height / 2) + DeltaCore.clickGui.getYOffset() - DeltaCore.clickGui.getTextYOffset(), DeltaCore.clickGui.getButtonTextColour());
            }

            if (isOver(mouseX, mouseY))
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, ColourUtils.ColorUtils.toRGBA(255, 255, 255, 50));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (button.open && mouseButton == 0 && isOver(mouseX, mouseY) && button.open) setting.setBVal(!setting.getBVal());
    }

    public boolean isOver(int x, int y) {
        return x >= this.x && y > this.y + DeltaCore.clickGui.getYOffset() && x <= this.x + this.width && y <= this.y + this.height + DeltaCore.clickGui.getYOffset();
    }
}
