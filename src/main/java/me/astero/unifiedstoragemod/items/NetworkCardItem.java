package me.astero.unifiedstoragemod.items;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NetworkCardItem extends BaseItem {

    public NetworkCardItem(Properties properties) {
        super(properties);

    }



    private final List<SavedStorageData> savedStorageData = new ArrayList<>();

    public void saveNbt(ItemStack itemStack, String key, String value) {

        CompoundTag nbt = itemStack.getTag();

        if(nbt == null)
            nbt = new CompoundTag();


        final String sValue = ModUtils.serializeBlockPosNbt(value);


        CompoundTag innerNbt = new CompoundTag();
        innerNbt.putString(key, sValue);




        nbt.put(ModUtils.MODID, innerNbt);

        itemStack.setTag(nbt);


    }

    public CustomBlockPosData loadNbt(ItemStack itemStack, String key) {

        CompoundTag nbt = itemStack.getTag();
        System.out.println("NBT " + nbt);



        CustomBlockPosData customBlockPosData = null;

        if(nbt != null) {
            nbt = nbt.getCompound(ModUtils.MODID);

            String[] eValue = nbt.getString(key).split(", ");

            customBlockPosData = ModUtils.convertStringToBlockData(eValue);


        }


        return customBlockPosData;


    }


    private void addStorageData(LazyOptional<IItemHandler> inventory, String blockCoordinate) {

        checkIfStorageExists(ModUtils.convertStringToBlockData(blockCoordinate.split(", ")));

//        System.out.println("test" + savedStorageData.size());
//        SavedStorageData savedStorageData = new SavedStorageData(inventory,
//                ModUtils.convertStringToBlockData(blockCoordinate.split(", ")));
//
//
//        if(!this.savedStorageData.contains(savedStorageData)) {
//            this.savedStorageData.add(savedStorageData);
//        }
//        else {
//            System.out.println("Already saved before");
//        }

    }

    private int test = 0;
    private void checkIfStorageExists(CustomBlockPosData customBlockPosData) {
        // mainly for double chests

        BlockPos blockPos = customBlockPosData.getBlockPos();



        BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(blockPos);


        Block block = Minecraft.getInstance().level.getBlockState(blockPos).getBlock();



        if(blockEntity instanceof ChestBlockEntity chestBlockEntity) {



            System.out.println("CLICKED ON " + chestBlockEntity.getUpdateTag());
        }









    }


    public BlockEntity lookInAllDirections(ChestBlockEntity currentChestBlock, ChestBlockEntity parentChestBlock, boolean nested) {





        ChestType clickedChestType = currentChestBlock.getBlockState().getValue(ChestBlock.TYPE);
        System.out.println(clickedChestType);

//
//
//        System.out.println("current " + clickedChestType);
//        ChestType lookingForType = clickedChestType == ChestType.RIGHT ? ChestType.LEFT : ChestType.RIGHT;
//
//
//        System.out.println(lookingForType);
//
//
//        BlockPos[] directions = {currentChestBlock.getBlockPos().west(),
//                currentChestBlock.getBlockPos().east(),
//                currentChestBlock.getBlockPos().south(),
//                currentChestBlock.getBlockPos().north()};
//
//
//        for(BlockPos pos : directions) {
//
//            BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(pos);
//
//            if(entity instanceof ChestBlockEntity chestBlockEntity) {
//
//
//
//                if(chestBlockEntity == parentChestBlock) {
//
//                    return chestBlockEntity;
//
//                }
//
//                lookInAllDirections(chestBlockEntity, parentChestBlock, false);
//
//            }
//        }



        return null;


    }

    private ChestType getLookingForChestType(ChestBlockEntity chestBlockEntity) {
        ChestType clickedChestType = chestBlockEntity.getBlockState().getValue(ChestBlock.TYPE);

        ChestType lookingForType = clickedChestType == ChestType.RIGHT ? ChestType.LEFT : ChestType.RIGHT;

        return lookingForType;
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {




        if(!level.isClientSide) {

            ItemStack itemStack = player.getItemInHand(interactionHand);
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);


            BlockEntity hitBlockEntity = level.getBlockEntity(blockHitResult.getBlockPos());



            if(hitBlockEntity != null) {

                if(hitBlockEntity
                        .getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {


                    LazyOptional<IItemHandler> inventory = hitBlockEntity
                            .getCapability(ForgeCapabilities.ITEM_HANDLER);

                    addStorageData(inventory,
                            ModUtils.serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()));



                    player.displayClientMessage(Component.translatable("language."
                            + ModUtils.MODID + ".linked_storage"), true);
                }



//
//                if(hitBlockEntity instanceof DrawerGridControllerEntity entity) {
//
//                    for(ItemIdentifier d : entity.mergedStorageContents) {
//                        System.out.println(d.getItemStack());
//                    }
//
//                    saveNbt(itemStack, "grid_pos", entity.getBlockPos().toString());
//                }
//                else if(hitBlockEntity
//                        .getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
//
//                    System.out.println("A storage block");
//
//                    CustomBlockPosData gridPos = loadNbt(itemStack, "grid_pos");
//
//
//                    if(gridPos == null) {
//
//
//
//                        // Send the action bar message to the player
//                        player.displayClientMessage(Component.translatable("language."
//                                        + ModUtils.MODID + ".no_grid_for_chest_selection"), true);
//
//                        return super.use(level, player, interactionHand);
//
//                    }
//
//                    BlockEntity blockEntity = level.getBlockEntity(gridPos.getBlockPos());
//
//
//                    if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {
//
//
//
//                        drawerGridControllerEntity.addChestLocations(
//                                ModUtils.serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()));
//
//
//                    }
//
//
//
//
//
//                }


            }






        }


        return super.use(level, player, interactionHand);



    }




    public List<Component> addShiftText() {

        return ModUtils.
                breakComponentLine(Component.translatable("lore.unifiedstorage.network_card"),
                        0, 0);

    }
}
