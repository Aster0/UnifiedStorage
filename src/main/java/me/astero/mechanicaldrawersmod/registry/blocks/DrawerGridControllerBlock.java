package me.astero.mechanicaldrawersmod.registry.blocks;

import me.astero.mechanicaldrawersmod.registry.BlockEntityRegistry;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerBlockEntity;
import me.astero.mechanicaldrawersmod.registry.blocks.entity.handler.DrawerItemStackHandler;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DrawerGridControllerBlock extends Block implements EntityBlock {



    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;




    public DrawerGridControllerBlock(Properties properties) {
        super(properties);

        this.defaultBlockState().setValue(FACING, Direction.NORTH);

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


        return InteractionResult.FAIL;
    }




    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state0, boolean bool) {
        super.onPlace(state, level, pos, state0, bool);


    }


}
