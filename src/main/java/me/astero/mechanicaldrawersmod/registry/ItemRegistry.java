package me.astero.mechanicaldrawersmod.registry;

import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry extends ObjectRegistry {



    // Create a Deferred Register to hold Items which will all be registered under the "mechanicaldrawers" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModUtils.MODID);

    private static RegistryObject<Item> testItem = CreativeTabRegistry.addToCreativeTab(
            registerObject("test",
            () -> new Item(new Item.Properties())));






}
