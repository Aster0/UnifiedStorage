package me.astero.unifiedstoragemod.data;

import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemIdentifier {


    private ItemStack itemStack;

    private Map<String, Integer> locations = new HashMap<>();
    private int count;

    public ItemIdentifier(ItemStack itemStack, int count, Map<String, Integer> locations) {
        this.itemStack = itemStack;
        this.count = count;
        this.locations = locations;

    }

    public ItemIdentifier(ItemStack itemStack, int count) {
        this.itemStack = itemStack;
        this.count = count;

    }

    public Map<String, Integer> getLocations() {
        return locations;
    }

    public void updateLocations(int value) {
        int valueLeft = value;


        for(String key : getLocations().keySet()) {

            int currentValue = getLocations().get(key);


            valueLeft = currentValue + valueLeft;



            getLocations().put(key, Math.max(0, valueLeft));

            valueLeft = Math.abs(valueLeft);



            if(valueLeft == 0)
                break;
        }
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void setCount(int value) {
        this.count = value;
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
            return ItemStack.isSameItemSameTags(itemIdentifier.getItemStack(), itemStack);

        }

        return false;
    }
}
