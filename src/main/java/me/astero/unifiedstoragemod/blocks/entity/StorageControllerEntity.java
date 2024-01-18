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
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class StorageControllerEntity extends BaseBlockEntity implements MenuProvider {




    public static final int MAX_UPGRADES = 3;
    private int maxChests = 100;
    private static final Component MENU_TITLE = Component.translatable("container."
            + ModUtils.MODID + ".storage_controller_menu_title");

    private Queue<Player> menuLoadQueue = new LinkedList<>();

    public List<ItemIdentifier> mergedStorageContents = new ArrayList<>();
    private List<SavedStorageData> editedChestLocations = new ArrayList<>();

    private final List<BlockEntity> cachedStorages = new ArrayList<>();

    public List<BlockEntity> getCachedStorages() {
        return cachedStorages;
    }

    public StorageControllerMenu menu;

    private boolean craftingEnabled = false, dimensionalEnabled = false;


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
        modNbt.put("network_card", this.getNetworkInventory().serializeNBT());
        modNbt.put("visual_item", this.getVisualItemInventory().serializeNBT());
        modNbt.put("upgrade_inventory", this.getUpgradeInventory().serializeNBT());
        modNbt.put("crafting_inventory", this.getCraftingInventory().serializeNBT());





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


            this.setDimensionalEnabled(this.isUpgradeModuleInserted(UpgradeModule.DIMENSIONAL));


            networkItem.loadNbt(itemStack, player);


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
                    this.getBlockPos(), true, player.getUUID(), true), serverPlayer);


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

        updateMergedStorageClient(player);



        for(SavedStorageData customBlockPosData : queueToRemoveChest) {


            networkItem.getStorageLocations().remove(customBlockPosData);
            networkItem.saveNbt(itemStack);



            editedChestLocations.remove(customBlockPosData);

            AsteroLogger.info("Storage Block detected missing: " + customBlockPosData
                    + " - Removed automatically from data");
        }



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
