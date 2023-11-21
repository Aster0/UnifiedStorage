package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

public class StorageGUIScrollWheel extends CustomScrollWheel {


    private GridControllerMenu menu;
    public StorageGUIScrollWheel(int x, int y, int maxY, int pages, GridControllerMenu menu) {
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
