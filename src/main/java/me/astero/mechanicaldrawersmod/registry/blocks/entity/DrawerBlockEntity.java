package me.astero.mechanicaldrawersmod.registry.blocks.entity;

import me.astero.mechanicaldrawersmod.registry.BlockEntityRegistry;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.handler.DrawerItemStackHandler;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class DrawerBlockEntity extends BlockEntity {


    private int maxStack = 64;

    private DrawerItemStackHandler inventory = new DrawerItemStackHandler(this, 1);

    private final LazyOptional<DrawerItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    public DrawerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DRAWER_BLOCK_ENTITY.get(), pos, state);
    }



    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        CompoundTag modNbt = nbt.getCompound(ModUtils.MODID);

        this.inventory.deserializeNBT(modNbt.getCompound("drawer_items"));

        maxStack = modNbt.getInt("drawer_max_stack");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);


        CompoundTag modNbt = new CompoundTag();
        modNbt.put("drawer_items", this.inventory.serializeNBT());
        modNbt.putInt("drawer_max_stack", maxStack);



        nbt.put(ModUtils.MODID, modNbt);
    }

    public void updateDrawerMaxStack(int size) {
        maxStack = size;
        this.setChanged();
    }

    public InteractionResult addItemsToDrawer(ItemStack itemStackInHand, int size) {

        ItemStack itemStackInDrawer = this.getInventory().getStackInSlot(0);
        AsteroLogger.info("blog");
        if(itemStackInDrawer.getCount() >= maxStack) return InteractionResult.FAIL;

        AsteroLogger.info("log");

        itemStackInDrawer.setCount(itemStackInDrawer.getCount() + size);
        this.setChanged();

        if(itemStackInDrawer.isEmpty()) {

            ItemStack toInsert = itemStackInHand.copy();

            toInsert.setCount(size);
            this.getInventory().insertItem(0, toInsert, false);
        }



        AsteroLogger.info("shrink");
        itemStackInHand.shrink(size);

        return InteractionResult.CONSUME;
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

    public DrawerItemStackHandler getInventory() {
        return inventory;
    }

    public LazyOptional<DrawerItemStackHandler> getOptional() {
        return optional;
    }

    public int getMaxStack() {
        return maxStack;
    }
}
