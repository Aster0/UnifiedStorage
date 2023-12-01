package me.astero.unifiedstoragemod.items;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;


public class StorageWingsItem extends NetworkItem {

    public StorageWingsItem(Properties properties) {
        super(properties, "Storage Controller",
                Component.translatable("lore.unifiedstorage.network_card"), 1,
                NetworkBlockType.CONTROLLER);
    }


    @Override
    public void onNetworkBlockInteract() {

    }


}
