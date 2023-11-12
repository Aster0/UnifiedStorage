package me.astero.mechanicaldrawersmod.registry;

import com.mojang.logging.LogUtils;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class ObjectRegistry {

    private static final Logger LOGGER = LogUtils.getLogger();







    public static void register(IEventBus modEventBus) {


        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);

        // to register the static items/blocks
        CreativeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);


    }

    protected static <T extends Item> RegistryObject<T> registerObject(String name, Supplier<T> supplier) {


        RegistryObject<T> registryObject = ItemRegistry.ITEMS.register(name, supplier);

        return registryObject;

    }

    protected static RegistryObject<Item> registerObject(String name, RegistryObject<Block> block) {


        RegistryObject<Item> registryObject = ItemRegistry.ITEMS.register(name, () ->
                new BlockItem(block.get(), new Item.Properties()));

        return registryObject;

    }
}
