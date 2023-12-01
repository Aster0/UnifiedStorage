//package me.astero.unifiedstoragemod.api.jei;
//
//
//import me.astero.unifiedstoragemod.client.screen.StorageControllerScreen;
//import me.astero.unifiedstoragemod.utils.ModUtils;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.constants.RecipeTypes;
//import mezz.jei.api.registration.IGuiHandlerRegistration;
//import mezz.jei.api.registration.IRecipeTransferRegistration;
//import net.minecraft.resources.ResourceLocation;
//
//@JeiPlugin
//public class JEIPlugin implements IModPlugin {
//
//
//    @Override
//    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//
//        registration.addRecipeClickArea(StorageControllerScreen.class,
//                88, 32, 28, 23);
//    }
//    @Override
//    public ResourceLocation getPluginUid() {
//        return new ResourceLocation(ModUtils.MODID);
//    }
//
//
//    @Override
//    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//
////        registration.addRecipeTransferHandler(new CraftingRecipeTransfer());
//    }
//}