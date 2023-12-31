package me.astero.unifiedstoragemod.integrations.jei.crafting;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.utils.ModUtils;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CraftingRecipeTransfer<C extends StorageControllerMenu, R> implements IRecipeTransferHandler<C, R> {



    private Class<C> menuClass;

    private IRecipeTransferHandlerHelper helper;

    public CraftingRecipeTransfer(Class<C> menuClass, IRecipeTransferHandlerHelper helper) {
        this.menuClass = menuClass;
        this.helper = helper;


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
    public @Nullable IRecipeTransferError transferRecipe(C container,
                                                         R recipe, IRecipeSlotsView recipeSlots,
                                                         Player player, boolean maxTransfer, boolean doTransfer) {


        if(!container.getStorageControllerEntity().isCraftingEnabled()) {
            return helper.createUserErrorWithTooltip(Component.translatable(
                    "jei." + ModUtils.MODID + ".crafting_not_enabled"));
        }


        if(doTransfer)
            container.onRecipeTransfer(recipeSlots);


        return null;
    }



}