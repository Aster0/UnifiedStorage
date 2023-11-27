package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            ModUtils.MODID);

    public static final RegistryObject<MenuType<StorageControllerMenu>> GRID_CONTROLLER_MENU = MENUS.register(
            "grid_controller_menu", () -> IForgeMenuType.create(StorageControllerMenu::new));
}
