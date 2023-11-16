package me.astero.mechanicaldrawersmod.registry;

import me.astero.mechanicaldrawersmod.registry.blocks.DrawerGridControllerBlock;
import me.astero.mechanicaldrawersmod.registry.menu.GridControllerMenu;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
            ModUtils.MODID);

    public static final RegistryObject<MenuType<GridControllerMenu>> GRID_CONTROLLER_MENU = MENUS.register(
            "grid_controller_menu", () -> IForgeMenuType.create(GridControllerMenu::new));
}
