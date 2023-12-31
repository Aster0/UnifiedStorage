package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.client.screen.widgets.generic.CustomScrollWheel;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;

public class StorageGUIScrollWheel extends CustomScrollWheel {


    private StorageControllerMenu menu;
    public StorageGUIScrollWheel(int x, int y, int maxY, int pages, StorageControllerMenu menu) {
        super(x, y, maxY, pages, menu);

        this.menu = menu;
    }

    @Override
    public void onDragDown() {
        if(currentPage >= super.getPages() || super.isDisabled())
            return;

        super.currentPage = menu.nextPage();


    }

    @Override
    public void onDragUp() {
        super.currentPage = menu.previousPage();
    }



}
