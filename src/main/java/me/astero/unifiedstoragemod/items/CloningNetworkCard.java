package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;


public class CloningNetworkCard extends NetworkItem {


    public CloningNetworkCard(Properties properties) {
        super(properties, Component.translatable("lore.unifiedstorage.cloning_network_card"),
                Integer.MAX_VALUE, NetworkBlockType.STORAGE_CLONE, UpgradeType.NETWORK);


    }


    @Override
    public boolean onNetworkBlockInteract(BlockEntity blockEntity, ItemStack itemStack, Player player) {

//        System.out.println(blockEntity.getLevel().dimension());
//        blockEntity.getLevel().getServer().getAllLevels();

        if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {

            ItemStackHandler networkInventory = storageControllerEntity.getNetworkInventory();

            ItemStack networkCard = networkInventory.getStackInSlot(0);

            String playerMessage = "language." + ModUtils.MODID + ".clone_not_success";

            if(!networkCard.equals(ItemStack.EMPTY, false)) { // if there's a network card in the storage controller
                // clone network card

                itemStack.setTag(networkCard.getTag());

                playerMessage = "language." + ModUtils.MODID + ".clone_success";

            }


            sendClientMessage(player, playerMessage);

        }


        return true;

    }

    @Override
    public InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player, ItemStack itemStack) {
        return null;
    }
}
