package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.data.ItemData;
import me.astero.unifiedstoragemod.items.CraftingUpgradeCardItem;
import me.astero.unifiedstoragemod.items.GridWrenchItem;

import me.astero.unifiedstoragemod.items.StorageNetworkCardItem;
import me.astero.unifiedstoragemod.items.StorageWingsItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {



    // Create a Deferred Register to hold Items which will all be registered under the "mechanicaldrawers" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);


    private static RegistryObject<Item> GRID_WRENCH =
            ObjectRegistry.registerObject("grid_wrench", new ItemData<Item>(
                    () -> new GridWrenchItem(new Item.Properties())), true);

    private static RegistryObject<Item> STORAGE_WINGS =
            ObjectRegistry.registerObject("storage_wings", new ItemData<Item>(
                    () -> new StorageWingsItem(new Item.Properties().stacksTo(1))), true);

    public static RegistryObject<Item> NETWORK_CARD =
            ObjectRegistry.registerObject("network_card", new ItemData<Item>(
                    () -> new StorageNetworkCardItem(new Item.Properties().stacksTo(1))), true);

    public static RegistryObject<Item> CRAFTING_UPGRADE_CARD =
            ObjectRegistry.registerObject("crafting_upgrade_card", new ItemData<Item>(
                    () -> new CraftingUpgradeCardItem(new Item.Properties())), true);


//    private static RegistryObject<Item> WIRELESS_STORAGE =
//            ObjectRegistry.registerObject("wireless_storage", new ItemData<Item>(
//                    () -> new WirelessStorage(new Item.Properties())), true);






}
