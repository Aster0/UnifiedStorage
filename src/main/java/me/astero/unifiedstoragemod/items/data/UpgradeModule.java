package me.astero.unifiedstoragemod.items.data;

import me.astero.unifiedstoragemod.registry.ItemRegistry;
import net.minecraft.world.item.Item;

public enum UpgradeModule {

    CRAFTING(ItemRegistry.CRAFTING_UPGRADE_CARD.get()),
    WIRELESS(ItemRegistry.WIRELESS_UPGRADE_CARD.get()),
    DIMENSIONAL(ItemRegistry.DIMENSIONAL_UPGRADE_CARD.get()),
    POWER(null);


    private Item item;
    UpgradeModule(Item item) {
        this.item = item;

    }

    public Item getItem() {
        return item;
    }
}
