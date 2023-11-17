package me.astero.mechanicaldrawersmod.registry.menu.data;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ViewOnlySlot extends Slot {

    private static final Container EMPTY_INVENTORY = new SimpleContainer(1);
    public ViewOnlySlot(int xPos, int yPos) {
        super(EMPTY_INVENTORY, 0, xPos, yPos);
    }


    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void set(ItemStack p_40240_) {

    }

    @Override
    public boolean mayPickup(Player p_40228_) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack p_40231_) {
        return false;
    }

    @Override
    public final int getMaxStackSize() {
        return 0;
    }
}
