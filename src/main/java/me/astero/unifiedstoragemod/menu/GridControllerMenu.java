package me.astero.unifiedstoragemod.menu;

import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.registry.MenuRegistry;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.menu.data.ViewOnlySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GridControllerMenu extends AbstractContainerMenu {

    public static final int VISIBLE_CONTENT_HEIGHT = 27, STARTING_SLOT_INDEX = 36;


    private int scrollPage;

    private final DrawerGridControllerEntity drawerGridControllerEntity;
    private final ContainerLevelAccess containerLevelAccess;

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

    }

    private void createInventory(Inventory pInventory, DrawerGridControllerEntity drawerGridControllerEntity) {

        createPlayerInventory(pInventory);
        createPlayerHotbar(pInventory);
        createBlockEntityInventory(drawerGridControllerEntity);

    }



    private void createBlockEntityInventory(DrawerGridControllerEntity drawerGridControllerEntity) {


        drawerGridControllerEntity.getOptional();

//        drawerGridControllerEntity.getOptional().ifPresent(inventory -> {
//            for (int row = 0; row < 3; row++) {
//                for (int column = 0; column < 9; column++) {
//                    addSlot(new SlotItemHandler(inventory,
//                            column + (row * 9),
//                            8 + (column * 18),
//                            18 + (row * 18)));
//                }
//            }
//        });




        scrollPage = 1;
        generateSlots(scrollPage);



    }

    public void nextPage() {
        scrollPage++;
        generateSlots(scrollPage);
    }

    public void generateSlots(int page) {


        // Math.ceil(drawerGridControllerEntity.getTotalItems() / 9d)

//        int x = 0;
//
//        for( ItemIdentifier i : drawerGridControllerEntity.mergedStorageContents) {
//            System.out.println(i.getItemStack() + " " + i.getCount() + " Index: " + x);
//
//            x++;
//        }
//
//        System.out.println("---");





        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                int currentIndex = (row * 9) + column;



                ItemIdentifier itemIdentifier =
                        drawerGridControllerEntity.getMergedStorageContents(currentIndex);




                if(!itemIdentifier.getItemStack().equals(ItemStack.EMPTY, false)) {



                    if(page == 1)
                        addSlot( new ViewOnlySlot(itemIdentifier,
                                8 + (column * 18), 18 + (row * 18)));
                    else {


                        int slotToFetch = ((page - 1) * VISIBLE_CONTENT_HEIGHT) + currentIndex;


                        int slotIndex = STARTING_SLOT_INDEX + currentIndex;



                        itemIdentifier =
                                drawerGridControllerEntity.getMergedStorageContents(slotToFetch);


                        if(slots.get(slotIndex) instanceof ViewOnlySlot) {

                            slots.set(slotIndex, new ViewOnlySlot(itemIdentifier,
                                    8 + (column * 18), 18 + (row * 18)));


                        }
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
}
