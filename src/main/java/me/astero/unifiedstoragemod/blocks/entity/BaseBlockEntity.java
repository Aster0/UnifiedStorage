package me.astero.unifiedstoragemod.blocks.entity;

import me.astero.unifiedstoragemod.items.NetworkCardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseBlockEntity extends BlockEntity {

    public BaseBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }


    public abstract void updateNetworkCardItems(ItemStack itemStack, Player player);
    public abstract void actionWhenNetworkTakenOut(Player player);
}
