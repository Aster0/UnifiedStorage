package me.astero.mechanicaldrawersmod.registry.menu;

import me.astero.mechanicaldrawersmod.registry.BlockRegistry;
import me.astero.mechanicaldrawersmod.registry.MenuRegistry;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerGridControllerEntity;
import me.astero.mechanicaldrawersmod.registry.menu.data.ViewOnlySlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

public class GridControllerMenu extends AbstractContainerMenu {


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


        for (int row = 0; row < 3; row++) {
                for (int column = 0; column < 9; column++) {
                    addSlot(addSlot(new ViewOnlySlot(
                            8 + (column * 18), 18 + (row * 18))));
                }
        }











    }



    private void createPlayerInventory(Inventory pInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(pInventory, 9 + column + (row * 9),
                        8 + (column * 18),
                        84 + (row * 18)));
            }
        }
    }

    private void createPlayerHotbar(Inventory pInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(pInventory, column, 8 + (column * 18),
                    142));
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
