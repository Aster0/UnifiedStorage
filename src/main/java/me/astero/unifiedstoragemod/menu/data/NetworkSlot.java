package me.astero.unifiedstoragemod.menu.data;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NetworkSlot extends SlotItemHandler {


    public NetworkSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
}
