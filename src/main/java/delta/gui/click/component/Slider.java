package delta.gui.click.component;

import delta.DeltaCore;
import delta.setting.Setting;
import delta.util.ColourUtils;
import net.minecraft.client.gui.Gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider {
    Setting setting;
    Button button;
    boolean selected;
    int width, height, x, y;

    public Slider(Setting setting, Button button, int x, int y, int width, int height) {
        this.setting = setting;
        this.button = button;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(int mouseX, int mouseY, int x, int y) {
        if (button.open) {
            this.x = x;
            this.y = y;
            double diff = Math.min(this.width, Math.max(0, mouseX - this.x));

            if (selected) {
                if (diff == 0) {
                    this.setting.setDVal(setting.getMin());
                } else {
                    if (setting.isIntVal()) {
                        this.setting.setDVal(roundToPlace((diff / (this.width)) * (this.setting.getMax() - this.setting.getMin()) + this.setting.getMin(), 0));
                    } else {
                        this.setting.setDVal(roundToPlace((diff / (this.width)) * (this.setting.getMax() - this.setting.getMin()) + this.setting.getMin(), 2));
                    }
                }
            }
            int slide = (int) -(((this.width) * this.setting.getDVal() - this.setting.getMin()) / (this.setting.getMin() - this.setting.getMax()));

            Gui.drawRect(this.x + slide, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, y + height + DeltaCore.clickGui.getYOffset() - 1, DeltaCore.clickGui.getButtonColour());
            Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + slide, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, DeltaCore.clickGui.getEnabledButtonColour());
            DeltaCore.clickGui.drawString(DeltaCore.clickGui.mc.fontRenderer, setting.getName() + " - " + setting.getDVal(), x + 2, (this.y + this.height / 2) + DeltaCore.clickGui.getYOffset() - DeltaCore.clickGui.getTextYOffset(), DeltaCore.clickGui.getEnabledButtonTextColour());
            if (isOver(mouseX, mouseY))
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, ColourUtils.ColorUtils.toRGBA(255, 255, 255, 50));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (button.open && mouseButton == 0 && isOver(mouseX, mouseY)) selected = true;
    }

    public void mouseReleased(int state) {
        if (state == 0) selected = false;
    }

    public boolean isOver(int x, int y) {
        return x >= this.x && y > this.y + DeltaCore.clickGui.getYOffset() && x <= this.x + this.width && y <= this.y + this.height + DeltaCore.clickGui.getYOffset();
    }

    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
