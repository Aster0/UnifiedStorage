package me.astero.unifiedstoragemod.client.screen.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public interface ICustomWidgetComponent {

    void tick(GuiGraphics guiGraphics, int leftPos, int topPos);
    void onMouseClick(double mouseX, double mouseY);
    void onMouseDrag(double mouseX, double mouseY, int button, double dragX, double dragY);
    void onMouseScrolled(double mouseX, double mouseY, double delta, double rawDelta);
    void onMouseRelease();

    void renderCustomTooltip(GuiGraphics guiGraphics, Font font, int x, int y, Slot slot);

    static void tickAll(List<ICustomWidgetComponent> widgets, GuiGraphics guiGraphics, int leftPos, int topPos) {

        for(ICustomWidgetComponent widget : widgets) {
            widget.tick(guiGraphics, leftPos, topPos);
        }
    }

    static void renderToolTipAll(List<ICustomWidgetComponent> widgets, GuiGraphics guiGraphics, Font font, int x, int y, Slot slot) {

        for(ICustomWidgetComponent widget : widgets) {
            widget.renderCustomTooltip(guiGraphics, font, x, y, slot);
        }
    }

}
