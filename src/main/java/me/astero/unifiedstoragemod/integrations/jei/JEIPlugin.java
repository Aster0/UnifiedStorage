package me.astero.unifiedstoragemod.integrations.jei;


import me.astero.unifiedstoragemod.integrations.jei.crafting.CraftingRecipeTransfer;
import me.astero.unifiedstoragemod.integrations.jei.recipes.StorageControllerColorCategory;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerBlockMenu;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPlugin implements IModPlugin {


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ModUtils.MODID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {

        registration.addRecipeTransferHandler(new CraftingRecipeTransfer<>(StorageControllerBlockMenu.class,
                        registration.getTransferHelper()),
                RecipeTypes.CRAFTING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(new StorageControllerColorCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {


//        registration.addIngredientInfo(new
//                ItemStack(BlockRegistry.STORAGE_CONTROLLER_BLOCK_COLORED.get(DyeColor.WHITE").get()),
//                VanillaTypes.ITEM_STACK, Component.literal("hi"));

        registration.addRecipes(StorageControllerColorCategory.RECIPE_TYPE,
                StorageControllerColorCategory.getRecipes());


    }


}


