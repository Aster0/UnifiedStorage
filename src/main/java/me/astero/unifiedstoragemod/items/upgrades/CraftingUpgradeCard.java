package me.astero.unifiedstoragemod.items.upgrades;


import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;


public class CraftingUpgradeCard extends UpgradeCardItem {


    public CraftingUpgradeCard(Properties properties) {
        super(properties, UpgradeType.UPGRADE, Component.translatable("lore."
                + ModUtils.MODID + ".crafting_upgrade"));


    }


}
