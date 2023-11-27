package me.astero.unifiedstoragemod.blocks.entity;

import me.astero.unifiedstoragemod.blocks.entity.handler.NetworkCardItemStackHandler;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.NetworkCardItem;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.registry.ItemRegistry;
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

public class DrawerGridControllerEntity extends BaseBlockEntity implements MenuProvider {




    private int maxChests = 100;
    private static final Component MENU_TITLE = Component.translatable("container."
            + ModUtils.MODID + ".grid_controller_menu_title");

    public List<String> chestLocations = new ArrayList<>();;

    public List<ItemIdentifier> mergedStorageContents = new ArrayList<>();
    private List<SavedStorageData> editedChestLocations = new ArrayList<>();


    private ItemStackHandler networkInventory =
            new NetworkCardItemStackHandler<>(this) {

                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);
                    setChanged();
                }
            };

    private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.networkInventory);

    public DrawerGridControllerEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DRAWER_CONTROLLER_BLOCK_ENTITY.get(), pos, state);

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
            System.out.println("not exist");
            addChests(location);
            this.setChanged();



        }

        System.out.println("added " + chestLocations.size());

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


        System.out.println(nbt);
    }


    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        CompoundTag modNbt = nbt.getCompound(ModUtils.MODID);


        this.getNetworkInventory().deserializeNBT(modNbt.getCompound("network_card"));




        loadEditedChests(modNbt);










    }




    private List<SavedStorageData> queueToRemoveChest = new ArrayList<>();
    private void loadStorageContents(SavedStorageData chestData) {

        System.out.println(level.isClientSide() + " Generating " + chestData);

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

                }
                else {
                    mergedStorageContents.add(itemIdentifier);

                }

            }

        }







    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);




        CompoundTag modNbt = new CompoundTag();
        modNbt.put("network_card", this.getNetworkInventory().serializeNBT());

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

    public LazyOptional<ItemStackHandler> getOptional() {
        return optional;
    }


    @Override
    public Component getDisplayName() {
        return MENU_TITLE;
    }

    @Override
    public void updateNetworkCardItems(ItemStack itemStack, Player player) {




        if(itemStack.getItem() instanceof NetworkCardItem networkCardItem) {

            if(level.isClientSide())
                return;

            mergedStorageContents.clear();
            queueToRemoveChest.clear();

            networkCardItem.loadNbt(itemStack);
            System.out.println(networkCardItem.getStorageLocations(itemStack));

            editedChestLocations = networkCardItem.getStorageLocations(itemStack);

            updateStorageContents(player);




        }

        if(player.containerMenu instanceof GridControllerMenu menu) {
            menu.createBlockEntityInventory();
        }
    }

    @Override
    public void actionWhenNetworkTakenOut(Player player) {
        mergedStorageContents.clear();
        queueToRemoveChest.clear();

        if(player.containerMenu instanceof GridControllerMenu menu) {
            menu.createBlockEntityInventory();
        }
    }
    private void updateMergedStorageClient(Player player) {
        if(player instanceof ServerPlayer serverPlayer) {


            ModNetwork.sendToClient(new MergedStorageLocationEntityPacket(mergedStorageContents,
                    this.getBlockPos()), serverPlayer);


        }
    }

    private void updateStorageContents(Player player) {


        for(SavedStorageData customBlockPosData : editedChestLocations) {


            loadStorageContents(customBlockPosData);

        }

        updateMergedStorageClient(player);



        for(SavedStorageData customBlockPosData : queueToRemoveChest) {
            chestLocations.remove("x=" + customBlockPosData.getCustomBlockPosData().getBlockPos().getX() + ", y=" +
                    customBlockPosData.getCustomBlockPosData().getBlockPos().getY()
                    + ", z=" + customBlockPosData.getCustomBlockPosData().getBlockPos().getZ());

            editedChestLocations.remove(customBlockPosData);

            AsteroLogger.info("Storage Block detected missing: " + customBlockPosData
                    + " - Removed automatically from data");
        }

    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pControllerId, Inventory pInventory, Player player) {






        IItemHandler iItemHandler = getNetworkInventory();

        ItemStack itemStack = iItemHandler.getStackInSlot(0);

        updateNetworkCardItems(itemStack, player);



        GridControllerMenu gridControllerMenu = new GridControllerMenu(pControllerId, pInventory, this);

        this.menu = gridControllerMenu;

        return gridControllerMenu;
    }

    public GridControllerMenu menu;
}
