package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;

public enum SlotType {
    UPGRADE(216, 3, 8, -1, new ItemStack(ItemRegistry.SHADOW_UPGRADE_CARD.get())),
    NETWORK(80, 2, 10, 0,null),
    FILTER(80, 2, 10, 0,null),
    VISUAL_BLOCK(103, 3, 10, 0,null);


    private int iconHeight, iconWidth, offsetX, offsetY;
    private ItemStack customItemStack; // only for textures above 16x16

    SlotType(int iconHeight, int iconWidth, int offsetX, int offsetY, ItemStack customItemStack) {

        this.iconHeight = iconHeight;
        this.iconWidth = iconWidth;
        this.offsetX = offsetX;
        this.customItemStack = customItemStack;
        this.offsetY = offsetY;

    }

    public ItemStack getCustomItemStack() {
        return customItemStack;
    }


    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public int getIconWidth() {
        return iconWidth;
    }
}
