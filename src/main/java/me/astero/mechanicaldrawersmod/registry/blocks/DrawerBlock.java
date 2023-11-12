package me.astero.mechanicaldrawersmod.registry.blocks;

import com.mojang.logging.LogUtils;
import me.astero.mechanicaldrawersmod.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import org.slf4j.Logger;

public class DrawerBlock extends Block implements EntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();


    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DrawerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

        System.out.println("created!!! " + state);
        return BlockEntityRegistry.DRAWER_BLOCK_ENTITY.get().create(pos, state);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(FACING);

    }

    @Override
    public InteractionResult use(BlockState state, Level level,
                                 BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {



        if(!level.isClientSide()) {


            if(interactionHand == InteractionHand.MAIN_HAND)
                player.sendSystemMessage(Component.literal("HI"));

        }

        return super.use(state, level, pos, player, interactionHand, blockHitResult);
    }
}
