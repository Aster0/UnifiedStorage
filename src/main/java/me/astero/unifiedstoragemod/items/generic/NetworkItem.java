package me.astero.unifiedstoragemod.items.generic;


import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.*;
import me.astero.unifiedstoragemod.items.generic.BaseItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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


// any items that can connect to a network should extend this.
public abstract class NetworkItem extends BaseItem {


    private String linkedBlock;

    private NetworkBlockType networkBlockType;

    private int maxLinkedLimit;
    public NetworkItem(Properties properties, MutableComponent shiftText,
                       int maxLinkedLimit, NetworkBlockType networkBlockType, UpgradeType upgradeType) {
        super(properties, upgradeType, shiftText);


        this.linkedBlock = networkBlockType.getBlockName();
        this.maxLinkedLimit = maxLinkedLimit;
        this.networkBlockType = networkBlockType;
    }





    protected List<SavedStorageData> storageLocations = new ArrayList<>();


    public List<SavedStorageData> getStorageLocations() {
        return storageLocations;
    }

    public void saveNbt(ItemStack itemStack) {


        CompoundTag nbt = itemStack.getTag();

        if(nbt == null)
            nbt = new CompoundTag();


        CompoundTag innerNbt = new CompoundTag();








        for(int i = 0; i < storageLocations.size(); i++) {
            innerNbt.putString("storage" + i, storageLocations.get(i)
                    .getCustomBlockPosData().toString());


        }

        nbt.put(ModUtils.MODID, innerNbt);


        itemStack.setTag(nbt);

    }

    public void loadNbt(ItemStack itemStack) {


        CompoundTag nbt = itemStack.getTag();

        storageLocations.clear();


        if(nbt == null) {
            return;
        }



        nbt = nbt.getCompound(ModUtils.MODID);




        for(int i = 0; i < UpgradeTier.MAX.getAmount(); i++) {



            String rawPos = nbt.getString("storage" + i);


            if(rawPos.length() > 0) {

                rawPos = ModUtils.serializeBlockPosNbt(rawPos);

                CustomBlockPosData customBlockPosData =
                        ModUtils.convertStringToBlockData(rawPos.split(", "));

                SavedStorageData savedStorageData = new SavedStorageData(customBlockPosData);


                saveToStorage(savedStorageData, null);

                continue;
            }

            return;

        }



    }



    protected boolean addStorageData(String blockCoordinate, ItemStack itemStack, Player player) {



        SavedStorageData savedStorageData = new SavedStorageData(
                ModUtils.convertStringToBlockData(blockCoordinate.split(", ")));


        boolean added = false;



        if(saveToStorage(savedStorageData, player)) {

            added = true;


        }
        else {



            removeFromStorage(savedStorageData, player);

        }

        saveNbt(itemStack);



        return added;

    }


    protected boolean saveToStorage(SavedStorageData savedStorageData, Player player) {




        if(!storageLocations.contains(savedStorageData)) {


            if(storageLocations.size() >= maxLinkedLimit) { // cannot save but can unlink it (else)

                sendClientMessage(player, "language." + ModUtils.MODID +
                        ".unable_to_link_block");


                return true;
            }



            storageLocations.add(savedStorageData);


            sendClientMessage(player, "language."
                    + ModUtils.MODID + ".linked_storage");


            return true;
        }



        return false;

    }

    private void sendClientMessage(Player player, String message) {
        if(player != null)
            player.displayClientMessage(Component.translatable(message), true);
    }

    private void removeFromStorage(SavedStorageData savedStorageData, Player player) {


//        System.out.println("Remove??? ");
//        List<SavedStorageData> savedStorageDataList = storageLocations.get(getKey(itemStack));
//
//
//        savedStorageDataList.removeIf((value) ->
//                value.getCustomBlockPosData()
//                        .equals(savedStorageData.getCustomBlockPosData()));

        storageLocations.remove(savedStorageData);


        sendClientMessage(player, "language."
                + ModUtils.MODID + ".unlinked_storage");



    }




    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);



        loadNbt(stack);


        String lore = Component.translatable("lore." + ModUtils.MODID + ".network_card_storages").getString();



        int amountOfStorages = storageLocations.size();


        components.addAll(ModUtils.
                        breakComponentLine(
                                Component.literal(lore.replace("%amount%", String.valueOf(amountOfStorages))
                                        .replace("%block%", linkedBlock))));

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {




        if(!level.isClientSide) {



            ItemStack itemStack = player.getItemInHand(interactionHand);
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);






            BlockEntity hitBlockEntity = level.getBlockEntity(blockHitResult.getBlockPos());



            if(hitBlockEntity != null) {


                boolean isBlock = networkBlockType == NetworkBlockType.STORAGE ? hitBlockEntity
                        .getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()
                        : hitBlockEntity instanceof StorageControllerEntity;


                if(isBlock) {



                    loadNbt(itemStack);



                    onNetworkBlockInteract((StorageControllerEntity) hitBlockEntity);

                    addStorageData(ModUtils.serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()),
                            itemStack, player);



                    return InteractionResultHolder.success(itemStack);

                }


            }

            loadNbt(itemStack);
            
            if(onItemUse(this.storageLocations, player, itemStack) == null)
                return super.use(level, player, interactionHand);


        }


        return super.use(level, player, interactionHand);



    }


    public abstract void onNetworkBlockInteract(StorageControllerEntity storageControllerEntity);

    public abstract InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player,
                                                                 ItemStack itemStack);
}
