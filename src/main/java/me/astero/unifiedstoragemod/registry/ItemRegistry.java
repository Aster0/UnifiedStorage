package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.data.ItemData;
import me.astero.unifiedstoragemod.items.CraftingUpgradeCard;
import me.astero.unifiedstoragemod.items.GridWrenchItem;

import me.astero.unifiedstoragemod.items.StorageNetworkCard;
import me.astero.unifiedstoragemod.items.StorageWings;
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

    public static RegistryObject<Item> STORAGE_WINGS =
            ObjectRegistry.registerObject("storage_wings", new ItemData<Item>(
                    () -> new StorageWings(new Item.Properties().stacksTo(1))), true);

    public static RegistryObject<Item> NETWORK_CARD =
            ObjectRegistry.registerObject("network_card", new ItemData<Item>(
                    () -> new StorageNetworkCard(new Item.Properties().stacksTo(1))), true);

    public static RegistryObject<Item> CRAFTING_UPGRADE_CARD =
            ObjectRegistry.registerObject("crafting_upgrade_card", new ItemData<Item>(
                    () -> new CraftingUpgradeCard(new Item.Properties())), true);

    public static RegistryObject<Item> BLANK_UPGRADE_CARD =
            ObjectRegistry.registerObject("blank_upgrade_card", new ItemData<Item>(
                    () -> new CraftingUpgradeCard(new Item.Properties())), true);

    public static RegistryObject<Item> SHADOW_UPGRADE_CARD =
            ObjectRegistry.registerObject("shadow_upgrade_card", new ItemData<Item>(
                    () -> new Item(new Item.Properties())), false);


//    private static RegistryObject<Item> WIRELESS_STORAGE =
//            ObjectRegistry.registerObject("wireless_storage", new ItemData<Item>(
//                    () -> new WirelessStorage(new Item.Properties())), true);






}
