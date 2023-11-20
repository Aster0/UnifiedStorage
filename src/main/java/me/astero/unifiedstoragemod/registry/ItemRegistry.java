package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.data.ItemData;
import me.astero.unifiedstoragemod.items.GridWrenchItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {



    // Create a Deferred Register to hold Items which will all be registered under the "mechanicaldrawers" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    private static RegistryObject<Item> testItem =
            ObjectRegistry.registerObject("test", new ItemData<Item>(
            () -> new Item(new Item.Properties())), true);

    private static RegistryObject<Item> GRID_WRENCH =
            ObjectRegistry.registerObject("grid_wrench", new ItemData<Item>(
                    () -> new GridWrenchItem(new Item.Properties())), true);






}
