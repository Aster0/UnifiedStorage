package me.astero.mechanicaldrawersmod.registry.blocks.entity.handler;

import me.astero.mechanicaldrawersmod.registry.blocks.DrawerBlock;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerBlockEntity;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
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
        AsteroLogger.info("changed");
    }

}
