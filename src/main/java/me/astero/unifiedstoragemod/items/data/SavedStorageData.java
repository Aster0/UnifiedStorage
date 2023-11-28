package me.astero.unifiedstoragemod.items.data;

import me.astero.unifiedstoragemod.utils.ModUtils;

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
