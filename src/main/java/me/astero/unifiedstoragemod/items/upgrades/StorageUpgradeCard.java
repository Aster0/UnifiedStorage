package me.astero.unifiedstoragemod.items.upgrades;

import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;

public class StorageUpgradeCard extends UpgradeCardItem {

    public StorageUpgradeCard(Properties properties, String name) {
        super(properties, UpgradeType.UPGRADE, Component.translatable("lore." + ModUtils.MODID + "." + name));


    }

}
