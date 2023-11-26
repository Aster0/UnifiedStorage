package me.astero.unifiedstoragemod.items.data;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class SavedStorageData {


    private LazyOptional<IItemHandler> inventory;
    private CustomBlockPosData customBlockPosData;


    public SavedStorageData(LazyOptional<IItemHandler> inventory, CustomBlockPosData customBlockPosData) {
        this.inventory = inventory;
        this.customBlockPosData = customBlockPosData;
    }


    public void setInventory(LazyOptional<IItemHandler> inventory) {
        this.inventory = inventory;
    }

    public LazyOptional<IItemHandler> getInventory() {
        return inventory;
    }

    @Override
    public boolean equals(Object obj) {


        if(obj instanceof SavedStorageData savedStorageData) {

            IItemHandler inventory1 = inventory.orElse(null);
            IItemHandler inventory2 = savedStorageData.inventory.orElse(null);


            System.out.println(inventory2);

            if(inventory1 == null || inventory2 == null) {
                System.out.println("NULL!!!!!");
                return false;
            }


            for(int i = 0; i < inventory1.getSlots(); i++) {
                ItemStack stack1 = inventory1.getStackInSlot(i);
                ItemStack stack2 = inventory2.getStackInSlot(i);




                if (!ItemStack.isSameItemSameTags(stack1, stack2)) {
                    return false;
                }
            }





        }

        return true;
    }
}
