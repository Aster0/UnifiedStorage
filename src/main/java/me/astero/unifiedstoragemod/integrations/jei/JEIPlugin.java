package me.astero.unifiedstoragemod.integrations.jei;


import me.astero.unifiedstoragemod.integrations.jei.crafting.CraftingRecipeTransfer;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerBlockMenu;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerItemMenu;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.utils.ModUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;

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
}