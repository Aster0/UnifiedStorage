package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import net.minecraft.network.chat.Component;


public class StorageNetworkCardItem extends NetworkItem {


    public StorageNetworkCardItem(Properties properties) {
        super(properties, "Storages", Component.translatable("lore.unifiedstorage.network_card"),
                Integer.MAX_VALUE, NetworkBlockType.STORAGE, UpgradeType.NETWORK);


    }


    @Override
    public void onNetworkBlockInteract() {

    }
}
