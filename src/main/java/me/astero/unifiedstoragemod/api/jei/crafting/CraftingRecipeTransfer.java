//package me.astero.unifiedstoragemod.api.jei.crafting;
//
//import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
//import mezz.jei.api.helpers.IJeiHelpers;
//import mezz.jei.api.recipe.RecipeType;
//import mezz.jei.api.recipe.transfer.IRecipeTransferError;
//import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
//import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
//import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
//import mezz.jei.api.registration.IRecipeTransferRegistration;
//import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.MenuType;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Optional;
//
//public class CraftingRecipeTransfer<T extends AbstractContainerScreen> implements IRecipeTransferHandler<T, R> {
//
//    @Override
//    public Class getContainerClass() {
//        return null;
//    }
//
//    @Override
//    public Optional<MenuType> getMenuType() {
//        return Optional.empty();
//    }
//
//    @Override
//    public RecipeType getRecipeType() {
//        return null;
//    }
//
//    @Override
//    public @Nullable IRecipeTransferError transferRecipe(AbstractContainerMenu container, Object recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
//        return null;
//    }
//}
