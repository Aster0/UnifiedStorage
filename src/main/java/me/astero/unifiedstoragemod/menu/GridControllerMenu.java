package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.client.screen.widgets.NetworkSlotGUI;
import me.astero.unifiedstoragemod.client.screen.widgets.UpgradeSlotGUI;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.menu.data.CustomGUISlot;
import me.astero.unifiedstoragemod.menu.data.StorageSearchData;
import me.astero.unifiedstoragemod.menu.enums.MouseAction;
import me.astero.unifiedstoragemod.menu.interfaces.IMenuInteractor;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.UpdateStorageInventoryClientEntityPacket;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.registry.MenuRegistry;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class GridControllerMenu extends Menu implements IMenuInteractor {

    public static final int VISIBLE_CONTENT_HEIGHT = 27, STARTING_SLOT_INDEX = 36;


    private boolean finishedAdding = false;

    private StorageSearchData storageSearchData = new StorageSearchData();

    private int scrollPage;

    private final DrawerGridControllerEntity drawerGridControllerEntity;
    private final ContainerLevelAccess containerLevelAccess;

    private Inventory pInventory;

    private NetworkSlotGUI<GridControllerMenu> networkSlotGUI;
    private UpgradeSlotGUI<GridControllerMenu> upgradeSlotGUI;

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


    public NetworkSlotGUI<GridControllerMenu> getNetworkSlotGUI() {
        return networkSlotGUI;
    }

    public UpgradeSlotGUI<GridControllerMenu> getUpgradeSlotGUI() {
        return upgradeSlotGUI;
    }

    public Inventory getPlayerInventory() {
        return pInventory;
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
        createUpgradeSlots();


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
                    addSlot( new CustomGUISlot(itemIdentifier,
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



                    if(slots.get(slotIndex) instanceof CustomGUISlot) {

                        slots.set(slotIndex, new CustomGUISlot(itemIdentifier,
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

    private void createUpgradeSlots() {

        networkSlotGUI = new NetworkSlotGUI<>(1,  210, 0,
                210, 0);

        networkSlotGUI.create(this);

        upgradeSlotGUI = new UpgradeSlotGUI<>(3,  210, 50,
                210, 50);

        upgradeSlotGUI.create(this);
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
    public void interactWithMenu(ItemStack itemStack, boolean take, int value, boolean quickMove, int slotIndex) {


        if(take) {




            int index = drawerGridControllerEntity.mergedStorageContents
                    .indexOf(new ItemIdentifier(itemStack, 1));

            if(index == -1) return;

            ItemIdentifier itemIdentifier = drawerGridControllerEntity.mergedStorageContents.get(
                    index);



            if(itemIdentifier.getCount() < value) {
                return; // somehow we don't have enough value to take it out of the storage
            }



            itemStack.setCount(value);

            int slot = -1;
            ItemStack stack = itemStack;


            if(quickMove) {

                int remainingSlot = pInventory.getSlotWithRemainingSpace(itemStack);
                int freeSlot = pInventory.getFreeSlot();

                if(pInventory.getFreeSlot() == -1 && remainingSlot == -1) // no free slots, dont extract.
                    return;

                slot = remainingSlot != -1 ? remainingSlot :
                        freeSlot;


                System.out.println(slot + " SLOT");
                if(remainingSlot != -1) {

                    ItemStack remainingSlotItem = pInventory.getItem(remainingSlot);
                    int toFill = remainingSlotItem.getMaxStackSize() -
                            pInventory.getItem(remainingSlot).getCount();


                    System.out.println(toFill + " FILL");


                    if(toFill > itemStack.getCount()) { // means we don't have enough to fill
                        toFill = itemStack.getCount();
                    }

                    itemStack.setCount(toFill);



                    remainingSlotItem.setCount(toFill + remainingSlotItem.getCount());

                    stack = remainingSlotItem;

                }

            
            }

            value = itemStack.getCount();


            System.out.println(pInventory.player.level().isClientSide() + " CIENT");
            int actualValue = updateAllStorages(itemStack, value, true, quickMove, slotIndex);

            
            if(quickMove) {

                System.out.println(actualValue);
                if(slot != -1 && actualValue > 0)
                    pInventory.setItem(slot, stack);
            }
            
            value = actualValue;

            itemStack.setCount(value);




            if(!pInventory.player.level().isClientSide())
                ModNetwork.sendToClient(new UpdateStorageInventoryClientEntityPacket(
                        drawerGridControllerEntity.getBlockPos(),
                        value, itemStack, slotIndex, quickMove, true), (ServerPlayer) pInventory.player);

            updateInsertVisual(drawerGridControllerEntity, itemStack,
                    value, quickMove, slotIndex, true);




            //updateStorageContents(itemStack, -value);



        }
        else { // place in storage

            updateAllStorages(itemStack, value, false, quickMove, slotIndex);

        }
    }



    private int updateAllStorages(ItemStack itemStack, int value, boolean take, boolean quickMove, int slotIndex) {


        int valueTakenOut = 0;
        ItemStack remainingStack = itemStack.copy();

        if(pInventory.player.level().isClientSide())
            return 0;

        int valueLeft = value;


        for(CustomBlockPosData blockPosData : drawerGridControllerEntity.getEditedChestLocations()) {

            BlockEntity storageBlockEntity = drawerGridControllerEntity.getStorageBlockAt(blockPosData.getBlockPos());

            if(storageBlockEntity != null) {


                IItemHandler chestInventory = storageBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .orElse(new ItemStackHandler(0));




                for(int i = 0; i < chestInventory.getSlots(); i++) {

                    ItemStack stackInSlot = chestInventory.getStackInSlot(i);

                    if(!take) {



                        ItemStack clonedItemStack = itemStack.copy();
                        clonedItemStack.setCount(remainingStack.getCount());

                        remainingStack = chestInventory.insertItem(i, clonedItemStack, false);




                        if(remainingStack.equals(ItemStack.EMPTY, false)) break;


                    }



                    if(ItemStack.isSameItemSameTags(itemStack, stackInSlot)) {




                        if(take) {


                            int calculateValueLeft = valueLeft - stackInSlot.getCount();


                            int toMinusFromStack = stackInSlot.getCount() - valueLeft;

                            valueLeft = Math.max(calculateValueLeft, 0);

                            ItemStack extracted = chestInventory.extractItem(i,
                                    stackInSlot.getCount() - Math.max(toMinusFromStack, 0),
                                    false);


                            valueTakenOut  += extracted.getCount();

                            if(valueLeft == 0) break;
                        }



                    }


                }


            }


            if(valueLeft == 0) break;


            if((remainingStack.equals(ItemStack.EMPTY, false)))
                break;


        }


        if(!take) { // if put into the storage

            if(!quickMove)
                setCarried(remainingStack);

            ModNetwork.sendToClient(new UpdateStorageInventoryClientEntityPacket(
                    drawerGridControllerEntity.getBlockPos(),
                    remainingStack.getCount(), itemStack, slotIndex, quickMove, false), (ServerPlayer) pInventory.player);

            updateInsertVisual(drawerGridControllerEntity, itemStack,
                    remainingStack.getCount(), quickMove, slotIndex, false);
        }
        else {

            remainingStack.setCount(valueTakenOut);

            if(!quickMove) {
                setCarried(remainingStack); // we take whatever that was clicked in the slot

            }

        }






        return valueTakenOut;




    }

    public void updateInsertVisual(DrawerGridControllerEntity d, ItemStack itemStack, int value, boolean quickMove,
                                   int slotIndex, boolean take) {

        int index = d.mergedStorageContents
                .indexOf(new ItemIdentifier(itemStack, 1));



        if(index != -1) {

            ItemIdentifier itemIdentifier = d.mergedStorageContents.get(
                    index);


            if(take) {
                int valueToStay = itemIdentifier.getCount() - value;
                itemIdentifier.setCount(valueToStay);

                regenerateCurrentPage();
                return;
            }

            int itemCountLeft = itemIdentifier.getCount()
                    + itemStack.getCount() - value;


            itemIdentifier.setCount(itemCountLeft);


            checkToRemoveInSlotForQuickMove(quickMove, itemCountLeft, slotIndex, value, itemStack);

            return;
        }


        int itemCountLeft = itemStack.getCount() - value;

        if(itemCountLeft != 0) {
            d.mergedStorageContents.add(new ItemIdentifier(itemStack,
                    itemCountLeft));

            checkToRemoveInSlotForQuickMove(quickMove, itemCountLeft, slotIndex, value, itemStack);

            regenerateCurrentPage();



        }

    }

    private void checkToRemoveInSlotForQuickMove(boolean quickMove, int itemCountLeft, int slotIndex, int value, ItemStack itemStack) {
        if(quickMove) {
            if(itemCountLeft != 0) {

                int finalValue = itemStack.getCount() - value;
                pInventory.removeItem(slotIndex, finalValue);
            }

        }
    }


    public void updateStorageContents(ItemStack itemStack, int value) {

        ItemIdentifier queuedToBeRemoved = null;


        int index = drawerGridControllerEntity.mergedStorageContents.indexOf(
                new ItemIdentifier(itemStack, 1));


        if(index == -1 ) return;

        ItemIdentifier existingItemIdentifier =
                drawerGridControllerEntity.mergedStorageContents.get(
                        index);

        existingItemIdentifier.setCount(existingItemIdentifier.getCount() + value);

        if(existingItemIdentifier.getCount() <= 0) {
            queuedToBeRemoved = existingItemIdentifier;
        }


        if(queuedToBeRemoved != null) {
            drawerGridControllerEntity.mergedStorageContents.remove(queuedToBeRemoved);
        }



        regenerateCurrentPage();


    }


    @Override
    public void addCustomSlot(Slot slot) {

        addSlot(slot);
    }
}
