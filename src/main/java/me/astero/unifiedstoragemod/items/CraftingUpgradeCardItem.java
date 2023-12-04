package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;


public class CraftingUpgradeCardItem extends UpgradeCardItem {


    public CraftingUpgradeCardItem(Properties properties) {
        super(properties, UpgradeType.UPGRADE, Component.translatable("lore."
                + ModUtils.MODID + ".crafting_upgrade"));


    }


}
