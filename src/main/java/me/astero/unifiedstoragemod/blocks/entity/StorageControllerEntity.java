package me.astero.unifiedstoragemod.blocks.entity;

import me.astero.unifiedstoragemod.blocks.StorageControllerBlock;
import me.astero.unifiedstoragemod.blocks.entity.handler.NetworkCardItemStackHandler;
import me.astero.unifiedstoragemod.blocks.entity.handler.UpgradeCardItemStackHandler;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
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
import me.astero.unifiedstoragemod.utils.AsteroLogger;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class StorageControllerEntity extends BaseBlockEntity implements MenuProvider {




    public static final int MAX_UPGRADES = 3;
    private int maxChests = 100;
    private static final Component MENU_TITLE = Component.translatable("container."
            + ModUtils.MODID + ".storage_controller_menu_title");

    private Queue<Player> menuLoadQueue = new LinkedList<>();

    public List<ItemIdentifier> mergedStorageContents = new ArrayList<>();
    public List<ItemIdentifier> queueToRemoveItems = new ArrayList<>();
    private List<SavedStorageData> editedChestLocations = new ArrayList<>();

    private final List<BlockEntity> cachedStorages = new ArrayList<>();

    public List<BlockEntity> getCachedStorages() {
        return cachedStorages;
    }

    public StorageControllerMenu menu;

    private boolean craftingEnabled = false, dimensionalEnabled = false, updateClientsOnStorageChange = false;


    public void setCraftingEnabled(boolean craftingEnabled) {
        this.craftingEnabled = craftingEnabled;
    }

    public void setDimensionalEnabled(boolean dimensionalEnabled) {
        this.dimensionalEnabled = dimensionalEnabled;
    }

    public boolean isCraftingEnabled() {
        return craftingEnabled;
    }

    public boolean isDimensionalEnabled() {
        return dimensionalEnabled;
    }

    private ItemStackHandler networkInventory =
            new NetworkCardItemStackHandler<>(this);

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

    @Override
    public void setChanged() {
        super.setChanged();




        boolean isNetworkInSlot = this.getNetworkInventory().getStackInSlot(0).getCount() == 1;


        if(level != null && this.getBlockState().getValue(StorageControllerBlock.STATUS) != isNetworkInSlot) {




            if(!level.isClientSide) {
                level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(StorageControllerBlock.STATUS,
                        isNetworkInSlot));


            }

        }
    }

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

    public void setUpdateClientsOnStorageChange(boolean updateClientsOnStorageChange) {
        this.updateClientsOnStorageChange = updateClientsOnStorageChange;
    }

    public boolean isUpdateClientsOnStorageChange() {
        return updateClientsOnStorageChange;
    }


    @Override
    public void onLoad() {
        super.onLoad();



        setChanged();
    }

    public BlockEntity getStorageBlockAt(SavedStorageData chestData) {


        Level storageLevel = getStorageLevel(chestData);

        if(!isDimensionalEnabled()) {
            if(!storageLevel.equals(this.getLevel())) { // means we won't load other dimensions chest
                return null;
            }
        }

        BlockEntity blockEntity = storageLevel.getBlockEntity(chestData.getCustomBlockPosData().getBlockPos());

        if(blockEntity == null)
            return null;

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








    private Level getStorageLevel(SavedStorageData chestData) {


        return chestData.getLevel() == null ? this.getLevel() : chestData.getLevel();

    }

    @Override
    public void setLevel(Level p_155231_) {
        super.setLevel(p_155231_);


    }

    private List<SavedStorageData> queueToRemoveChest = new ArrayList<>();

    public void forceChunk(boolean force, BlockEntity blockEntity) {

        int chunkX = SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getX());
        int chunkZ = SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getZ());


        ForgeChunkManager.forceChunk((ServerLevel) blockEntity.getLevel(), ModUtils.MODID, blockEntity.getBlockPos(),
                chunkX,
                chunkZ, force, false); // for different dimensions



    }

    private void loadStorageContents(BlockEntity blockEntity) {


        forceChunk(true, blockEntity);


        cachedStorages.add(blockEntity);

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

        boolean updateStorage = isUpdateClientsOnStorageChange();
        if(isUpdateClientsOnStorageChange()) {

            setUpdateClientsOnStorageChange(false);



            modNbt.put("storage_items", serializeInventory(this.mergedStorageContents));


            modNbt.put("queued_items", serializeInventory(queueToRemoveItems));
            this.queueToRemoveItems.clear();
        }
        else {

            modNbt.put("network_card", this.getNetworkInventory().serializeNBT());
            modNbt.put("visual_item", this.getVisualItemInventory().serializeNBT());
            modNbt.put("upgrade_inventory", this.getUpgradeInventory().serializeNBT());
            modNbt.put("crafting_inventory", this.getCraftingInventory().serializeNBT());
        }

        modNbt.putBoolean("update_storage", updateStorage);


        nbt.put(ModUtils.MODID, modNbt);
    }


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);


        CompoundTag modNbt = nbt.getCompound(ModUtils.MODID);



        if(modNbt.getBoolean("update_storage") && menu != null) { // check if the player is in a menu, then load this cache.

            List<ItemIdentifier> queuedStorageList = deserializeInventory(modNbt.getList("queued_items",
                    Tag.TAG_COMPOUND), false);

            if(queuedStorageList != null) {
                this.queueToRemoveItems.addAll(queuedStorageList);
            }

            List<ItemIdentifier> newStorageList = deserializeInventory(modNbt.getList("storage_items",
                    Tag.TAG_COMPOUND), true);

            if(newStorageList != null) {
                this.mergedStorageContents = newStorageList;

                if(menu != null) {
                    if(menu.getStorageSearchData().isSearching())
                        menu.onStorageSearch(menu.getCachedSearchString(), true);
                    else
                        menu.regenerateCurrentPage();
                }

            }

            return;
        }


        this.getNetworkInventory().deserializeNBT(modNbt.getCompound("network_card"));
        this.getVisualItemInventory().deserializeNBT(modNbt.getCompound("visual_item"));
        this.getUpgradeInventory().deserializeNBT(modNbt.getCompound("upgrade_inventory"));
        this.getCraftingInventory().deserializeNBT(modNbt.getCompound("crafting_inventory"));


        if(this.getNetworkInventory() != null) {

            setDisabled(this.getNetworkInventory().getStackInSlot(0).equals(ItemStack.EMPTY, false));

            if(this.menu != null)
                menu.regenerateCurrentPage();


        }



        //loadEditedChests(modNbt);


    }

    private List<ItemIdentifier> deserializeInventory(ListTag listTag, boolean removeZeroCounts) {

        List<ItemIdentifier> newItemIdentifier = null;

        if(listTag.size() > 0) {

            newItemIdentifier = new ArrayList<>();


            for(int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                int count = tag.getInt("count");




                ItemStack itemStack = ItemStack.of(tag);


                if(!queueToRemoveItems.contains(new ItemIdentifier(itemStack, 1)) && count <= 0 && removeZeroCounts)
                    continue;

                Map<String, Integer> locationMap = new HashMap<>();

                for(String location : tag.getString("locations").split(", ")) {

                    int amount = tag.getInt("location_" + location);

                    locationMap.putIfAbsent(location, amount);


                }




                newItemIdentifier.add(new ItemIdentifier(itemStack, count, locationMap));
            }


            //modNbt.put("storage_items", tagList);


        }

        return newItemIdentifier;
    }

    private ListTag serializeInventory(List<ItemIdentifier> list) {

        ListTag nbtTagList = new ListTag();
        for(int i = 0; i < list.size(); i++) {
            ItemIdentifier itemIdentifier = list.get(i);


            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("count", itemIdentifier.getCount());


            if(itemIdentifier.getLocations() != null) {
                String locations = "";

                for(String location : itemIdentifier.getLocations().keySet()) {

                    locations += location + ", ";

                    itemTag.putInt("location_" + location, itemIdentifier.getLocations().get(location));
                }

                if(locations.length() > 0) {
                    locations.substring(0, locations.length() - 1); // remove ,
                }

                itemTag.putString("locations", locations);

            }




            itemIdentifier.getItemStack().save(itemTag);


            nbtTagList.add(itemTag);

        }


        return nbtTagList;
    }




    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {


        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == Direction.WEST || side == Direction.EAST) {
                return this.optionalUpgradeItem.cast();
            }

            return this.optional.cast();
        }


        return this.optional.cast(); // prevent memory overflow if someone ask for something other than ITEM_HANDLER
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


            this.setDimensionalEnabled(this.isUpgradeModuleInserted(UpgradeModule.DIMENSIONAL));


            networkItem.loadNbt(itemStack, player);


            editedChestLocations = new ArrayList<>(networkItem.getStorageLocations());

            updateStorageContents(player, networkItem, itemStack);



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

    private void updateStorageContents(Player player, NetworkItem networkItem, ItemStack itemStack) {


        if(editedChestLocations == null)
            return;

        getCachedStorages().clear();

        long time = System.currentTimeMillis();


        for(SavedStorageData customBlockPosData : editedChestLocations) {


            Level storageLevel = getStorageLevel(customBlockPosData);


            if(!isDimensionalEnabled()) {
                if(!storageLevel.equals(this.getLevel())) { // means we won't load other dimensions chest
                    continue;
                }
            }


            BlockEntity blockEntity = getStorageBlockAt(customBlockPosData);

            if(blockEntity == null)  { // if the storage block is deleted, it will be null.



                queueToRemoveChest.add(customBlockPosData);
                continue;

            }

            loadStorageContents(blockEntity);


        }


        AsteroLogger.info("Took " + (System.currentTimeMillis() - time) + "ms to load all chests from the Storage Controller.");

        //sendStorageUpdateToClient(level);
        updateMergedStorageClient(player);



        for(SavedStorageData customBlockPosData : queueToRemoveChest) {


            networkItem.getStorageLocations().remove(customBlockPosData);
            networkItem.saveNbt(itemStack);



            editedChestLocations.remove(customBlockPosData);

            AsteroLogger.info("Storage Block detected missing: " + customBlockPosData
                    + " - Removed automatically from data");
        }



    }

    private void updateMergedStorageClient(Player player) {
        if(player instanceof ServerPlayer serverPlayer) {

            ModNetwork.sendToClient(new MergedStorageLocationEntityPacket(mergedStorageContents,
                    this.getBlockPos(), true, player.getUUID(), true), serverPlayer);


        }
    }

    public void sendStorageUpdateToClient(Level level) {
        if(!level.isClientSide) {


            level.sendBlockUpdated(getBlockPos(),
                    getBlockState(), getBlockState(),
                    Block.UPDATE_CLIENTS);

        }

        setUpdateClientsOnStorageChange(true);
    }


    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {


        if(level.isClientSide)
            return;

        if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {


            if(storageControllerEntity.menuLoadQueue.size() > 0) {

                IItemHandler iItemHandler = storageControllerEntity.getNetworkInventory();

                ItemStack itemStack = iItemHandler.getStackInSlot(0);


                storageControllerEntity.disabled = itemStack.equals(ItemStack.EMPTY, false);

                Player player = storageControllerEntity.menuLoadQueue.remove();

                if(!storageControllerEntity.disabled)
                    storageControllerEntity.updateNetworkCardItems(itemStack, player);


                if(player instanceof ServerPlayer serverPlayer) {
                    ModNetwork.sendToClient(new UpdateStorageDisabledEntityPacket(storageControllerEntity.disabled,
                            storageControllerEntity.getBlockPos()), serverPlayer);
                }
            }
        }

    }
    public StorageControllerMenu buildMenu(int pControllerId, Inventory pInventory, Player player) {



        StorageControllerBlockMenu storageControllerMenu = new
                StorageControllerBlockMenu(pControllerId, pInventory, this);

        if(!menuLoadQueue.contains(player)) // so the player can't request twice
            menuLoadQueue.add(player); // queue to load later, we'll open the menu first.

        return storageControllerMenu;
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pControllerId, Inventory pInventory, Player player) {

        return buildMenu(pControllerId, pInventory, player);
    }



}
