package me.astero.unifiedstoragemod.menu.data;

import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class NetworkSlot extends SlotItemHandler {


    public NetworkSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {

        if(stack.getItem() instanceof NetworkItem networkItem) {
            return networkItem.getUpgradeType() == UpgradeType.NETWORK;
        }

        return false;
    }

}
