package me.astero.mechanicaldrawersmod.registry.blocks.entity;

import me.astero.mechanicaldrawersmod.registry.BlockEntityRegistry;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.handler.DrawerItemStackHandler;
import me.astero.mechanicaldrawersmod.registry.items.data.CustomBlockPosData;
import me.astero.mechanicaldrawersmod.registry.menu.GridControllerMenu;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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
import java.util.Arrays;
import java.util.List;

public class DrawerGridControllerEntity extends BlockEntity implements MenuProvider {




    private int maxChests = 6;
    private static final Component MENU_TITLE = Component.translatable("container."
            + ModUtils.MODID + ".grid_controller_menu_title");

    public List<String> chestLocations = new ArrayList<>();;
    private List<CustomBlockPosData> editedChestLocations = new ArrayList<>();
    private ItemStackHandler inventory = new ItemStackHandler(27);

    private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> this.inventory);

    public DrawerGridControllerEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.DRAWER_CONTROLLER_BLOCK_ENTITY.get(), pos, state);

        System.out.println("created " + this);
    }


    public void addChestLocations(String location) {

        if(!chestLocations.contains(location)) {
            System.out.println("not exist");
            chestLocations.add(location);
            this.setChanged();
        }



    }




    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        CompoundTag modNbt = nbt.getCompound(ModUtils.MODID);


        editedChestLocations.clear();

        for(int i = 0; i < maxChests; i++) {


            String rawPos = modNbt.getString("chest" + i);

            System.out.println(rawPos.length() + " RAW POS");

            if(rawPos != null && rawPos.length() > 0) {
                String[] pos = rawPos.split(", ");


                chestLocations.add(rawPos);
                CustomBlockPosData customBlockPosData = ModUtils.convertStringToBlockData(pos);
                editedChestLocations.add(customBlockPosData);

            }


        }


        System.out.println("LOADINGNBT " + nbt.getCompound(ModUtils.MODID));





    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);


        CompoundTag modNbt = new CompoundTag();


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

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public LazyOptional<ItemStackHandler> getOptional() {
        return optional;
    }


    @Override
    public Component getDisplayName() {
        return MENU_TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pControllerId, Inventory pInventory, Player player) {
        return new GridControllerMenu(pControllerId, pInventory, this);
    }
}
