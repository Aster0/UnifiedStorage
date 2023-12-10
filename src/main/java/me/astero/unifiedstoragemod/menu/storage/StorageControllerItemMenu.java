package me.astero.unifiedstoragemod.menu.storage;

import me.astero.unifiedstoragemod.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class StorageControllerItemMenu extends StorageControllerMenu {

    private static final MenuType<?> MENU_TYPE = MenuRegistry.STORAGE_CONTROLLER_ITEM_MENU.get();
    public StorageControllerItemMenu(int containerId, Inventory pInventory, FriendlyByteBuf friendlyByteBuf) {

        this(containerId, pInventory, pInventory
                .player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));





    }

    public StorageControllerItemMenu(int containerId, Inventory pInventory, BlockEntity blockEntity) {

        super(MENU_TYPE, containerId, pInventory, blockEntity);


    }

}
