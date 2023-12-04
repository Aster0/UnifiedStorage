package me.astero.unifiedstoragemod.client.screen.widgets.generic;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CustomButton extends ImageButton {

    public CustomButton(int x, int y, int width, int height, WidgetSprites sprites, MutableComponent component, OnPress onPress) {
        super(x, y, width, height, sprites, onPress);

        this.setTooltip(Tooltip.create(component));
    }

    @Override
    public void onRelease(double p_93669_, double p_93670_) {

        this.setFocused(false);
        super.onRelease(p_93669_, p_93670_);
    }
}
