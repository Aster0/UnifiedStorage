package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.menu.data.StorageSearchData;
import me.astero.unifiedstoragemod.menu.enums.InventoryAction;
import me.astero.unifiedstoragemod.menu.interfaces.IMenuInteractor;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.registry.MenuRegistry;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.menu.data.ViewOnlySlot;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class GridControllerMenu extends AbstractContainerMenu implements IMenuInteractor {

    public static final int VISIBLE_CONTENT_HEIGHT = 27, STARTING_SLOT_INDEX = 36;


    private boolean finishedAdding = false;

    private StorageSearchData storageSearchData = new StorageSearchData();

    private int scrollPage;

    private final DrawerGridControllerEntity drawerGridControllerEntity;
    private final ContainerLevelAccess containerLevelAccess;

    private Inventory pInventory;

    public int lastClickedSlot;

    public GridControllerMenu(int containerId, Inventory pInventory, FriendlyByteBuf friendlyByteBuf) {

        this(containerId, pInventory, pInventory
                .player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));



    }

    public GridControllerMenu(int containerId, Inventory pInventory, BlockEntity blockEntity) {
        super(MenuRegistry.GRID_CONTROLLER_MENU.get(), containerId);


        if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {
            this.drawerGridControllerEntity = drawerGridControllerEntity;

        }
        else {
            throw new IllegalStateException("Incorrect block entity class, please check again. (%s)"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.containerLevelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        createInventory(pInventory, drawerGridControllerEntity);

        this.pInventory = pInventory;

    }


    public DrawerGridControllerEntity getDrawerGridControllerEntity() {
        return drawerGridControllerEntity;
    }

    public void onStorageSearchStop() {
        storageSearchData.setSearching(false);
        generateStartPage();

    }

    public int getTotalPages() {



        double value = (double) drawerGridControllerEntity.mergedStorageContents.size() / VISIBLE_CONTENT_HEIGHT;


        return (int) Math.ceil(value);
    }

    public void onStorageSearch(String entry) {


        storageSearchData.getSearchedStorageList().clear();


        storageSearchData.setSearching(true);
        for(ItemIdentifier itemIdentifier : drawerGridControllerEntity.mergedStorageContents) {


            String itemName = itemIdentifier.getItemStack().getDisplayName().getString().toLowerCase();
            // [Compass]

            if(itemName.substring(1, itemName.length() - 1)
                    .contains(entry.toLowerCase())) {

                storageSearchData.getSearchedStorageList().add(itemIdentifier);
            }
        }

        generateStartPage();




    }

    private void createInventory(Inventory pInventory, DrawerGridControllerEntity drawerGridControllerEntity) {

        createPlayerHotbar(pInventory);
        createPlayerInventory(pInventory);
        createBlockEntityInventory(drawerGridControllerEntity);

    }



    private void createBlockEntityInventory(DrawerGridControllerEntity drawerGridControllerEntity) {

        drawerGridControllerEntity.getOptional();
        generateStartPage();

    }

    private void generateStartPage() {
        scrollPage = 1;
        generateSlots(scrollPage);
    }

    public int nextPage() {


        if(scrollPage > getTotalPages() - 1)
            return getTotalPages();

        scrollPage++;
        generateSlots(scrollPage);


        return scrollPage;
    }

    public void regenerateCurrentPage() {
        generateSlots(scrollPage);
    }

    public int previousPage() {

        if(scrollPage - 1 < 1)
            return 1;

        scrollPage--;
        generateSlots(scrollPage);

        System.out.println(scrollPage);

        return scrollPage;
    }



    public void generateSlots(int page) {




        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                int currentIndex = (row * 9) + column;



                ItemIdentifier itemIdentifier =
                        drawerGridControllerEntity.getMergedStorageContents(currentIndex,
                                storageSearchData.getSearchedStorageList(), false);



                if(page == 1 && !finishedAdding) {
                    addSlot( new ViewOnlySlot(itemIdentifier,
                            8 + (column * 18), 18 + (row * 18), currentIndex));



                    if(currentIndex == VISIBLE_CONTENT_HEIGHT - 1)
                        finishedAdding = true;
                }

                else {



                    int slotToFetch = ((page - 1) * VISIBLE_CONTENT_HEIGHT) + currentIndex;


                    int slotIndex = STARTING_SLOT_INDEX + currentIndex;



                    itemIdentifier =
                            drawerGridControllerEntity.getMergedStorageContents(slotToFetch,
                                    storageSearchData.getSearchedStorageList(), storageSearchData.isSearching());



                    if(slots.get(slotIndex) instanceof ViewOnlySlot) {

                        slots.set(slotIndex, new ViewOnlySlot(itemIdentifier,
                                8 + (column * 18), 18 + (row * 18), slotToFetch));


                    }
                }
            }
        }



    }








    private void createPlayerInventory(Inventory pInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                addSlot(new Slot(pInventory, 9 + column + (row * 9),
                        8 + (column * 18),
                        153 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory pInventory) {
        for (int column = 0; column < 9; column++) {

            addSlot(new Slot(pInventory, column, 8 + (column * 18),
                    211));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot fromSlot = getSlot(index);
        ItemStack fromStack = fromSlot.getItem();

        if(fromStack.getCount() <= 0)
            fromSlot.set(ItemStack.EMPTY);

        if(!fromSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack copyFromStack = fromStack.copy();

        if(index < 36) {
            if(!moveItemStackTo(fromStack, 36, 63, false))
                return ItemStack.EMPTY;

        } else if (index < 63) {
            if(!moveItemStackTo(fromStack, 0, 36, false))
                return ItemStack.EMPTY;

        }

        fromSlot.setChanged();
        fromSlot.onTake(player, fromStack);

        return copyFromStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.containerLevelAccess, player, BlockRegistry.DRAWER_GRID_BLOCK.get());
    }




    @Override
    public void interactWithMenu(ClickType clickType, InventoryAction action, ItemStack itemStack,
                                 Slot slot, boolean cameFromStorage) { // handles both client and server side of when the player interacts





        // gg

        if (slot != null && slot.index >= 0 && slot.index < slots.size()) {


            if(itemStack != null) {



                if(clickType == ClickType.PICKUP) {



                    ItemStack itemStackToPlace = itemStack.copy();
                    ItemStack copiedItemstack = itemStack.copy();





                    if(!getCarried().equals(ItemStack.EMPTY)) {   // means we are placing smth down





                        if(slot != null) {


                            if(!(slot instanceof ViewOnlySlot v)) { // player inventory



                                if(action == InventoryAction.PLACE_ONE_OR_SPLIT) {
                                    // to place only one
                                    getCarried().shrink(1);

                                    itemStackToPlace.grow(1);

                                }


                                itemStackToPlace = getCarried();




                            }

                        }

                    }
                    if(getCarried().equals(ItemStack.EMPTY)) {   // our hands are not carrying anything




                        if(action == InventoryAction.PLACE_ONE_OR_SPLIT) {
                            // means we want to split

                            int valueToSplit = (int) Math.ceil((double) itemStack.getCount() / 2);
                            int valueToStay = itemStack.getCount() - valueToSplit;


                            copiedItemstack.setCount(valueToSplit);
                            itemStackToPlace.setCount(valueToStay);

                        }
                        else {
                            itemStackToPlace = ItemStack.EMPTY;



                        }
                    }



                    if(!cameFromStorage) {
                        slot.set(itemStackToPlace);

                    }




                    setCarried(copiedItemstack); // we take whatever that was clicked in the slot








                }
            }

        }








    }


    @Override
    public void interactWithMenu(ItemStack itemStack, boolean take, int value) {
        System.out.println(itemStack.getCount());

        if(take) {
            setCarried(itemStack); // we take whatever that was clicked in the slot
            takeOutFromStorage(itemStack, value);
        }
        else { // place in storage

        }
    }


    public void updateStorageContents(ItemStack itemStack, int value) {

        ItemIdentifier queuedToBeRemoved = null;

        ItemIdentifier existingItemIdentifier =
                drawerGridControllerEntity.mergedStorageContents.get(
                        drawerGridControllerEntity.mergedStorageContents.indexOf(
                                new ItemIdentifier(itemStack, 1)));

        existingItemIdentifier.setCount(existingItemIdentifier.getCount() + value);

        if(existingItemIdentifier.getCount() <= 0) {
            queuedToBeRemoved = existingItemIdentifier;
        }

//        for(ItemIdentifier itemIdentifier : drawerGridControllerEntity.mergedStorageContents) {
//
//            System.out.println(itemIdentifier.getItemStack());
//            if(ItemStack.isSameItemSameTags(itemStack, itemIdentifier.getItemStack())) {
//                itemIdentifier.setCount(itemIdentifier.getCount() + value);
//
//                System.out.println(itemIdentifier.getCount() + " FOUIND");
//
//                if(itemIdentifier.getCount() <= 0) {
//                    queuedToBeRemoved = itemIdentifier;
//                }
//
//                break;
//            }
//        }

        if(queuedToBeRemoved != null) {
            drawerGridControllerEntity.mergedStorageContents.remove(queuedToBeRemoved);
        }



        regenerateCurrentPage();


    }
    private void takeOutFromStorage(ItemStack itemStack, int valueToDeduct) {

        int valueLeft = valueToDeduct;



        for(CustomBlockPosData blockPosData : drawerGridControllerEntity.getEditedChestLocations()) {

            BlockEntity storageBlockEntity = drawerGridControllerEntity.getStorageBlockAt(blockPosData.getBlockPos());

            if(storageBlockEntity != null) {


                IItemHandler chestInventory = storageBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .orElse(new ItemStackHandler(0));


                for(int i = 0; i < chestInventory.getSlots(); i++) {

                    ItemStack stackInSlot = chestInventory.getStackInSlot(i);



                    if(ItemStack.isSameItemSameTags(itemStack, stackInSlot)) {


                        int calculateValueLeft = valueLeft - stackInSlot.getCount();


                        int toMinusFromStack = stackInSlot.getCount() - valueLeft;

                        valueLeft = Math.max(calculateValueLeft, 0);

                        stackInSlot.setCount(Math.max(toMinusFromStack, 0));


                        if(valueLeft == 0) break;


                    }


                }



            }

            if(valueLeft == 0) break;



        }
    }
}
