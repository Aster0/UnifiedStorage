package me.astero.unifiedstoragemod.blocks.colors;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;

public class StorageControllerColors {


    private List<Integer> dyeColors = new ArrayList<>();
    public StorageControllerColors() {

        for(DyeColor color : DyeColor.values()) {

            dyeColors.add(color.getMapColor().calculateRGBColor(MapColor.Brightness.NORMAL));
        }

    }
}
