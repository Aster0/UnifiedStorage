package me.astero.mechanicaldrawersmod.data;

import net.minecraft.world.item.ItemStack;

public class ItemIdentifier {


    private ItemStack itemStack;
    private int count;

    public ItemIdentifier(ItemStack itemStack, int count) {
        this.itemStack = itemStack;
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o){

        if(o instanceof ItemIdentifier itemIdentifier) {
            return itemIdentifier.itemStack.equals(this.itemStack, false);
        }

        return false;
    }
}
