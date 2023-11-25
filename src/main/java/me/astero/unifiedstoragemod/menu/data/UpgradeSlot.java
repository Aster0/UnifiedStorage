package me.astero.unifiedstoragemod.menu.data;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class UpgradeSlot extends SlotItemHandler {


    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
}
