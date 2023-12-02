package me.astero.unifiedstoragemod.integrations.jei.crafting;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public class CraftingRecipeTransfer<C extends StorageControllerMenu, R> implements IRecipeTransferHandler<C, R> {



    public Class<C> menuClass;

    public CraftingRecipeTransfer(Class<C> menuClass) {
        this.menuClass = menuClass;


    }

    @Override
    public Class getContainerClass() {
        return menuClass;
    }

    @Override
    public Optional<MenuType<C>> getMenuType() {
        return Optional.empty();
    }


    @Override
    public RecipeType getRecipeType() {
        return null;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(C container, R recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {

        if(doTransfer)
            container.onRecipeTransfer(recipeSlots);


        return null;
    }



}