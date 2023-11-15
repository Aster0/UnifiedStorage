package me.astero.mechanicaldrawersmod.registry;


import me.astero.mechanicaldrawersmod.registry.blocks.DrawerBlock;
import me.astero.mechanicaldrawersmod.registry.data.BlockData;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry  {

    //    // Creates a new Block with the id "mechanicaldrawers:example_block", combining the namespace and path
//    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register
//    ("example_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
//    // Creates a new BlockItem with the id "mechanicaldrawers:example_block", combining the namespace and path

//    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register
//    ("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Create a Deferred Register to hold Blocks which will all be registered under the "mechanicaldrawers" namespace




    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModUtils.MODID);


//    public static final RegistryObject<Block> DRAWER_BLOCK = BLOCKS.register
//            ("drawer_block", () -> new DrawerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
//                    .strength(1f), 70));
//
//
//
//
//
//
//    public static final RegistryObject<Item> DRAWER_BLOCK_ITEM = CreativeTabRegistry.addToCreativeTab
//            (ObjectRegistry.registerObject("drawer_block",
//                    DRAWER_BLOCK));


    public static final RegistryObject<Block> DRAWER_BLOCK = ObjectRegistry.registerObject
            ("drawer_block", new BlockData(() -> new DrawerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .strength(1f), 70)), true);












}
