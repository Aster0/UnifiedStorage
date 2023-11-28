package me.astero.unifiedstoragemod.menu.interfaces;

import net.minecraft.world.item.ItemStack;

public interface IMenuInteractor {
    void interactWithMenu(ItemStack itemStack, boolean take, int value, boolean quickMove, int slotIndex);
}
