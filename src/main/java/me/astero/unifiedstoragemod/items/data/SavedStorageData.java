package me.astero.unifiedstoragemod.items.data;

import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.world.level.Level;

public class SavedStorageData {


    private CustomBlockPosData customBlockPosData;
    private Level level;


    public SavedStorageData(CustomBlockPosData customBlockPosData, Level level) {
        this.customBlockPosData = customBlockPosData;
        this.level = level;
    }

    public CustomBlockPosData getCustomBlockPosData() {
        return customBlockPosData;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {

        return this.level;
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
