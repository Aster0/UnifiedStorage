package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.world.ForgeChunkManager;

import java.util.List;


public class StorageNetworkCard extends NetworkItem {


    public StorageNetworkCard(Properties properties) {
        super(properties, Component.translatable("lore.unifiedstorage.network_card"),
                Integer.MAX_VALUE, NetworkBlockType.STORAGE, UpgradeType.NETWORK);


    }


    @Override
    public boolean onNetworkBlockInteract(BlockEntity blockEntity, ItemStack itemStack, Player player) {

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player, ItemStack itemStack) {

        return null;
    }
}
