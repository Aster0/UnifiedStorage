package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;


public class BlankUpgradeCard extends UpgradeCardItem {


    public BlankUpgradeCard(Properties properties) {
        super(properties, UpgradeType.UPGRADE, Component.translatable("lore." + ModUtils.MODID + ".blank_upgrade"));


    }


}
