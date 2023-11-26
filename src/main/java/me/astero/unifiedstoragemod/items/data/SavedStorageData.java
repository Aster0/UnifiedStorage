package me.astero.unifiedstoragemod.items.data;

import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class SavedStorageData {


    private CustomBlockPosData customBlockPosData;


    public SavedStorageData(CustomBlockPosData customBlockPosData) {
        this.customBlockPosData = customBlockPosData;
    }


    public CustomBlockPosData getCustomBlockPosData() {
        return customBlockPosData;
    }

    @Override
    public boolean equals(Object obj) {


        if(obj instanceof SavedStorageData savedStorageData) {


            return savedStorageData.customBlockPosData.equals(this.customBlockPosData);
        }

        return false;
    }


    @Override
    public String toString() {
        return ModUtils.serializeBlockPosNbt(customBlockPosData.getBlockPos().toString());
    }
}
