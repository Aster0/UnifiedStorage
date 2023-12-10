package me.astero.unifiedstoragemod.items;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.NetworkBlockType;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;


public class StorageWings extends NetworkItem implements Equipable {

    public StorageWings(Properties properties) {
        super(properties,
                Component.translatable("lore.unifiedstorage.storage_wings"), 1,
                NetworkBlockType.CONTROLLER, UpgradeType.NONE);
    }


    @Override
    public void onNetworkBlockInteract(StorageControllerEntity storageControllerEntity) {


    }

    @Override
    public InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player, ItemStack itemStack) {
        return null;
    }

    public InteractionResultHolder<ItemStack> use(Level p_41137_, Player p_41138_, InteractionHand p_41139_) {
        return this.swapWithEquipmentSlot(this, p_41137_, p_41138_, p_41139_);
    }

    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_ELYTRA;

    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}
