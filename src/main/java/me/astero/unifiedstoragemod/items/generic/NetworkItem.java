package me.astero.unifiedstoragemod.items.generic;


import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.*;
import me.astero.unifiedstoragemod.items.generic.BaseItem;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
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
                    .getCustomBlockPosData().toString() + "; " + storageLocations.get(i).getLevel().dimension());





        }

        nbt.put(ModUtils.MODID, innerNbt);


        itemStack.setTag(nbt);

    }

    public void loadNbt(ItemStack itemStack, Player player) {


        CompoundTag nbt = itemStack.getTag();

        storageLocations.clear();


        if(nbt == null) {
            return;
        }



        nbt = nbt.getCompound(ModUtils.MODID);




        for(int i = 0; i < UpgradeTier.MAX.getAmount(); i++) {



            String rawParameter = nbt.getString("storage" + i);



            if(rawParameter.length() > 0) {

                List<String> parameters = new ArrayList<>(Arrays.asList(rawParameter.split("; ")));

                String rawPos = ModUtils.serializeBlockPosNbt(parameters.get(0));


                Level level = null;

                if(player != null) {

                    if(player instanceof ServerPlayer serverPlayer) {


                        try {
                            level = ModUtils.findLevel(parameters.get(1), serverPlayer);
                        }
                        catch(IndexOutOfBoundsException e) { // for old storage cards before inter-dimension update
                            // keeps the level at null.
                        }


                    }

                }

                CustomBlockPosData customBlockPosData =
                        ModUtils.convertStringToBlockData(rawPos.split(", "));

                SavedStorageData savedStorageData = new SavedStorageData(customBlockPosData, level);


                saveToStorage(savedStorageData, null);


                continue;
            }



            return;

        }



    }



    protected boolean addStorageData(String blockCoordinate, ItemStack itemStack, Player player) {



        SavedStorageData savedStorageData = new SavedStorageData(
                ModUtils.convertStringToBlockData(blockCoordinate.split(", ")), player.level());


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

    protected void sendClientMessage(Player player, String message) {
        if(player != null)
            player.displayClientMessage(Component.translatable(message), true);
    }

    private void removeFromStorage(SavedStorageData savedStorageData, Player player) {



        storageLocations.remove(savedStorageData);


        sendClientMessage(player, "language."
                + ModUtils.MODID + ".unlinked_storage");



    }




    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);

        loadNbt(stack, null);

        if(networkBlockType == NetworkBlockType.STORAGE_CLONE) { // dont show the lore yet
            if(storageLocations.size() == 0) {
                return;
            }
        }




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



                    loadNbt(itemStack, player);



                    if(onNetworkBlockInteract(hitBlockEntity, itemStack, player)) {

                        return InteractionResultHolder.success(itemStack);
                    }


                    addStorageData(ModUtils.serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()),
                            itemStack, player);



                    return InteractionResultHolder.success(itemStack);

                }


            }

            loadNbt(itemStack, player);

            if(onItemUse(this.storageLocations, player, itemStack) == null)
                return super.use(level, player, interactionHand);


        }


        return super.use(level, player, interactionHand);



    }


    public abstract boolean onNetworkBlockInteract(BlockEntity blockEntity, ItemStack itemStack, Player player);

    public abstract InteractionResultHolder<ItemStack> onItemUse(List<SavedStorageData> savedStorageData, Player player,
                                                                 ItemStack itemStack);
}
