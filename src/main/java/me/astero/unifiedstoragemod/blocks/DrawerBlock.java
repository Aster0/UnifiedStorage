package me.astero.unifiedstoragemod.blocks;

import me.astero.unifiedstoragemod.blocks.entity.DrawerBlockEntity;
import me.astero.unifiedstoragemod.blocks.entity.handler.DrawerItemStackHandler;
import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.utils.AsteroLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DrawerBlock extends Block implements EntityBlock {


    public  int maxStack = 64;

    private final int size = 1;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;




    public DrawerBlock(Properties properties, int maxStack) {
        super(properties);

        this.maxStack = maxStack;
        this.defaultBlockState().setValue(FACING, Direction.NORTH);

    }



    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {


        return BlockEntityRegistry.DRAWER_BLOCK_ENTITY.get().create(pos, state);
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
    public void attack(BlockState p_60499_, Level p_60500_, BlockPos p_60501_, Player p_60502_) {

        super.attack(p_60499_, p_60500_, p_60501_, p_60502_);





    }

    @Override
    public InteractionResult use(BlockState state, Level level,
                                 BlockPos pos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {


        // right click to deposit into drawer
        AsteroLogger.info(String.valueOf(player.isCrouching()));
        if(!level.isClientSide()) {


            if(interactionHand == InteractionHand.MAIN_HAND) {

                if(level.getBlockEntity(pos) instanceof DrawerBlockEntity drawerBlockEntity) {


                    ItemStack itemStackInHand = player.getMainHandItem();
                    DrawerItemStackHandler drawerInventory = drawerBlockEntity.getInventory();
                    ItemStack itemStackInDrawer = drawerInventory.getStackInSlot(0);
                    AsteroLogger.info(drawerInventory.getStackInSlot(0).toString());


                    if(!itemStackInHand.isEmpty()) { // there's something in the player's hands




                        if(itemStackInHand.getItem().equals(itemStackInDrawer.getItem())
                                || itemStackInDrawer.isEmpty()) {
                            // if item in drawer matches what player wants to put in or it's empty

                            int toInsertValue = maxStack - itemStackInDrawer.getCount();
                            int insertableAmount = toInsertValue;

                            if(toInsertValue > itemStackInHand.getCount()) {
                                insertableAmount = itemStackInHand.getCount();
                            }


                            return drawerBlockEntity.addItemsToDrawer(itemStackInHand,
                                    insertableAmount); // consumed to the drawer.

                        }






                    }


                }

            }


        }


        return InteractionResult.SUCCESS;
    }




    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state0, boolean bool) {
        super.onPlace(state, level, pos, state0, bool);

        BlockEntity blockEntity = level.getBlockEntity(pos);


        if(blockEntity instanceof DrawerBlockEntity drawerBlockEntity) {

            drawerBlockEntity.updateDrawerMaxStack(maxStack);
            // set size
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if((player.isCreative() && !player.isCrouching())
                || (player.isCreative() && !player.getMainHandItem().isEmpty())) { // if player is in creative and not crouching,
            // don't destroy block

            return false;
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);


    }
}
