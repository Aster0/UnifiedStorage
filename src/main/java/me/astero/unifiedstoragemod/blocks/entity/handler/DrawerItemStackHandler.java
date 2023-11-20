package me.astero.unifiedstoragemod.blocks.entity.handler;

import me.astero.unifiedstoragemod.blocks.entity.DrawerBlockEntity;
import me.astero.unifiedstoragemod.utils.AsteroLogger;
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
        AsteroLogger.info("contents changed");


        drawerBlockEntity.updateRender();


    }

}
