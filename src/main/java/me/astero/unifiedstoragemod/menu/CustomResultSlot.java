package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;

public class CustomResultSlot<T extends StorageControllerMenu> extends ResultSlot {

    private CraftingContainer craftSlots;
    private T menu;
    public CustomResultSlot(Player player, CraftingContainer craftingContainer,
                            Container resultContainer, int p_40169_, int x, int y, T t) {
        super(player, craftingContainer, resultContainer, p_40169_, x, y);

        this.craftSlots = craftingContainer;
        this.menu = t;
    }

    @Override
    public ItemStack remove(int index) {

        return this.container.getItem(index); // override to not actually remove the item
        // so we can make our own behavior interacting from the storage network.
    }

    @Override
    public void onTake(Player player, ItemStack result) {

        this.checkTakeAchievements(result);

        if(!player.level().isClientSide) {
            menu.onItemCrafted(result, false);
        }

    }


    public void onQuickStackCraft(ItemStack result) {
        menu.onItemCrafted(result, true);
    }
}
