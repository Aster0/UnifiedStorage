package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import net.minecraft.network.chat.Component;


public class StorageNetworkCard extends NetworkItem {


    public StorageNetworkCard(Properties properties) {
        super(properties, "Storages", Component.translatable("lore.unifiedstorage.network_card"),
                Integer.MAX_VALUE, NetworkBlockType.STORAGE, UpgradeType.NETWORK);


    }


    @Override
    public void onNetworkBlockInteract() {

    }
}
