package me.astero.unifiedstoragemod.client.screen.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public abstract class BaseUpgradeSlot {



    public void renderCustomTooltip(List<Component> componentList, GuiGraphics guiGraphics, Font font, int x, int y) {
        guiGraphics.renderTooltip(font, componentList, Optional.empty(), x + 20, y + 30);
    }


    public abstract void renderCustomTooltip(GuiGraphics guiGraphics, Font font);
}
