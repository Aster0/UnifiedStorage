package me.astero.unifiedstoragemod.registry;

import me.astero.unifiedstoragemod.data.BlockData;
import me.astero.unifiedstoragemod.data.ItemData;
import me.astero.unifiedstoragemod.data.ObjectData;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ObjectRegistry {









    public static void register(IEventBus modEventBus) {


        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);

        // to register the static items/blocks
        CreativeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);

        MenuRegistry.MENUS.register(modEventBus);


    }

    protected static <T extends ItemLike> RegistryObject<T> registerObject(String name, ObjectData<T> objectData,
                                                                           boolean addToCreativeTab) {

        Supplier<? extends Item> itemSupplier = null;
        RegistryObject<T> toReturn = null;

        RegistryObject<Block> registeredBlock = null;

        if(objectData instanceof ItemData itemData) {

            itemSupplier = itemData.get();


        }
        else if(objectData instanceof BlockData blockData) {

            Supplier<Block> blockSupplier = blockData.get();

            registeredBlock = BlockRegistry.BLOCKS.register(name, blockSupplier);

            final RegistryObject<Block> finalRegisteredBlock = registeredBlock;
            itemSupplier = () -> new BlockItem(finalRegisteredBlock.get(), new Item.Properties());


        }

        RegistryObject<? extends Item> registeredItem = ItemRegistry.ITEMS.register(name, itemSupplier);

        if(addToCreativeTab)
            CreativeTabRegistry.addToCreativeTab(registeredItem);



        if(registeredBlock == null) {
            return (RegistryObject<T>) registeredItem;
        }



        return (RegistryObject<T>) registeredBlock;

    }



}
