package me.astero.unifiedstoragemod.items;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.*;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerItemMenu;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.NetworkContext;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class WirelessStorage extends NetworkItem implements MenuProvider{


    private CustomBlockPosData storageLocation;


    public WirelessStorage(Properties properties) {
        super(properties,
                Component.translatable("lore.unifiedstorage.wireless_storage"), 1,
                NetworkBlockType.CONTROLLER, UpgradeType.NONE);
    }



    @Override
    public boolean onNetworkBlockInteract(BlockEntity blockEntity, ItemStack itemStack, Player player) {

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player, ItemStack itemStack) {


        if(savedStorageData.size() > 0) {

            BlockEntity blockEntity = player.level()
                    .getBlockEntity(savedStorageData.get(0).getCustomBlockPosData().getBlockPos());

            if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {


                if(!storageControllerEntity.isUpgradeModuleInserted(UpgradeModule.WIRELESS)) {
                    sendClientMessage(player, "language." + ModUtils.MODID +
                            ".no_wireless_upgrade");

                    return null;
                }


                if(player instanceof ServerPlayer serverPlayer) {


                    serverPlayer.openMenu(new SimpleMenuProvider((pContainerId, pInventory, player1) ->
                            storageControllerEntity.buildMenu(pContainerId, pInventory, player1),
                            Component.translatable("container." + ModUtils.MODID +
                                    ".storage_controller_menu_title_wireless")), (packet)
                            -> packet.writeBlockPos(blockEntity.getBlockPos()));


                    return InteractionResultHolder.success(itemStack);
                }

            }

        }


        sendClientMessage(player, "language." + ModUtils.MODID +
                ".wireless_storage_not_linked");


        return null; // fail
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("a");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory playerInventory, Player player) {

        BlockEntity blockEntity = player.level().getBlockEntity(this.storageLocation.getBlockPos());

        if(blockEntity instanceof StorageControllerEntity entity) {
            return new StorageControllerItemMenu(pContainerId, playerInventory,
                    entity);
        }

        return null;

    }
}
