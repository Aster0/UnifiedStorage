package me.astero.unifiedstoragemod.blocks;

import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.registry.BlockRegistry;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorageControllerBlock extends BaseBlock implements EntityBlock {



    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty
            STATUS = BooleanProperty.create("status");


    private final String BULLET_POINT = " - ";



    public StorageControllerBlock(Properties properties) {
        super(properties);

        registerDefaultState(this.defaultBlockState().setValue(STATUS, false));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));




    }


    @Override
    public List<Component> addShiftText(ItemStack itemStack) {

        return ModUtils.
                breakComponentLine(Component.translatable("lore.unifiedstorage.storage_controller"));
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

            CompoundTag modTag = tag.getCompound("BlockEntityTag").getCompound(ModUtils.MODID);

            String storageItems = modTag.getString("storage_items");

            String[] items = storageItems.split(";");
            components.add(Component.literal(""));

            components.add(Component.literal(tag.isEmpty() ?
                    "" : Component.translatable("lore." +
                    ModUtils.MODID + ".dropped_storage_controller_header2").getString()));


            for(int i = 0; i < items.length; i++) {

                String item = items[i];



                if(item.startsWith("NET")) {

                    addLore(components, item, Component.translatable("lore." + ModUtils.MODID
                            + ".dropped_storage_controller_header").getString()
                            .replace("%header%", "§eInserted Network: "));
                }
                else if(item.startsWith("VISUAL")) {

                    addLore(components, item, Component.translatable("lore." + ModUtils.MODID
                                    + ".dropped_storage_controller_header").getString()
                            .replace("%header%", "§eInserted Visual Item: "));
                }
                else {
                    if(item.startsWith("UPGRADE")) { // start of upgrade
                        components.add(Component.literal("§eInserted Upgrades: "));
                    }

                    components.add(Component.literal(BULLET_POINT + item.replace("UPGRADE::", "")));

                }


            }


        }

    }

    private List<Component> addLore(List<Component> components, String item, String header) {

        components.add(Component.literal(header));
        components.add(Component.literal(BULLET_POINT + item.split("::")[1]));
        components.add(Component.literal(""));

        return components;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {


        if(!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);





            List<ItemStack> drops = getDrops(state, (ServerLevel) level,
                    pos, null, null, ItemStack.EMPTY);


            ItemStack stack = new ItemStack(state.getBlock());
            ItemStack stackBeforeNbt = stack.copy();


            blockEntity.saveToItem(stack);



            if(stack.getTag() != null) {
                CompoundTag tag = stack.getTag().getCompound("BlockEntityTag").getCompound(ModUtils.MODID);



                ItemStackHandler networkCard = new ItemStackHandler(1);
                networkCard.deserializeNBT(tag.getCompound("network_card"));

                ItemStackHandler visualItem = new ItemStackHandler(1);
                visualItem.deserializeNBT(tag.getCompound("visual_item"));

                ItemStackHandler upgradeItems = new ItemStackHandler(StorageControllerEntity.MAX_UPGRADES);
                upgradeItems.deserializeNBT(tag.getCompound("upgrade_inventory"));


                String storedItems = getStorageContents(new String[]{"NET", "VISUAL"}, networkCard, visualItem);

                boolean hasUpgrades = false;

                String upgrades = "";

                for(int i = 0; i < StorageControllerEntity.MAX_UPGRADES; i++) {

                    if(!upgradeItems.getStackInSlot(i).equals(ItemStack.EMPTY, false)) {

                        if(!hasUpgrades) {
                            hasUpgrades = true;

                            storedItems += "UPGRADE::";
                        }


                        upgrades += ModUtils.capitalizeName(upgradeItems.getStackInSlot(i).getHoverName().getString()) + ";";
                    }
                }

                storedItems += upgrades;

                if(storedItems.length() > 1) {
                    storedItems = storedItems.substring(0, storedItems.length() - 1);

                    tag.putString("storage_items", storedItems);

                }


                if(networkCard.getStackInSlot(0).equals(ItemStack.EMPTY, false) &&
                        visualItem.getStackInSlot(0).equals(ItemStack.EMPTY, false)) {

                    if(!hasUpgrades)
                        stack = stackBeforeNbt;
                }

            }




            popResource(level, pos, stack);



        }


        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }


    private String getStorageContents(String[] names, ItemStackHandler ...inventory) {
        String storedItems = "";


        for(int i = 0; i < inventory.length; i++) {

            ItemStackHandler itemStackHandler = inventory[i];

            if(!itemStackHandler.getStackInSlot(0).equals(ItemStack.EMPTY, false)) {

                storedItems += names[i] + "::"
                        + ModUtils.capitalizeName(itemStackHandler.getStackInSlot(0).getHoverName().getString())
                        + (names[i].equals("VISUAL") ? " x" + itemStackHandler.getStackInSlot(0).getCount() : "")
                        + ";";
            }
        }

        return storedItems;

    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(FACING);
        builder.add(STATUS);


    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {


        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }


    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {

        if(newState.getBlock() instanceof StorageControllerBlock) {

            return; // don't remove the block entity at that position, so the new dyed controller will retain it.
        }

        super.onRemove(oldState, level, pos, newState, isMoving);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(STATUS) ? 12 : 0;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> entityType) {
        return entityType == BlockEntityRegistry.STORAGE_CONTROLLER_BLOCK_ENTITY.get()
                ? StorageControllerEntity::tick : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level,
                                 BlockPos pos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {

        BlockEntity blockEntity = level.getBlockEntity(pos);





        if(!level.isClientSide()) {


            if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {

                if(player instanceof ServerPlayer sPlayer) {


                    if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof DyeItem dye) {



                        DyeColor dyeColor = dye.getDyeColor();

                        RegistryObject<Block> targetColoredBlock =
                                BlockRegistry.STORAGE_CONTROLLER_BLOCK_COLORED.get(dyeColor);



                        if(targetColoredBlock != null) {

                            if(!targetColoredBlock.get().equals(this.defaultBlockState().getBlock())) { // if it's not already dyed with the same color.

                                BlockState oldState = blockEntity.getBlockState();
                                BlockState newState = BlockRegistry.STORAGE_CONTROLLER_BLOCK_COLORED.get(
                                                dyeColor).get().defaultBlockState().setValue(FACING, oldState.getValue(FACING))
                                        .setValue(STATUS, oldState.getValue(STATUS));


                                level.setBlockAndUpdate(pos, newState);

                                if(!player.isCreative())
                                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);



                                return InteractionResult.SUCCESS;
                            }


                        }


                    }


                    sPlayer.openMenu(storageControllerEntity, pos);
                    return InteractionResult.CONSUME;
                }

            }

        }


        return InteractionResult.SUCCESS;
    }






}



