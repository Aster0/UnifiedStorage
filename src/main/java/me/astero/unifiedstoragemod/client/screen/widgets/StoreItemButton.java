package me.astero.unifiedstoragemod.client.screen.widgets;

import me.astero.unifiedstoragemod.client.screen.storage.StorageControllerScreen;
import me.astero.unifiedstoragemod.client.screen.widgets.generic.CustomButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class StoreItemButton<T extends AbstractContainerScreen<? extends AbstractContainerMenu>> extends CustomButton {


    private T screen;

    public StoreItemButton(int x, int y, int width, int height, WidgetSprites sprites, MutableComponent component, T screen) {


        super(x, y, width, height, sprites, component, (button) -> {

            if(screen instanceof StorageControllerScreen storageScreen) {

                storageScreen.clearCraftingGrid();
            }
        });

        this.screen = screen;
    }






}
