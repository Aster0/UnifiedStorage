package me.astero.unifiedstoragemod.blocks.entity;

import me.astero.unifiedstoragemod.blocks.entity.handler.NetworkCardItemStackHandler;
import me.astero.unifiedstoragemod.blocks.entity.handler.UpgradeCardItemStackHandler;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.StorageNetworkCard;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeModule;
import me.astero.unifiedstoragemod.items.data.UpgradeType;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerBlockMenu;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.UpdateStorageDisabledEntityPacket;
import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.utils.AsteroLogger;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StorageControllerEntity extends BaseBlockEntity implements MenuProvider {




    public static final int MAX_UPGRADES = 3;
    private int maxChests = 100;
    private static final Component MENU_TITLE = Component.translatable("container."
            + ModUtils.MODID + ".storage_controller_menu_title");

    public List<String> chestLocations = new ArrayList<>();;

    public List<ItemIdentifier> mergedStorageContents = new ArrayList<>();
    private List<SavedStorageData> editedChestLocations = new ArrayList<>();

    public StorageControllerMenu menu;

    private boolean craftingEnabled = false;


    public void setCraftingEnabled(boolean craftingEnabled) {
        this.craftingEnabled = craftingEnabled;
    }

    public boolean isCraftingEnabled() {
        return craftingEnabled;
    }


    private ItemStackHandler networkInventory =
            new NetworkCardItemStackHandler<>(this) {

                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    setChanged();
                }
            };

    private ItemStackHandler visualItemInventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);

            setChanged();
        }
    };

    private ItemStackHandler upgradeInventory = new UpgradeCardItemStackHandler(MAX_UPGRADES, this);

    private ItemStackHandler craftingInventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);

            setChanged();
        }
    };

    private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.networkInventory);

    private final LazyOptional<ItemStackHandler> optionalVisualItem = LazyOptional.of(() -> this.visualItemInventory);

    private final LazyOptional<ItemStackHandler> optionalUpgradeItem = LazyOptional.of(() -> this.upgradeInventory);
    private final LazyOptional<ItemStackHandler> optionalCraftingInventory = LazyOptional.of(() -> this.craftingInventory);
    public StorageControllerEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.STORAGE_CONTROLLER_BLOCK_ENTITY.get(), pos, state);

    }



    public List<SavedStorageData> getEditedChestLocations() {
        return editedChestLocations;
    }





    public BlockEntity getStorageBlockAt(BlockPos blockPos) {

        BlockEntity blockEntity = level.getBlockEntity(blockPos);


        return blockEntity
                .getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent() ? blockEntity : null;
    }



    public void setMergedStorageContents(List<ItemIdentifier> mergedStorageContents) {
        this.mergedStorageContents = mergedStorageContents;
    }

    public int getTotalItems() {
        return mergedStorageContents.size();
    }

    public ItemIdentifier getMergedStorageContents(int index) {

        ItemIdentifier itemIdentifier;


        try {
            itemIdentifier =  mergedStorageContents.get(index);



        }
        catch (IndexOutOfBoundsException e) {
            itemIdentifier = new ItemIdentifier(ItemStack.EMPTY, 1);

        }

        return itemIdentifier;
    }

    public void addChestLocations(String location) {


        if(!chestLocations.contains(location)) {

            addChests(location);
            this.setChanged();



        }



    }

    public ItemIdentifier getMergedStorageContents(int index, List<ItemIdentifier> searchedItemList, boolean useSearch) {


        List<ItemIdentifier> itemIdentifiers = mergedStorageContents;



        if(useSearch) {
            itemIdentifiers = searchedItemList;

        }

        ItemIdentifier itemIdentifier;
        try {
            itemIdentifier =  itemIdentifiers.get(index);




        }
        catch (IndexOutOfBoundsException e) {
            itemIdentifier = new ItemIdentifier(ItemStack.EMPTY, 1);


        }



        return itemIdentifier;
    }
    private void addChests(String location) {
        chestLocations.add(location);
        CustomBlockPosData customBlockPosData = ModUtils.convertStringToBlockData(location.split(", "));
        editedChestLocations.add(new SavedStorageData(customBlockPosData));
    }

    private void loadEditedChests(CompoundTag nbt) {

        editedChestLocations.clear();

        for(int i = 0; i < maxChests; i++) {



            String rawPos = nbt.getString("chest" + i);




            if(rawPos != null && rawPos.length() > 0) {
                String[] pos = rawPos.split(", ");

                if(chestLocations.contains(rawPos)) continue;

                addChests(rawPos);



                //loadStorageContents(customBlockPosData);


            }




        }



    }


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);


        CompoundTag modNbt = nbt.getCompound(ModUtils.MODID);


        this.getNetworkInventory().deserializeNBT(modNbt.getCompound("network_card"));
        this.getVisualItemInventory().deserializeNBT(modNbt.getCompound("visual_item"));
        this.getUpgradeInventory().deserializeNBT(modNbt.getCompound("upgrade_inventory"));
        this.getCraftingInventory().deserializeNBT(modNbt.getCompound("crafting_inventory"));


        //loadEditedChests(modNbt);



    }




    private List<SavedStorageData> queueToRemoveChest = new ArrayList<>();
    private void loadStorageContents(SavedStorageData chestData) {


        BlockEntity blockEntity = this.level.getBlockEntity(chestData.getCustomBlockPosData().getBlockPos());


        if(blockEntity == null)  { // if the storage block is deleted, it will be null.


            queueToRemoveChest.add(chestData);

            return;
        }



        IItemHandler chestInventory = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElse(new ItemStackHandler(0));


        for(int i = 0; i < chestInventory.getSlots(); i++) {

            ItemStack chestItemStack = chestInventory.getStackInSlot(i);


            if(chestItemStack.getItem() != Items.AIR) {
                ItemStack itemStack = chestItemStack.copy();
                itemStack.setCount(1);


                ItemIdentifier itemIdentifier = new ItemIdentifier(itemStack, chestItemStack.getCount());



                if(mergedStorageContents.contains(itemIdentifier)) { // already exists, we can merge


                    ItemIdentifier existingItemIdentifier =
                            mergedStorageContents.get(mergedStorageContents.indexOf(itemIdentifier));


                    existingItemIdentifier.addCount(chestItemStack.getCount());

                    itemIdentifier = existingItemIdentifier;

                }
                else {

                    mergedStorageContents.add(itemIdentifier);

                }

                // mark down where we found these items


                String blockName = ModUtils.capitalizeName(blockEntity.getBlockState().getBlock().asItem().toString());




                itemIdentifier.getLocations().putIfAbsent(blockName, 0);

                int oldValue = itemIdentifier.getLocations().get(blockName);
                itemIdentifier.getLocations().put(blockName, oldValue + chestItemStack.getCount());






            }

        }








    }


    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);



        CompoundTag modNbt = new CompoundTag();
        modNbt.put("network_card", this.getNetworkInventory().serializeNBT());
        modNbt.put("visual_item", this.getVisualItemInventory().serializeNBT());
        modNbt.put("upgrade_inventory", this.getUpgradeInventory().serializeNBT());
        modNbt.put("crafting_inventory", this.getCraftingInventory().serializeNBT());

        for(int i = 0; i < chestLocations.size(); i++) {
            modNbt.putString("chest" + i, this.chestLocations.get(i));

        }



        nbt.put(ModUtils.MODID, modNbt);
    }







    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {



        return cap == ForgeCapabilities.ITEM_HANDLER ? this.optional.cast()
                : super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
        this.optionalVisualItem.invalidate();
        this.optionalUpgradeItem.invalidate();
        this.optionalCraftingInventory.invalidate();

    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public ItemStackHandler getNetworkInventory() {
        return networkInventory;
    }

    public ItemStackHandler getVisualItemInventory() {
        return visualItemInventory;
    }

    public ItemStackHandler getUpgradeInventory() {
        return upgradeInventory;
    }

    public boolean isUpgradeModuleInserted(UpgradeModule module) {

        for(int i = 0; i < getUpgradeInventory().getSlots(); i++) {
            ItemStack stack = getUpgradeInventory().getStackInSlot(i);

            if(stack.getItem().equals(module.getItem())) {
                return true;
            }

        }

        return false;
    }


    public ItemStackHandler getCraftingInventory() {
        return craftingInventory;
    }

    public LazyOptional<ItemStackHandler> getOptional() {
        return optional;
    }


    @Override
    public Component getDisplayName() {
        return MENU_TITLE;
    }

    @Override
    public void updateNetworkCardItems(ItemStack itemStack, Player player) {




        if(itemStack.getItem() instanceof NetworkItem networkItem) {

            if(networkItem.getUpgradeType() != UpgradeType.NETWORK)
                return;

            if(level.isClientSide())
                return;

            mergedStorageContents.clear();
            queueToRemoveChest.clear();



            networkItem.loadNbt(itemStack);


            editedChestLocations = new ArrayList<>(networkItem.getStorageLocations());



            updateStorageContents(player, networkItem, itemStack);




        }

        if(player.containerMenu instanceof StorageControllerMenu menu) {
            menu.createBlockEntityInventory();
        }
    }

    @Override
    public void actionWhenNetworkTakenOut(Player player) {
        mergedStorageContents.clear();
        queueToRemoveChest.clear();

        if(player.containerMenu instanceof StorageControllerMenu menu) {
            menu.createBlockEntityInventory();
        }
    }
    private void updateMergedStorageClient(Player player) {
        if(player instanceof ServerPlayer serverPlayer) {

            ModNetwork.sendToClient(new MergedStorageLocationEntityPacket(mergedStorageContents,
                    this.getBlockPos()), serverPlayer);


        }
    }

    private void updateStorageContents(Player player, NetworkItem networkItem, ItemStack itemStack) {


        if(editedChestLocations == null)
            return;

        for(SavedStorageData customBlockPosData : editedChestLocations) {


            loadStorageContents(customBlockPosData);

        }

        updateMergedStorageClient(player);



        for(SavedStorageData customBlockPosData : queueToRemoveChest) {


            networkItem.getStorageLocations().remove(customBlockPosData);
            networkItem.saveNbt(itemStack);


            chestLocations.remove("x=" + customBlockPosData.getCustomBlockPosData().getBlockPos().getX() + ", y=" +
                    customBlockPosData.getCustomBlockPosData().getBlockPos().getY()
                    + ", z=" + customBlockPosData.getCustomBlockPosData().getBlockPos().getZ());

            editedChestLocations.remove(customBlockPosData);

            AsteroLogger.info("Storage Block detected missing: " + customBlockPosData
                    + " - Removed automatically from data");
        }

    }

    public StorageControllerMenu buildMenu(int pControllerId, Inventory pInventory, Player player) {

        IItemHandler iItemHandler = getNetworkInventory();

        ItemStack itemStack = iItemHandler.getStackInSlot(0);


        disabled = itemStack.equals(ItemStack.EMPTY, false);


        if(!disabled)
            updateNetworkCardItems(itemStack, player);


        if(player instanceof ServerPlayer serverPlayer) {

            ModNetwork.sendToClient(new UpdateStorageDisabledEntityPacket(disabled, this.getBlockPos()), serverPlayer);
        }


        StorageControllerBlockMenu storageControllerMenu = new
                StorageControllerBlockMenu(pControllerId, pInventory, this);

        return storageControllerMenu;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pControllerId, Inventory pInventory, Player player) {

        return buildMenu(pControllerId, pInventory, player);
    }



}
