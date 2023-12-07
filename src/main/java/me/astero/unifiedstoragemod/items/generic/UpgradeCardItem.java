package me.astero.unifiedstoragemod.items.generic;

import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.BaseItem;
import net.minecraft.network.chat.MutableComponent;

public class UpgradeCardItem extends BaseItem {

    public UpgradeCardItem(Properties properties, UpgradeType upgradeType, MutableComponent shiftText) {
        super(properties, upgradeType, shiftText);
    }

}
