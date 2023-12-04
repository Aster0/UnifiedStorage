package me.astero.unifiedstoragemod.client.screen.widgets.generic;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Optional;

public abstract class BaseUpgradeSlot {



    public void renderCustomTooltip(List<Component> componentList, GuiGraphics guiGraphics, Font font, int x, int y, Slot slot) {


        guiGraphics.renderTooltip(font, componentList, Optional.empty(), x + 20, y + 30);
    }



}
