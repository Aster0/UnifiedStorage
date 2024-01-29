package me.astero.unifiedstoragemod.items.generic;

import net.minecraftforge.items.ItemStackHandler;

public abstract class StorageDisc {


    private final int size;
    private final ItemStackHandler itemStackHandler;


    public StorageDisc(int size) {
        this.size = size;

        itemStackHandler = new ItemStackHandler(size);
    }



}
