package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerBlockMenu;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerItemMenu;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            ModUtils.MODID);

    public static final RegistryObject<MenuType<StorageControllerMenu>> STORAGE_CONTROLLER_MENU = MENUS.register(
            "storage_controller_menu", () -> IForgeMenuType.create(StorageControllerBlockMenu::new));

    public static final RegistryObject<MenuType<StorageControllerMenu>> STORAGE_CONTROLLER_ITEM_MENU = MENUS.register(
            "storage_controller_item_menu", () -> {


                return IForgeMenuType.create((c, dd, p) -> {

                    return new StorageControllerItemMenu(c, dd, p);
                });
            });
}
