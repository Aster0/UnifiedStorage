package me.astero.unifiedstoragemod.items;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;


public class GridWrenchItem extends Item {

    public GridWrenchItem(Properties properties) {
        super(properties);
    }


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




        CustomBlockPosData customBlockPosData = null;

        if(nbt != null) {
            nbt = nbt.getCompound(ModUtils.MODID);

            String[] eValue = nbt.getString(key).split(", ");

            customBlockPosData = ModUtils.convertStringToBlockData(eValue);


        }







        return customBlockPosData;




    }







    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {



        if(!level.isClientSide) {

            ItemStack itemStack = player.getItemInHand(interactionHand);
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);









            BlockEntity hitBlockEntity = level.getBlockEntity(blockHitResult.getBlockPos());



            if(hitBlockEntity != null) {



                if(hitBlockEntity instanceof StorageControllerEntity entity) {

                    for(ItemIdentifier d : entity.mergedStorageContents) {

                    }

                    saveNbt(itemStack, "grid_pos", entity.getBlockPos().toString());
                }
                else if(hitBlockEntity
                        .getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {



                    CustomBlockPosData gridPos = loadNbt(itemStack, "grid_pos");


                    if(gridPos == null) {



                        // Send the action bar message to the player
                        player.displayClientMessage(Component.translatable("language."
                                        + ModUtils.MODID + ".no_grid_for_chest_selection"), true);

                        return super.use(level, player, interactionHand);

                    }

                    BlockEntity blockEntity = level.getBlockEntity(gridPos.getBlockPos());


                    if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {



                        storageControllerEntity.addChestLocations(
                                ModUtils.serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()));


                    }





                }


            }






        }


        return super.use(level, player, interactionHand);



    }


}
