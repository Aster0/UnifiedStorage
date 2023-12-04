package me.astero.unifiedstoragemod.blocks.entity.handler;

import me.astero.unifiedstoragemod.blocks.entity.BaseBlockEntity;
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

        ItemStack slotInStack = this.getStackInSlot(slot);

        if(slotInStack.equals(ItemStack.EMPTY,false)) {
            blockEntity.actionWhenNetworkTakenOut(blockEntity.getLevel().getPlayerByUUID(
                    Minecraft.getInstance().player.getUUID()));
            blockEntity.setDisabled(true);
            return;
        }



        blockEntity.setDisabled(false);

        blockEntity.updateNetworkCardItems(this.getStackInSlot(slot), blockEntity.getLevel().getPlayerByUUID(
                Minecraft.getInstance().player.getUUID()
        ));




    }
}
