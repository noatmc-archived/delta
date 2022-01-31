package delta.gui.click.component;

import delta.DeltaCore;
import delta.util.ColourUtils;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class KeybindButton {
    Button button;
    int width, height, x, y;

    public KeybindButton(Button button, int x, int y, int width, int height) {
        this.button = button;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void render(int mouseX, int mouseY, int x, int y) {
        this.x = x;
        this.y = y;
        if (button.open) {
            if (DeltaCore.clickGui.getSelectedKeybindButton() == this) {
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, DeltaCore.clickGui.getEnabledButtonColour());
                DeltaCore.clickGui.drawString(DeltaCore.clickGui.mc.fontRenderer, "Key - " + Keyboard.getKeyName(button.module.getKey()), x + 3, (y + height / 2) + DeltaCore.clickGui.getYOffset() - DeltaCore.clickGui.getTextYOffset(), DeltaCore.clickGui.getEnabledButtonTextColour());
            } else {
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, DeltaCore.clickGui.getButtonColour());
                DeltaCore.clickGui.drawString(DeltaCore.clickGui.mc.fontRenderer, "Key - " + Keyboard.getKeyName(button.module.getKey()), x + 3, (y + height / 2) + DeltaCore.clickGui.getYOffset() - DeltaCore.clickGui.getTextYOffset(), DeltaCore.clickGui.getButtonTextColour());
            }

            if (isOver(mouseX, mouseY))
                Gui.drawRect(this.x, this.y + DeltaCore.clickGui.getYOffset(), this.x + this.width, this.y + this.height + DeltaCore.clickGui.getYOffset() - 1, ColourUtils.ColorUtils.toRGBA(255, 255, 255, 50));
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (button.open) {
            if (mouseButton == 0 && isOver(mouseX, mouseY)) {
                if (DeltaCore.clickGui.getSelectedKeybindButton() == this) {
                    DeltaCore.clickGui.setSelectedKeybindButton(null);
                } else {
                    DeltaCore.clickGui.setSelectedKeybindButton(this);
                }
            }
        }
    }

    public void keyTyped(int keyCode) {
        if (DeltaCore.clickGui.getSelectedKeybindButton() == this) {
            if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_ESCAPE) {
                this.button.module.setKey(Keyboard.KEY_NONE);
            } else {
                this.button.module.setKey(keyCode);
            }
        }
    }

    public boolean isOver(int x, int y) {
        return x >= this.x && y > this.y + DeltaCore.clickGui.getYOffset() && x <= this.x + this.width && y <= this.y + this.height + DeltaCore.clickGui.getYOffset();
    }
}
