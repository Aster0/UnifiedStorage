package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;

public class StorageGUIScrollWheel extends CustomScrollWheel {


    private StorageControllerMenu menu;
    public StorageGUIScrollWheel(int x, int y, int maxY, int pages, StorageControllerMenu menu) {
        super(x, y, maxY, pages, menu);

        this.menu = menu;
    }

    @Override
    public void onDragDown() {

        super.currentPage = menu.nextPage();


    }

    @Override
    public void onDragUp() {
        super.currentPage = menu.previousPage();
    }



}
