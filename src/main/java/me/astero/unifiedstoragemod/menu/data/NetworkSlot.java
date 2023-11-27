package me.astero.unifiedstoragemod.menu.data;

import me.astero.unifiedstoragemod.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class NetworkSlot extends SlotItemHandler {


    public NetworkSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem().equals(ItemRegistry.NETWORK_CARD.get());
    }
}