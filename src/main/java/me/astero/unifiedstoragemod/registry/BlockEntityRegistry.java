package me.astero.unifiedstoragemod.registry;


import me.astero.unifiedstoragemod.blocks.entity.DrawerBlockEntity;
import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

   public static final RegistryObject<BlockEntityType<StorageControllerEntity>> DRAWER_CONTROLLER_BLOCK_ENTITY =
           BLOCK_ENTITIES.register("drawer_controller_block_entity", () ->
                   BlockEntityType.Builder.of(StorageControllerEntity::new, BlockRegistry.STORAGE_CONTROLLER_BLOCK.get())
                           .build(null));












}
