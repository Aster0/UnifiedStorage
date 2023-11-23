package me.astero.unifiedstoragemod.menu.interfaces;

import me.astero.unifiedstoragemod.menu.enums.MouseAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface IMenuInteractor {
    void interactWithMenu(ItemStack itemStack, boolean take, int value, boolean quickMove);
}
