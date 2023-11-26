package me.astero.unifiedstoragemod.items.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CustomBlockPosData {


    private BlockPos blockPos;
    public CustomBlockPosData(int x, int y, int z) {
        this.blockPos = new BlockPos(x, y, z);

    }

    public BlockPos getBlockPos() {
        return blockPos;
    }


    @Override
    public String toString() {


        return "[x=" + getBlockPos().getX() + ", y=" + getBlockPos().getY()
                + ", z=" + getBlockPos().getZ() + "]";
    }


    public BlockEntity toBlockEntity(Level level) {

        return level.getBlockEntity(blockPos);
    }


}
