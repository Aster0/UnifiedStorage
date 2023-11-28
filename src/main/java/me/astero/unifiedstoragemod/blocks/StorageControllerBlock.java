package me.astero.unifiedstoragemod.blocks;

import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageControllerBlock extends BaseBlock implements EntityBlock {



    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;




    public StorageControllerBlock(Properties properties) {
        super(properties);

        this.defaultBlockState().setValue(FACING, Direction.NORTH);


    }

    @Override
    public List<Component> addShiftText(ItemStack itemStack) {

        return ModUtils.
                breakComponentLine(Component.translatable("lore.unifiedstorage.storage_controller"));
    }


    @Override
    public MapColor getMapColor(BlockState state, BlockGetter level, BlockPos pos, MapColor defaultColor) {
        return MapColor.COLOR_BLACK;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        return BlockEntityRegistry.STORAGE_CONTROLLER_BLOCK_ENTITY.get().create(pos, state);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, getter, components, flag);

        CompoundTag tag = stack.getTag();

        if(tag != null) {
            components.add(Component.literal(""));
            components.add(Component.literal(stack.getTag().isEmpty() ? "" : "§c§oConfigured"));
        }

    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {


        if(!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);





            List<ItemStack> drops = getDrops(state, (ServerLevel) level,
                    pos, null, null, ItemStack.EMPTY);


            ItemStack stack = drops.get(0);
            ItemStack stackBeforeNbt = stack.copy();


            blockEntity.saveToItem(stack);



            if(stack.getTag() != null) {
                CompoundTag tag = stack.getTag().getCompound("BlockEntityTag").getCompound(ModUtils.MODID);



                ItemStackHandler networkCard = new ItemStackHandler(1);
                networkCard.deserializeNBT(tag.getCompound("network_card"));

                ItemStackHandler visualItem = new ItemStackHandler(1);
                visualItem.deserializeNBT(tag.getCompound("visual_item"));


                if(networkCard.getStackInSlot(0).equals(ItemStack.EMPTY, false) &&
                        visualItem.getStackInSlot(0).equals(ItemStack.EMPTY, false)) {

                    stack = stackBeforeNbt;
                }

            }



            // temporary - TODO: to fix later when we include upgrade slots


            popResource(level, pos, stack);



        }


        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
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
            if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {

                if(player instanceof ServerPlayer sPlayer) {
                    sPlayer.openMenu(storageControllerEntity, pos);
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
