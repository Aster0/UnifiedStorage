package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;


public class CloningNetworkCard extends NetworkItem {


    public CloningNetworkCard(Properties properties) {
        super(properties, Component.translatable("lore.unifiedstorage.cloning_network_card"),
                1, NetworkBlockType.CONTROLLER, UpgradeType.NETWORK);


    }


    @Override
    public void onNetworkBlockInteract(BlockEntity blockEntity) {

//        System.out.println(blockEntity.getLevel().dimension());
//        blockEntity.getLevel().getServer().getAllLevels();

    }

    @Override
    public InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player, ItemStack itemStack) {
        return null;
    }
}
