package me.astero.unifiedstoragemod.menu.data;

import me.astero.unifiedstoragemod.data.ItemIdentifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ViewOnlySlot extends Slot {

    private static final Container EMPTY_INVENTORY = new SimpleContainer(0);

    private ItemIdentifier itemIdentifier;
    public ViewOnlySlot(ItemIdentifier itemIdentifier, int xPos, int yPos) {
        super(EMPTY_INVENTORY, 0, xPos, yPos);

        this.itemIdentifier = itemIdentifier;
    }

    public int getActualItemCount() {
        return itemIdentifier.getCount();
    }

    public Item getItemRepresentative() {
        return itemIdentifier.getItemStack().getItem();
    }
    public ItemStack getActualItem() {

        ItemStack stack = itemIdentifier.getItemStack().copy();
        stack.setCount(itemIdentifier.getCount());

        return stack;
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void set(ItemStack p_40240_) {

    }
    @Override
    public final ItemStack remove(int amount) {
        return ItemStack.EMPTY;
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
