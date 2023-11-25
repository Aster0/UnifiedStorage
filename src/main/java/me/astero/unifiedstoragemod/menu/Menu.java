package me.astero.unifiedstoragemod.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class Menu extends AbstractContainerMenu {

    protected Menu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }


    public abstract void addCustomSlot(Slot slot);
}
