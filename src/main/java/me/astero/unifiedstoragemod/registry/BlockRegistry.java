package me.astero.unifiedstoragemod.registry;


import me.astero.unifiedstoragemod.blocks.DrawerBlock;
import me.astero.unifiedstoragemod.blocks.StorageControllerBlock;
import me.astero.unifiedstoragemod.data.BlockData;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

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
            ("drawer_block", new BlockData<>(() -> new DrawerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .strength(1f), 70)), true);




    public static final Map<DyeColor, RegistryObject<Block>> STORAGE_CONTROLLER_BLOCK_COLORED = registerStorageControllerColored();

    public static Map<DyeColor, RegistryObject<Block>> registerStorageControllerColored() {

        Map<DyeColor, RegistryObject<Block>> blocks = new HashMap<>();

        for(DyeColor color : DyeColor.values()) {

            String dyeName = color.name().toLowerCase();

            blocks.put(color, ObjectRegistry.registerObject
                    ("storage_controller" + (dyeName.equals("white") ? "" : "_" + dyeName), new BlockData<>(() ->
                            new StorageControllerBlock(BlockBehaviour.Properties.of().lightLevel(value -> 10)
                                    .strength(0.2f).requiresCorrectToolForDrops())), true));

        }

        return blocks;
    }



}
