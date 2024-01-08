package me.astero.unifiedstoragemod.data;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BlockData<T extends Block> implements ObjectData<T> {


    private Supplier<T> itemSupplier;

    public BlockData(Supplier<T> itemSupplier) {

        this.itemSupplier = itemSupplier;
    }

    @Override
    public Supplier<T> get() {
        return itemSupplier;
    }
}
