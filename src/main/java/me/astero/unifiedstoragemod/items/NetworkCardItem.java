package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeTier;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class NetworkCardItem extends NetworkItem {


    public NetworkCardItem(Properties properties) {
        super(properties, "Storages", Component.translatable("lore.unifiedstorage.network_card"),
                Integer.MAX_VALUE, NetworkBlockType.STORAGE);


    }


    @Override
    public void onNetworkBlockInteract() {

    }
}
