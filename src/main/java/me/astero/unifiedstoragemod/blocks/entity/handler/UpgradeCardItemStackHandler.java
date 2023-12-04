package me.astero.unifiedstoragemod.blocks.entity.handler;

import me.astero.unifiedstoragemod.blocks.entity.BaseBlockEntity;
import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.items.data.UpgradeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class UpgradeCardItemStackHandler<T extends BaseBlockEntity> extends ItemStackHandler {

    T blockEntity;

    public UpgradeCardItemStackHandler(int size, T blockEntity) {
        super(size);

        this.blockEntity = blockEntity;

    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);



        if(blockEntity instanceof StorageControllerEntity controller) {

            controller.setCraftingEnabled(controller.isUpgradeModuleInserted(UpgradeModule.CRAFTING));


        }

        blockEntity.setChanged();





    }
}
