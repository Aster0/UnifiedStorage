package me.astero.mechanicaldrawersmod.registry.blocks.entity.handler;

import me.astero.mechanicaldrawersmod.registry.blocks.DrawerBlock;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerBlockEntity;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.items.ItemStackHandler;

public class DrawerItemStackHandler extends ItemStackHandler {


    private DrawerBlockEntity drawerBlockEntity;

    public DrawerItemStackHandler(DrawerBlockEntity drawerBlockEntity, int size) {
        super(size);

        this.drawerBlockEntity = drawerBlockEntity;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        drawerBlockEntity.setChanged();

        if(!drawerBlockEntity.getLevel().isClientSide)
            drawerBlockEntity.getLevel().sendBlockUpdated(drawerBlockEntity.getBlockPos(),
                    drawerBlockEntity.getBlockState(), drawerBlockEntity.getBlockState(),3);


        AsteroLogger.info("changed");
    }

}
