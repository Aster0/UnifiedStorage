package me.astero.unifiedstoragemod.menu.data;

import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class UpgradeSlot extends SlotItemHandler {


    public UpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }


    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof UpgradeCardItem;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
