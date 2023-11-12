package me.astero.mechanicaldrawersmod.registry;


import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerBlockEntity;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry extends ObjectRegistry {

   public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create
           (ForgeRegistries.BLOCK_ENTITY_TYPES, ModUtils.MODID);

   public static final RegistryObject<BlockEntityType<DrawerBlockEntity>> DRAWER_BLOCK_ENTITY =
           BLOCK_ENTITIES.register("drawer_block_entity", () ->
                   BlockEntityType.Builder.of(DrawerBlockEntity::new, BlockRegistry.DRAWER_BLOCK.get())
                           .build(null));












}
