package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.client.screen.widgets.*;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.NetworkCardItem;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.menu.data.CustomGUISlot;
import me.astero.unifiedstoragemod.menu.data.StorageSearchData;
import me.astero.unifiedstoragemod.menu.data.VisualItemSlot;
import me.astero.unifiedstoragemod.menu.interfaces.IMenuInteractor;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.GetCraftingRecipesEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.UpdateStorageInventoryClientEntityPacket;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StorageControllerMenu extends Menu implements IMenuInteractor {

    public static final int VISIBLE_CONTENT_HEIGHT = 27, STARTING_SLOT_INDEX = 36;


    private boolean finishedAdding = false;

    private StorageSearchData storageSearchData = new StorageSearchData();

    private int scrollPage;

    private final StorageControllerEntity storageControllerEntity;
    private final ContainerLevelAccess containerLevelAccess;

    private Inventory pInventory;
    public final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();

    public StorageControllerMenu(int containerId, Inventory pInventory, FriendlyByteBuf friendlyByteBuf) {

        this(containerId, pInventory, pInventory
                .player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));





    }

    public StorageControllerMenu(int containerId, Inventory pInventory, BlockEntity blockEntity) {
        super(MenuRegistry.GRID_CONTROLLER_MENU.get(), containerId);


        if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {
            this.storageControllerEntity = storageControllerEntity;

        }
        else {
            throw new IllegalStateException("Incorrect block entity class, please check again. (%s)"
                    .formatted(blockEntity.getClass().getCanonicalName()));
        }

        this.containerLevelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());


        this.pInventory = pInventory;

        createInventory(pInventory);




    }


    @Override
    public void removed(Player player) {
        super.removed(player);

        for(int i = 0; i < craftSlots.getItems().size(); i++) {

            getStorageControllerEntity().getCraftingInventory().setStackInSlot(i,
                    craftSlots.getItem(i));
        }




    }

    public Inventory getPlayerInventory() {
        return pInventory;
    }

    public StorageControllerEntity getStorageControllerEntity() {
        return storageControllerEntity;
    }

    public void onStorageSearchStop() {
        storageSearchData.setSearching(false);
        generateStartPage();

    }

    public int getTotalPages() {



        double value = (double) storageControllerEntity.mergedStorageContents.size() / VISIBLE_CONTENT_HEIGHT;


        return (int) Math.ceil(value);
    }

    public void onStorageSearch(String entry) {


        storageSearchData.getSearchedStorageList().clear();


        storageSearchData.setSearching(true);
        for(ItemIdentifier itemIdentifier : storageControllerEntity.mergedStorageContents) {


            String itemName = itemIdentifier.getItemStack().getDisplayName().getString().toLowerCase();
            // [Compass]

            if(itemName.substring(1, itemName.length() - 1)
                    .contains(entry.toLowerCase())) {

                storageSearchData.getSearchedStorageList().add(itemIdentifier);
            }
        }

        generateStartPage();




    }

    private void createInventory(Inventory pInventory) {

        createPlayerHotbar(pInventory);
        createPlayerInventory(pInventory);
        createBlockEntityInventory();
        createUpgradeSlots();

        createCraftingSlots();


    }



    public void createBlockEntityInventory() {

        //drawerGridControllerEntity.getOptional();
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

    public void onItemCrafted(ItemStack itemStack, boolean quickMove) {

        if(itemStack == null || itemStack.equals(ItemStack.EMPTY, false)) {
            return;
        }

        ItemStack copiedStack = itemStack.copy();

        if(getCarried().equals(ItemStack.EMPTY, false) ||
                ItemStack.isSameItem(itemStack, getCarried())) {





            if (!quickMove) {

                if (!getCarried().equals(ItemStack.EMPTY, false)) {

                    copiedStack.setCount(itemStack.getCount() + getCarried().getCount());


                }

                setCarried(copiedStack);

                for (ItemStack stack : craftSlots.getItems()) {
                    stack.shrink(1);
                }

                return;
            }
        }

        int lowestCount = 999;
        for(ItemStack stack : craftSlots.getItems()) { // see how many times i can craft

            if(!stack.equals(ItemStack.EMPTY, false))
                if(stack.getCount() < lowestCount)
                    lowestCount = stack.getCount();

        }



        copiedStack.setCount(copiedStack.getCount() * lowestCount);



        if(moveItemStackTo(copiedStack, 0, 36, false)) {


            for(int i = 0; i < lowestCount; i++) {
                for(ItemStack stack : craftSlots.getItems()) {
                    stack.shrink(1);

                }
            }


        }








    }

    public void generateSlots(int page) {




        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                int currentIndex = (row * 9) + column;



                ItemIdentifier itemIdentifier =
                        storageControllerEntity.getMergedStorageContents(currentIndex,
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
                            storageControllerEntity.getMergedStorageContents(slotToFetch,
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

    private void createCraftingSlots() {

        this.addSlot(new ResultSlot(pInventory.player,
                this.craftSlots, this.resultSlots, 0, 131, 103));


        for(int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {

                // 37, 85

                addSlot(new Slot(this.craftSlots, column + (row * 3),
                        37 + (column * 18), 85 + (row * 18)));

            }
        }


        for(int i = 0; i < getStorageControllerEntity().getCraftingInventory().getSlots(); i++) {

            ItemStack stack = getStorageControllerEntity().getCraftingInventory().getStackInSlot(i);

            if(!stack.equals(ItemStack.EMPTY, false)) {

                this.craftSlots.setItem(i, stack);
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

        NetworkSlotGUI<StorageControllerMenu> networkSlotGUI = new NetworkSlotGUI<>(1,  210, 0,
                210, 0, storageControllerEntity.getNetworkInventory());

        networkSlotGUI.create(this);

        UpgradeSlotGUI<StorageControllerMenu> upgradeSlotGUI = new UpgradeSlotGUI<>(3,  210, 40,
                210, 40, null, SlotType.UPGRADE);

        upgradeSlotGUI.create(this);


        VisualItemSlotGUI<StorageControllerMenu> visualItemSlotGUI = new VisualItemSlotGUI<>(1,  210, 120,
                210, 120, storageControllerEntity.getVisualItemInventory());

        visualItemSlotGUI.create(this);


        widgets.add(networkSlotGUI);
        widgets.add(upgradeSlotGUI);
        widgets.add(visualItemSlotGUI);

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot fromSlot = getSlot(index);
        ItemStack fromStack = fromSlot.getItem();



        if(fromStack.getItem() instanceof NetworkCardItem) {

            moveItemStackTo(fromStack, 63, 64, false);
        }
        else if(fromSlot.container instanceof TransientCraftingContainer || fromSlot instanceof VisualItemSlot) {
            moveItemStackTo(fromStack, 0, 36, false); // move to inventory


            if(fromSlot.container instanceof TransientCraftingContainer) {
                slotsChanged(fromSlot.container);
            }
        }


        fromSlot.setChanged();



        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.containerLevelAccess, player, BlockRegistry.STORAGE_CONTROLLER_BLOCK.get());
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);





        if(container instanceof TransientCraftingContainer) {

            findRecipe();
            getStorageControllerEntity().setChanged();
        }




    }

    public void findRecipe() {


        ModNetwork.sendToServer(new GetCraftingRecipesEntityPacket());





    }

    public void changeCraftingResultSlot(ItemStack itemStack) {
        if(itemStack != null)
            resultSlots.setItem(0, itemStack);
    }

    @Override
    public void interactWithMenu(ItemStack itemStack, boolean take, int value, boolean quickMove, int slotIndex) {


        if(take) {

            int index = storageControllerEntity.mergedStorageContents
                    .indexOf(new ItemIdentifier(itemStack, 1));



            if(index == -1) return;

            ItemIdentifier itemIdentifier = storageControllerEntity.mergedStorageContents.get(
                    index);



            if(itemIdentifier.getCount() < value) {
                return; // somehow we don't have enough value to take it out of the storage
            }



            itemStack.setCount(value);

            int slot = -1;
            ItemStack stack = itemStack.copy();

            ItemStack remainingSlotItem = null;

            if(quickMove) {

                int remainingSlot = pInventory.getSlotWithRemainingSpace(itemStack);
                int freeSlot = pInventory.getFreeSlot();

                if(pInventory.getFreeSlot() == -1 && remainingSlot == -1) // no free slots, dont extract.
                    return;

                slot = remainingSlot != -1 ? remainingSlot :
                        freeSlot;



                if(remainingSlot != -1) {

                    remainingSlotItem = pInventory.getItem(remainingSlot);
                    int toFill = remainingSlotItem.getMaxStackSize() -
                            pInventory.getItem(remainingSlot).getCount();



                    if(toFill > itemStack.getCount()) { // means we don't have enough to fill
                        toFill = itemStack.getCount();
                    }

                    itemStack.setCount(toFill);




                    //remainingSlotItem.setCount(toFill + remainingSlotItem.getCount());

                    stack = remainingSlotItem;

                }


            }

            value = itemStack.getCount();


            int actualValue = updateAllStorages(itemStack, value, true, quickMove, slotIndex);
            value = actualValue;


            
            if(quickMove) {




                if(slot != -1 && actualValue > 0) {

                    stack.setCount(actualValue +
                            (remainingSlotItem == null ? 0 : remainingSlotItem.getCount()));

                    pInventory.setItem(slot, stack);
                }

            }




            if(!pInventory.player.level().isClientSide()) {
                ModNetwork.sendToClient(new UpdateStorageInventoryClientEntityPacket(
                        storageControllerEntity.getBlockPos(),
                        value, itemStack, slotIndex, quickMove, true), (ServerPlayer) pInventory.player);
            }




            // update visual was here last time


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
        remainingStack.setCount(value);



        for(SavedStorageData blockPosData : storageControllerEntity.getEditedChestLocations()) {

            BlockEntity storageBlockEntity = storageControllerEntity.getStorageBlockAt(blockPosData
                    .getCustomBlockPosData().getBlockPos());

            if(storageBlockEntity != null) {


                IItemHandler chestInventory = storageBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .orElse(new ItemStackHandler(0));

                if(!take) {
                    for(int i = 0; i < chestInventory.getSlots(); i++) {

                        // we find slots that have this item stack first to put in
                        ItemStack stackInSlot = chestInventory.getStackInSlot(i);
                        if(ItemStack.isSameItem(stackInSlot, itemStack)) {

                            remainingStack = chestInventory.insertItem(i, remainingStack, false);

                        }

                    }
                }



                for(int i = 0; i < chestInventory.getSlots(); i++) {

                    ItemStack stackInSlot = chestInventory.getStackInSlot(i);

                    if(!take) {

                        // if not we put into empty slots


                        remainingStack = chestInventory.insertItem(i, remainingStack, false);


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

            ItemStack temporaryStack = itemStack.copy();

            temporaryStack.setCount(itemStack.getCount() - (value - remainingStack.getCount()));
            remainingStack = temporaryStack;


            if(!quickMove) {




                setCarried(remainingStack);

            }



            ModNetwork.sendToClient(new UpdateStorageInventoryClientEntityPacket(
                    storageControllerEntity.getBlockPos(),
                    remainingStack.getCount(), itemStack, slotIndex, quickMove, false), (ServerPlayer) pInventory.player);

            updateInsertVisual(storageControllerEntity, itemStack,
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

    public void updateInsertVisual(StorageControllerEntity d, ItemStack itemStack, int value, boolean quickMove,
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

            ItemIdentifier itemIdentifier = new ItemIdentifier(itemStack,
                    itemCountLeft);
            d.mergedStorageContents.add(itemIdentifier);

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


        int index = storageControllerEntity.mergedStorageContents.indexOf(
                new ItemIdentifier(itemStack, 1));


        if(index == -1 ) return;

        ItemIdentifier existingItemIdentifier =
                storageControllerEntity.mergedStorageContents.get(
                        index);

        existingItemIdentifier.setCount(existingItemIdentifier.getCount() + value);

        if(existingItemIdentifier.getCount() <= 0) {
            queuedToBeRemoved = existingItemIdentifier;
        }


        if(queuedToBeRemoved != null) {
            storageControllerEntity.mergedStorageContents.remove(queuedToBeRemoved);
        }



        regenerateCurrentPage();


    }


    @Override
    public void addCustomSlot(Slot slot) {

        addSlot(slot);
    }
}
