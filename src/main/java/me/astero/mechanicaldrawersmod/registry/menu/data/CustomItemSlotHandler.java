package me.astero.mechanicaldrawersmod.registry.menu.data;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CustomItemSlotHandler extends SlotItemHandler {

    public CustomItemSlotHandler(int index, int xPosition, int yPosition) {
        super(new ItemStackHandler(27), index, xPosition, yPosition);


    }




}
