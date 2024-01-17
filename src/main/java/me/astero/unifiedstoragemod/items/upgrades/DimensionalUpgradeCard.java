package me.astero.unifiedstoragemod.items.upgrades;

import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.NetworkCardInsertedEntityPacket;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DimensionalUpgradeCard extends UpgradeCardItem implements IBlockUpdater {

    public DimensionalUpgradeCard(Item.Properties properties) {
        super(properties, UpgradeType.UPGRADE, Component.translatable("lore." + ModUtils.MODID + ".dimensional_upgrade"));


    }

    @Override
    public void update(BlockEntity blockEntity) {

        ModNetwork.sendToServer(new
                NetworkCardInsertedEntityPacket(blockEntity.getBlockPos()));

    }
}
