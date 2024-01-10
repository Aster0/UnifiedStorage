package me.astero.unifiedstoragemod.blocks.entity.handler;

import me.astero.unifiedstoragemod.blocks.StorageControllerBlock;
import me.astero.unifiedstoragemod.blocks.entity.BaseBlockEntity;
import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class NetworkCardItemStackHandler<T extends BaseBlockEntity> extends ItemStackHandler {

    T blockEntity;

    public NetworkCardItemStackHandler(T blockEntity) {
        super(1);

        this.blockEntity = blockEntity;

    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);


        blockEntity.setChanged();


    }

}
