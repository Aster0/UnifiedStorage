package me.astero.unifiedstoragemod.menu.data;

import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemVisualSlot extends Slot {

    private static final Container EMPTY_INVENTORY = new SimpleContainer(0);

    private int slotIndex;

    private ItemIdentifier itemIdentifier;
    public ItemVisualSlot(ItemIdentifier itemIdentifier, int xPos, int yPos, int slotIndex) {
        super(EMPTY_INVENTORY, 0, xPos, yPos);

        this.slotIndex = slotIndex;

        this.itemIdentifier = itemIdentifier;
    }

    @Override
    public int getSlotIndex() {
        return this.slotIndex;
    }

    public int getActualItemCount() {
        return itemIdentifier.getCount();
    }

    public void setActualItemCount(int value) {
        this.itemIdentifier.setCount(value);
    }


    public List<Component> getItemLocations() {

        List<Component> components = new ArrayList<>();

        components.add(Component.literal(" "));

        for(String key : itemIdentifier.getLocations().keySet()) {

            int value = itemIdentifier.getLocations().get(key);

            components.add(Component.literal(Component.translatable("container." + ModUtils.MODID
                    + ".itemTooltip").getString().replace("%amount%",
                    String.valueOf(value)).replace("%block%", key)));

        }


        return components;

    }



    public ItemStack getActualItem() {

        ItemStack stack = itemIdentifier.getItemStack();


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
