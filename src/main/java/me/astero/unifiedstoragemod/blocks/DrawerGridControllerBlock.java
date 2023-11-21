package me.astero.unifiedstoragemod.blocks;

import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DrawerGridControllerBlock extends Block implements EntityBlock {



    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;




    public DrawerGridControllerBlock(Properties properties) {
        super(properties);

        this.defaultBlockState().setValue(FACING, Direction.NORTH);


    }


    @Override
    public MapColor getMapColor(BlockState state, BlockGetter level, BlockPos pos, MapColor defaultColor) {
        return MapColor.COLOR_BLACK;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return BlockEntityRegistry.DRAWER_CONTROLLER_BLOCK_ENTITY.get().create(pos, state);
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
    public InteractionResult use(BlockState state, Level level,
                                 BlockPos pos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {



        BlockEntity blockEntity = level.getBlockEntity(pos);

        if(!level.isClientSide()) {
            if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {

                if(player instanceof ServerPlayer sPlayer) {
                    sPlayer.openMenu(drawerGridControllerEntity, pos);
                    return InteractionResult.SUCCESS;
                }

            }

        }



        return InteractionResult.SUCCESS;
    }




    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state0, boolean bool) {
        super.onPlace(state, level, pos, state0, bool);


    }


}
