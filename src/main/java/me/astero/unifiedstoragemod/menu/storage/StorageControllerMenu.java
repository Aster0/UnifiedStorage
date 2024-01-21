package me.astero.unifiedstoragemod.menu.storage;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.client.screen.widgets.*;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.items.generic.UpgradeCardItem;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.upgrades.IBlockUpdater;
import me.astero.unifiedstoragemod.menu.CustomResultSlot;
import me.astero.unifiedstoragemod.menu.Menu;
import me.astero.unifiedstoragemod.menu.data.*;
import me.astero.unifiedstoragemod.menu.interfaces.IMenuInteractor;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import me.astero.unifiedstoragemod.networking.packets.*;
import me.astero.unifiedstoragemod.registry.ItemRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class StorageControllerMenu extends Menu implements IMenuInteractor {

    public static final int VISIBLE_CONTENT_HEIGHT = 27;


    private boolean finishedAdding = false;

    private StorageSearchData storageSearchData = new StorageSearchData();

    private int scrollPage;

    private final StorageControllerEntity storageControllerEntity;
    private final ContainerLevelAccess containerLevelAccess;

    private Inventory pInventory;

    private int storageStackStartSlot = 0;

    public final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3) {
        @Override
        public void setChanged() {
            super.setChanged();



            if(!getPlayerInventory().player.level().isClientSide) {

                ModNetwork.sendToAllClient(new UpdateAllCraftingSlotsClientEntityPacket(this.getItems(),
                        getStorageControllerEntity().getBlockPos(), getPlayerInventory().player.getUUID()));

                for(int i = 0; i < craftSlots.getItems().size(); i++) {

                    getStorageControllerEntity().getCraftingInventory().setStackInSlot(i,
                            craftSlots.getItem(i));
                }
            }

        }
    };
    private final ResultContainer resultSlots = new ResultContainer();

    private int craftSlotIndexStart = 0;

    public int getStorageStackStartSlot() {
        return storageStackStartSlot;
    }

    public StorageControllerMenu(MenuType<?> type, int containerId, Inventory pInventory, BlockEntity blockEntity) {
        super(type, containerId);


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


        storageControllerEntity.menu = this;



    }

    public int getCraftSlotIndexStart() {
        return craftSlotIndexStart;
    }


    public StorageSearchData getStorageSearchData() {
        return storageSearchData;
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

    public int getTotalPages(List<ItemIdentifier> itemIdentifiers) {



        double value = (double) itemIdentifiers.size() / VISIBLE_CONTENT_HEIGHT;


        return (int) Math.ceil(value);
    }

    public void onStorageSearch(String entry) {


        storageSearchData.getSearchedStorageList().clear();


        storageSearchData.setSearching(true);


        String currentSearchString = entry.toLowerCase();

        if(currentSearchString.startsWith("@")) { // search by modid

            String[] splitSearch = currentSearchString.split(" ");

            currentSearchString = "";

            for(int i = 1; i < splitSearch.length; i++) { // ignore the @modid part

                currentSearchString += splitSearch[i].toLowerCase() + " ";
            }


            currentSearchString = currentSearchString.trim();
        }

        for(ItemIdentifier itemIdentifier : storageControllerEntity.mergedStorageContents) {








            if(currentSearchString.startsWith("#")) {

                String finalCurrentSearchString = currentSearchString;

                if(finalCurrentSearchString.substring(1).length() == 0)
                    return;



                Optional<TagKey<Item>> tagKey = itemIdentifier.getItemStack().getTags().filter(itemTagKey ->
                                itemTagKey.location().toString().toLowerCase()
                                        .contains(finalCurrentSearchString.substring(1)))
                        .findAny();



                if(tagKey.isPresent()) {
                    storageSearchData.getSearchedStorageList().add(itemIdentifier);
                }


                continue;

            }



            String itemName = itemIdentifier.getItemStack().getDisplayName().getString().toLowerCase();
            // [Compass]
            // TODO: search by lore


            if(itemName.substring(1, itemName.length() - 1)
                    .contains(currentSearchString)) {


                storageSearchData.getSearchedStorageList().add(itemIdentifier);
            }
            else { // if we can't match the name, we try the lore.

                for(Component component : itemIdentifier.getItemStack().getTooltipLines(null, TooltipFlag.NORMAL)) {
                    if(component.getString().toLowerCase().contains(currentSearchString)) {
                        storageSearchData.getSearchedStorageList().add(itemIdentifier);
                    }
                }
            }


        }

        if(entry.startsWith("@")) {


            List<ItemIdentifier> editedSearchedList = new ArrayList<>();

            for(ItemIdentifier itemIdentifier : storageSearchData.getSearchedStorageList()) {

                try {
                    String modId = itemIdentifier.getItemStack().getItem().getDescriptionId().split("\\.")[1];


                    if(modId.toLowerCase().contains(entry.split(" ")[0].toLowerCase().substring(1))) {
                        editedSearchedList.add(itemIdentifier);
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {


                }
            }


            storageSearchData.setSearchedStorageList(editedSearchedList);
        }



        generateStartPage();




    }

    private void createInventory(Inventory pInventory) {

        storageControllerEntity.setDisabled(true);
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



    public int onItemCrafted(ItemStack itemStack, boolean quickMove) {


        if(!updateRecipeResult(getPlayerInventory().player).equals(itemStack, false)) { // make sure that the crafting slots corresponds with the result
            return 0;
        }

        if(itemStack == null || itemStack.equals(ItemStack.EMPTY, false)) {
            return 0;
        }

        ItemStack copiedStack = itemStack.copy();

        if(getCarried().equals(ItemStack.EMPTY, false) ||
                ItemStack.isSameItem(itemStack, getCarried())) {





            if (!quickMove) {

                if (!getCarried().equals(ItemStack.EMPTY, false)) {

                    copiedStack.setCount(itemStack.getCount() + getCarried().getCount());


                }

                //setCarried(copiedStack);




                if(!getPlayerInventory().player.level().isClientSide)  {

                    int index = 0;
                    for (ItemStack stack : craftSlots.getItems()) {


                        boolean canRemove = canRemoveItemFromInventory(stack, true, false, 1);

                        if(!stack.equals(ItemStack.EMPTY, false)) {

                            ItemStack remainingCraftingItem = ItemStack.EMPTY;

                            if(!stack.getCraftingRemainingItem().equals(ItemStack.EMPTY)) {
                                remainingCraftingItem = stack.getCraftingRemainingItem();
                            }

                            if(!canRemove) {



                                stack.shrink(1);

                                updateRemainingItem(stack, remainingCraftingItem, index);

                            }
                            else { // if we can remove from inventory, we put the remaining item in the inventory
                                moveToInventory(remainingCraftingItem);
                            }

                        }

                        index++;

                    }

                    slotsChanged(craftSlots);

                }

                craftSlots.setChanged();







                return 0;
            }


        }

        if(getPlayerInventory().getFreeSlot() == -1)
            return 0;


        List<ItemIdentifier> itemOccurrence = new ArrayList<>();


        int lowestCount = 999;
        int lowestCountOnGrid = 999;
        for(ItemStack stack : craftSlots.getItems()) { // see how many times i can craft

            if(!stack.equals(ItemStack.EMPTY, false)) {

                ItemIdentifier itemIdentifier = new ItemIdentifier(stack, 1);

                boolean justAdded = false;

                int index = itemOccurrence.indexOf(itemIdentifier);

                if(index == -1) {
                    itemOccurrence.add(itemIdentifier);
                    justAdded = true;
                }

                if(!justAdded) {
                    itemIdentifier = itemOccurrence.get(index);
                    itemIdentifier.setCount(itemIdentifier.getCount() + 1);
                }



                if(stack.getCount() < lowestCountOnGrid)
                    lowestCountOnGrid = stack.getCount();
            }

        }


        int craftableFromGrid = copiedStack.getCount() * lowestCountOnGrid;

        for(ItemIdentifier itemIdentifier : itemOccurrence) {

            int index = getStorageControllerEntity().mergedStorageContents.indexOf(itemIdentifier);

            int stackCount = index != -1 ? getStorageControllerEntity().getMergedStorageContents(index).getCount() : 0;
            int stackCountRevised = stackCount / itemIdentifier.getCount(); // divided by the occurrence



            if(stackCountRevised < lowestCount) {
                lowestCount = stackCountRevised;
            }

        }



        int craftedAmount = copiedStack.getCount() * lowestCount;




        if(craftedAmount > itemStack.getMaxStackSize()) {

            double divideBy = (double) craftedAmount / itemStack.getMaxStackSize();

            lowestCount = (int) (lowestCount / divideBy); // new lowest amount to hit nearest 64



        }


        int newCraftedAmount = copiedStack.getCount() * lowestCount;


        if(newCraftedAmount == copiedStack.getMaxStackSize()) {
            craftableFromGrid = 0; // dont use grid
            lowestCountOnGrid = 0;
        }
        else {
            // we see how many we need from the crafting grid to make it 64

            int newCraftingStackNeeded = (copiedStack.getMaxStackSize() - newCraftedAmount) / copiedStack.getCount();

            if(newCraftingStackNeeded < lowestCountOnGrid) {

                lowestCountOnGrid = newCraftingStackNeeded;
                craftableFromGrid = copiedStack.getCount() * lowestCountOnGrid;
            }

        }

        copiedStack.setCount((copiedStack.getCount() * lowestCount) + craftableFromGrid);


        // occurrence * lowest stack count to remove * must be after we confirmed the lowest stack count
        for(ItemIdentifier itemIdentifier : itemOccurrence) {
            canRemoveItemFromInventory(itemIdentifier.getItemStack(),
                    true, false, itemIdentifier.getCount() * lowestCount);


            if(!itemIdentifier.getItemStack().getCraftingRemainingItem().equals(ItemStack.EMPTY)) {

                ItemStack remainingItem = itemIdentifier.getItemStack().getCraftingRemainingItem().copy();

                remainingItem.setCount(itemIdentifier.getCount() * lowestCount);
                moveToInventory(remainingItem);
            }
        }





        int movedValue = copiedStack.getCount();

        if(moveItemStackTo(copiedStack, 0, 36, false)) {

            for(int i = 0; i < lowestCountOnGrid; i++) {

                int index = 0;
                for(ItemStack stack : craftSlots.getItems()) {

                    ItemStack remainingItem = stack.getCraftingRemainingItem();

                    stack.shrink(1);



                    if(!remainingItem.equals(ItemStack.EMPTY)) {
                        updateRemainingItem(stack, remainingItem, index);
                    }

                    index++;
                }
            }

            craftSlots.setChanged();
            slotsChanged(craftSlots);



            return movedValue;

        }

        return 0;

    }

    private void updateRemainingItem(ItemStack stack, ItemStack remainingItem, int index) {
        if(stack.getCount() > 0) {
            // move to inventory
            moveToInventory(remainingItem);

        }
        else {
            // put at crafting slot
            this.craftSlots.setItem(index, remainingItem);
            // e.g., water_bottle becomes empty_bottle

        }
    }

    public void generateSlots(int page) {



        if(storageStackStartSlot == 0)
            storageStackStartSlot = slots.size();

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                int currentIndex = (row * 9) + column;



                ItemIdentifier itemIdentifier =
                        storageControllerEntity.getMergedStorageContents(currentIndex,
                                storageSearchData.getSearchedStorageList(), false);


                if(page == 1 && !finishedAdding) {
                    addSlot(new ItemVisualSlot(itemIdentifier,
                            8 + (column * 18), 18 + (row * 18), currentIndex));




                    if(currentIndex == VISIBLE_CONTENT_HEIGHT - 1)
                        finishedAdding = true;
                }

                else {


                    int slotToFetch = ((page - 1) * VISIBLE_CONTENT_HEIGHT) + currentIndex;


                    int slotIndex = storageStackStartSlot + currentIndex;




                    itemIdentifier =
                            storageControllerEntity.getMergedStorageContents(slotToFetch,
                                    storageSearchData.getSearchedStorageList(), storageSearchData.isSearching());


                    if(slots.get(slotIndex) instanceof ItemVisualSlot) {

                        slots.set(slotIndex, new ItemVisualSlot(itemIdentifier,
                                8 + (column * 18), 18 + (row * 18), slotToFetch));


                    }
                }
            }
        }




    }








    private void createPlayerInventory(Inventory pInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                addSlot(new PlayerSlot(pInventory, 9 + column + (row * 9),
                        8 + (column * 18),
                        153 + (row * 18)));


            }
        }
    }

    private void createCraftingSlots() {

        this.addSlot(new CustomResultSlot<>(pInventory.player,
                this.craftSlots, this.resultSlots, 0, 131, 103, this));


        for(int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {

                // 37, 85

                if(craftSlotIndexStart == 0)
                    craftSlotIndexStart = this.slots.size();

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

            addSlot(new PlayerSlot(pInventory, column, 8 + (column * 18),
                    211));
        }
    }


    private void createUpgradeSlots() {

        NetworkSlotGUI<StorageControllerMenu> networkSlotGUI = new NetworkSlotGUI<>(1,  210, 0,
                210, 0, storageControllerEntity.getNetworkInventory());

        networkSlotGUI.create(this);

        UpgradeSlotGUI<StorageControllerMenu> upgradeSlotGUI = new UpgradeSlotGUI<>(3,  210, 40,
                210, 40, storageControllerEntity.getUpgradeInventory(), SlotType.UPGRADE);

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


        if(fromSlot instanceof CustomResultSlot resultSlot) {
            resultSlot.onQuickStackCraft(player, fromStack);
            return ItemStack.EMPTY;
        }



        if(fromStack.getItem() instanceof NetworkItem || fromStack.getItem() instanceof UpgradeCardItem) {



            if(!(fromSlot instanceof UpgradeSlot) && !(fromSlot instanceof NetworkSlot)) {


                ItemStack beforeMoveFromStack = fromStack.copy();

                if(moveItemStackTo(fromStack, 63, 67, false))  {
                    if(beforeMoveFromStack.getItem() instanceof NetworkItem) {



                        if(player.level().isClientSide) {
                            getStorageControllerEntity().setDisabled(false);
                        }
                        else {
                            getStorageControllerEntity().updateNetworkCardItems(beforeMoveFromStack,
                                    getStorageControllerEntity().getLevel().getPlayerByUUID(
                                            player.getUUID()
                                    ));
                        }

                    }
                    else if(beforeMoveFromStack.getItem() instanceof IBlockUpdater blockUpdater) {
                        blockUpdater.update(getStorageControllerEntity());
                    }

                    return ItemStack.EMPTY;
                }



            }
            else {

                moveItemStackTo(fromStack, 0, 36, false); // move to inventory


            }


            getStorageControllerEntity().setChanged();


        }
        else if(fromSlot.container instanceof TransientCraftingContainer || fromSlot instanceof VisualItemSlot) {
            moveItemStackTo(fromStack, 0, 36, false); // move to inventory


            if(fromSlot.container instanceof TransientCraftingContainer) {
                slotsChanged(fromSlot.container);
            }
        }

        if(fromSlot instanceof PlayerSlot) {



            interactWithMenu(fromStack, false,
                    fromStack.getCount(), true, fromSlot.getSlotIndex());

            return ItemStack.EMPTY;
        }


        fromSlot.setChanged();



        return ItemStack.EMPTY; // basically return nothing because ours is custom implementation and
        // we don't want the usual. as sometimes quick move means moving to the custom network inventory.
    }


    @Override
    public boolean stillValid(Player player) {

        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(ItemRegistry.WIRELESS_STORAGE.get())
                || stillValid(this.containerLevelAccess, player, storageControllerEntity.getBlockState().getBlock());
    }


    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);




        if(container instanceof TransientCraftingContainer) {




            if(!getPlayerInventory().player.level().isClientSide) {

                updateRecipeResult(getPlayerInventory().player);


            }




            getStorageControllerEntity().setChanged();



        }




    }

    public ItemStack updateRecipeResult(Player player) {

        if(!(player instanceof ServerPlayer)) {
            return ItemStack.EMPTY;
        }
        ItemStack itemResult = ItemStack.EMPTY;

        Optional<RecipeHolder<CraftingRecipe>> optional =
                player.level().getServer().getRecipeManager()
                        .getRecipeFor(RecipeType.CRAFTING, craftSlots, player.level());

        changeCraftingResult(ItemStack.EMPTY, (ServerPlayer) player);

        if(optional.isPresent()) {
            RecipeHolder<CraftingRecipe> recipeHolder = optional.get();
            CraftingRecipe craftingRecipe = recipeHolder.value();
            ItemStack result = craftingRecipe.assemble(craftSlots, player.level().registryAccess());

            itemResult = result;
            changeCraftingResult(result,  (ServerPlayer) player);
        }

        return itemResult;
    }

    private void changeCraftingResult(ItemStack stack, ServerPlayer player) {
        ModNetwork.sendToClient(new SendCraftingResultEntityPacket(stack), player);

        changeCraftingResultSlot(stack); // server side
    }

    public void findRecipe(ItemStack itemStack, int slot, ItemStack itemStackToStore) {

        ModNetwork.sendToServer(new UpdateCraftingSlotsEntityPacket(itemStack, slot, true, itemStackToStore, true));
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

            if(!getCarried().equals(ItemStack.EMPTY, false)) {
                return;
            }


            ItemIdentifier itemIdentifier = storageControllerEntity.mergedStorageContents.get(
                    index);




            if(itemIdentifier.getCount() < value) {
                return; // if somehow we don't have enough value to take it out of the storage
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




//            if(!pInventory.player.level().isClientSide()) {
//                ModNetwork.sendToClient(new UpdateStorageInventoryClientEntityPacket(
//                        storageControllerEntity.getBlockPos(),
//                        value, itemStack, slotIndex, quickMove, true), (ServerPlayer) pInventory.player);
//            }





            // update visual was here last time


            if(!pInventory.player.level().isClientSide)
                updateStorageNetwork(storageControllerEntity, itemStack, value, quickMove, slotIndex, true);





        }
        else { // place in storage

            ItemStack toPutIntoStorage = getCarried();


            if(quickMove)
                toPutIntoStorage = itemStack;


            updateAllStorages(toPutIntoStorage, value, false, quickMove, slotIndex);

        }



        if(!pInventory.player.level().isClientSide) {


            pInventory.player.level()
                    .sendBlockUpdated(storageControllerEntity.getBlockPos(),
                            storageControllerEntity.getBlockState(), storageControllerEntity.getBlockState(),
                            Block.UPDATE_CLIENTS);

        }

        storageControllerEntity.setUpdateClientsOnStorageChange(true);

//            ModNetwork.sendToAllClient(new MergedStorageLocationEntityPacket(storageControllerEntity.mergedStorageContents,
//                    storageControllerEntity.getBlockPos(), false, pInventory.player.getUUID(), false));
    }


    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);


        for(BlockEntity entity : storageControllerEntity.getCachedStorages()) {


            storageControllerEntity.forceChunk(false, entity);



        }

    }

    private void updateLocationAmount(String storageName, ItemStack itemStack, int value) {

        int index = storageControllerEntity.mergedStorageContents
                .indexOf(new ItemIdentifier(itemStack, 1));


        if(index != -1) { // found

            ItemIdentifier itemIdentifier = storageControllerEntity.mergedStorageContents.get(index);

            int valueBefore = itemIdentifier.getLocations().get(storageName) == null ? 0 :
                    Math.max(itemIdentifier.getLocations().get(storageName), 0);



            if(value != 0)
                itemIdentifier.getLocations().put(storageName, valueBefore + value);

            return;


        }


        if(value != 0) {
            Map<String, Integer> locations = new HashMap<>();
            locations.put(storageName, value);
            storageControllerEntity.mergedStorageContents.add(new ItemIdentifier(itemStack.copy(), 0, locations));
        }



    }

    public int updateAllStorages(ItemStack itemStack, int value, boolean take, boolean quickMove, int slotIndex) {



        int valueTakenOut = 0;
        ItemStack remainingStack = itemStack.copy();

        if(pInventory.player.level().isClientSide())
            return 0;

        int valueLeft = value;
        remainingStack.setCount(value);



        for(SavedStorageData blockPosData : storageControllerEntity.getEditedChestLocations()) {

            BlockEntity storageBlockEntity = storageControllerEntity.getStorageBlockAt(blockPosData);

            if(storageBlockEntity != null) {


                IItemHandler chestInventory = storageBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER)
                        .orElse(new ItemStackHandler(0));

                if(!take) {
                    for(int i = 0; i < chestInventory.getSlots(); i++) {

                        // we find slots that have this item stack first to put in
                        ItemStack stackInSlot = chestInventory.getStackInSlot(i);
                        if(ItemStack.isSameItem(stackInSlot, itemStack)) {


                            if(stackInSlot.getCount() >= stackInSlot.getMaxStackSize()) // means we can't insert, so just skip.
                                continue;

                            int previousRemainingStackCount = remainingStack.getCount();

                            remainingStack = chestInventory.insertItem(i, remainingStack, false);


                            updateLocationAmount(ModUtils.capitalizeName(
                                            storageBlockEntity.getBlockState().getBlock().asItem().toString()),
                                    itemStack, previousRemainingStackCount - remainingStack.getCount());


                        }

                    }
                }




                for(int i = 0; i < chestInventory.getSlots(); i++) {

                    ItemStack stackInSlot = chestInventory.getStackInSlot(i);

                    if(!take) {

                        // if not we put into empty slots

                        int previousRemainingStackCount = remainingStack.getCount();
                        remainingStack = chestInventory.insertItem(i, remainingStack, false);

                        updateLocationAmount(ModUtils.capitalizeName(
                                        storageBlockEntity.getBlockState().getBlock().asItem().toString()),
                                itemStack, previousRemainingStackCount - remainingStack.getCount());

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


                            updateLocationAmount(ModUtils.capitalizeName(
                                    storageBlockEntity.getBlockState().getBlock().asItem().toString()),
                                    itemStack, -extracted.getCount());

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




            updateStorageNetwork(storageControllerEntity, itemStack,
                    remainingStack.getCount(), quickMove, slotIndex, false);



            return remainingStack.getCount(); // how many we couldn't put in
        }
        else {

            remainingStack.setCount(valueTakenOut);


            if(!quickMove) {

                setCarried(remainingStack); // we take whatever that was clicked in the slot

            }

        }




        return valueTakenOut;




    }

    public void updateStorageNetwork(StorageControllerEntity controller, ItemStack itemStack, int value, boolean quickMove,
                                     int slotIndex, boolean take) {


        int index = controller.mergedStorageContents
                .indexOf(new ItemIdentifier(itemStack, 1));



        if(index != -1) {

            ItemIdentifier itemIdentifier = controller.mergedStorageContents.get(
                    index);


            if(take) {
                int valueToStay = itemIdentifier.getCount() - value;
                itemIdentifier.setCount(valueToStay);

                if(valueToStay <= 0) {


                    if(quickMove) {
                        controller.queueToRemoveItems.add(itemIdentifier);
                    }
                }






                regenerateCurrentPage();

                return;
            }


            int canInsertAmount = itemIdentifier.getCount()
                    + itemStack.getCount() - value;



            itemIdentifier.setCount(canInsertAmount);


            checkToRemoveInSlotForQuickMove(quickMove, canInsertAmount, slotIndex, value, itemStack);


            return;
        }


        if(!take) {
            int remainingValue = itemStack.getCount() - value;


            if(remainingValue != 0) {


                ItemIdentifier itemIdentifier = new ItemIdentifier(itemStack.copy(),
                        remainingValue);

                controller.mergedStorageContents.add(itemIdentifier);



                checkToRemoveInSlotForQuickMove(quickMove, remainingValue, slotIndex, value, itemStack);


                regenerateCurrentPage();




            }
        }





    }


    private void checkToRemoveInSlotForQuickMove(boolean quickMove, int itemCountLeft, int slotIndex, int value, ItemStack itemStack) {
        if(slotIndex == -1)
            return;

        if(quickMove) {

            if(itemCountLeft != 0) {


                int finalValue = itemStack.getCount() - value;
                pInventory.removeItem(slotIndex, finalValue);
            }

        }
    }

    public void onRecipeTransfer(IRecipeSlotsView recipeSlots) {


        for(int i = 1; i < recipeSlots.getSlotViews().size(); i++) {

            Optional<ItemStack> currentItemStack = recipeSlots.getSlotViews().get(i).getDisplayedItemStack();





            ItemStack craftingGridItem = this.craftSlots.getItem(i - 1);

            // remove item from the crafting grid first before transferring
            if(currentItemStack.isPresent()) {

                findRecipe(currentItemStack.get().copy(), i - 1, craftingGridItem.copy()); // server


            }


        }
    }

    public void populateCraftSlots(ItemStack itemStack, int slot) {


        this.craftSlots.setItem(slot, itemStack);

        craftSlots.setChanged();
    }


    public boolean canInsertItemIntoInventory(ItemStack itemStack, int value, int slot, boolean moveToPlayer) {



        // we try to put into the storage first
        int remaining = updateAllStorages(itemStack, value, false, true, -1);


        ItemStack copiedStack = itemStack.copy();
        copiedStack.setCount(remaining);

        populateCraftSlots(copiedStack, slot);


        if(remaining > 0 && moveToPlayer) { // means we can't put all into the storage
            // so we move to the player's inventory
            if(moveItemStackTo(copiedStack, 0, 36, false)) {
                populateCraftSlots(ItemStack.EMPTY, slot);



                return true;
            }


        }




        return remaining == 0;
    }

    public void moveToInventory(ItemStack itemStack) {
        if(!moveItemStackTo(itemStack, 0, 36, false)) {
            getPlayerInventory().player.drop(itemStack, false);
        }
    }

    public boolean canRemoveItemFromInventory(ItemStack itemStack, boolean remove, boolean removeFromPlayer, int value) {


        ItemIdentifier temporaryIdentifier = new ItemIdentifier(itemStack, 1);

        int index = getStorageControllerEntity().mergedStorageContents.indexOf(temporaryIdentifier);

        ItemIdentifier itemIdentifier = getStorageControllerEntity().getMergedStorageContents(index);

        /*

            PRIORITY = TAKE FROM STORAGE FIRST THEN TAKE FROM PLAYER'S INVENTORY!
         */



        if(index != -1 && itemIdentifier.getCount() >= value) {

            if(remove) {

                int value1 = updateAllStorages(itemStack, value, true, true, 0);


                if(value1 >= value) {

                    if(!pInventory.player.level().isClientSide()) {
                        itemIdentifier.setCount(itemIdentifier.getCount() - value);



                    }

                    return true;

                }






            }

            return false;
        }
        else if(getPlayerInventory().findSlotMatchingItem(itemStack) != -1 && removeFromPlayer) { // check player's inventory



            if(remove) {
                getPlayerInventory().getItem(getPlayerInventory().findSlotMatchingItem(itemStack)).shrink(1);
            }

            return true;


        }


        return false;


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
