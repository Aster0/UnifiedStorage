package me.astero.mechanicaldrawersmod.registry.blocks.entity;

import me.astero.mechanicaldrawersmod.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrawerBlockEntity extends BlockEntity {


    public DrawerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DRAWER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
    }
}
