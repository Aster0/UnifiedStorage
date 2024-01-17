package me.astero.unifiedstoragemod.items.upgrades;

import net.minecraft.world.level.block.entity.BlockEntity;


/*
    When an item is capable of updating a block entity.
 */
public interface IBlockUpdater {


    /*
        Actions to take when updating the Block Entity
        @params
     */
    void update(BlockEntity blockEntity);
}
