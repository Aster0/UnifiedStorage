package me.astero.unifiedstoragemod.blocks;

import me.astero.unifiedstoragemod.menu.enums.StorageDrawerType;
import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageDrawerBlock extends BaseBlock {

    private StorageDrawerType storageDrawerType;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public StorageDrawerBlock(Properties properties, StorageDrawerType storageDrawerType) {
        super(properties);

        this.storageDrawerType = storageDrawerType;
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(FACING);

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {


        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public List<Component> addShiftText(ItemStack itemStack) {
        return null;
    }

    public StorageDrawerType getStorageDrawerType() {
        return storageDrawerType;
    }

//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//        return BlockEntityRegistry.STORAGE_CONTROLLER_BLOCK_ENTITY.get().create(pos, state);
//    }

    @Override
    public InteractionResult use(BlockState blockState, Level level,
                                 BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {



        return super.use(blockState, level, blockPos, player, hand, result);
    }


}
