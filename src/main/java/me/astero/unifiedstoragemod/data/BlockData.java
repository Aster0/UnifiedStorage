package me.astero.unifiedstoragemod.data;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BlockData implements ObjectData {


    private Supplier<Block> itemSupplier;

    public BlockData(Supplier<Block> itemSupplier) {

        this.itemSupplier = itemSupplier;
    }

    @Override
    public Supplier get() {
        return itemSupplier;
    }
}
