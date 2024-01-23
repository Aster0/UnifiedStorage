package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
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
    public ItemStack remove(int count) {

        return this.getItem(); // override to not actually remove the item
        // so we can make our own behavior interacting from the storage network.
    }

    @Override
    public void onTake(Player player, ItemStack result) {


        if(!player.level().isClientSide) {

            triggerAchievements(result, result.getCount());
            menu.onItemCrafted(result, false);

        }

    }


    public void onQuickStackCraft(Player player, ItemStack result) {

        if(!player.level().isClientSide) {
            int count = menu.onItemCrafted(result, true);
            triggerAchievements(result, count);


        }

    }

    private void triggerAchievements(ItemStack result, int count) {
        this.onSwapCraft(count); // to trigger the removeCount > 0 to trigger achievements, idk what better way to do this.
        // this makes pressing 1,2,3,4,5 to swap add twice to the scoreboard though. Not a big issue.

        ItemStack resultCopy = result.copy(); // reflects correctly, so when I quick_move craft a stack,
        // it'll show up correctly as 64 or so in the crafting event,


        resultCopy.setCount(count);

        this.checkTakeAchievements(resultCopy);
    }
}
