package me.astero.mechanicaldrawersmod.registry.data;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemData<T extends Item> implements ObjectData {


    private Supplier<T> itemSupplier;

    public ItemData(Supplier<T> itemSupplier) {

        this.itemSupplier = itemSupplier;
    }

    @Override
    public Supplier get() {
        return itemSupplier;
    }
}
