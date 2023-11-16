package me.astero.mechanicaldrawersmod.registry.items.data;

import net.minecraft.core.BlockPos;

public class CustomBlockPosData {


    private BlockPos blockPos;
    public CustomBlockPosData(int x, int y, int z) {
        this.blockPos = new BlockPos(x, y, z);

    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
