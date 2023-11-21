package me.astero.unifiedstoragemod.client.screen.widgets;

import net.minecraft.client.gui.GuiGraphics;

public interface ICustomWidgetComponent {

    void tick(GuiGraphics guiGraphics);
    void onMouseClick(double mouseX, double mouseY);
    void onMouseDrag(double mouseX, double mouseY, int button, double dragX, double dragY);
    void onMouseScrolled(double mouseX, double mouseY, double delta, double rawDelta);
    void onMouseRelease();

}
