package me.astero.unifiedstoragemod.menu.interfaces;

import me.astero.unifiedstoragemod.menu.enums.InventoryAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface IMenuInteractor {


    void interactWithMenu(ClickType clickType, InventoryAction action, ItemStack itemStack, Slot slot, boolean cameFromStorage);
    void interactWithMenu(ItemStack itemStack);
}
