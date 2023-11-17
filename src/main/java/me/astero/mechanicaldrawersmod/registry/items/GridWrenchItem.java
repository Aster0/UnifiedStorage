package me.astero.mechanicaldrawersmod.registry.items;

import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerGridControllerEntity;
import me.astero.mechanicaldrawersmod.registry.items.data.CustomBlockPosData;
import me.astero.mechanicaldrawersmod.utils.AsteroLogger;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;


public class GridWrenchItem extends Item {

    public GridWrenchItem(Properties properties) {
        super(properties);
    }


    public void saveNbt(ItemStack itemStack, String key, String value) {

        CompoundTag nbt = itemStack.getTag();

        if(nbt == null)
            nbt = new CompoundTag();


        final String sValue = serializeBlockPosNbt(value);


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



    private String serializeBlockPosNbt(String value) {

        String eValue = value.substring(value.indexOf("x"), value.length() - 1);
        // Edited value to e.g., "x=2, y=-60, z=36"


        return eValue;

    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {



        if(!level.isClientSide) {

            ItemStack itemStack = player.getItemInHand(interactionHand);
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);








            BlockEntity hitBlockEntity = level.getBlockEntity(blockHitResult.getBlockPos());



            if(hitBlockEntity != null) {



                if(hitBlockEntity instanceof DrawerGridControllerEntity entity) {

                    System.out.println("Drawer " + entity.chestLocations.get(0));

                    saveNbt(itemStack, "grid_pos", entity.getBlockPos().toString());
                }
                else if(hitBlockEntity
                        .getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {

                    System.out.println("A storage block");

                    CustomBlockPosData gridPos = loadNbt(itemStack, "grid_pos");


                    if(gridPos == null) {



                        // Send the action bar message to the player
                        player.displayClientMessage(Component.translatable("language."
                                        + ModUtils.MODID + ".no_grid_for_chest_selection")
                                .withStyle(ChatFormatting.RED), true);

                        return super.use(level, player, interactionHand);

                    }

                    BlockEntity blockEntity = level.getBlockEntity(gridPos.getBlockPos());


                    if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {



                        drawerGridControllerEntity.addChestLocations(
                                serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()));


                    }





                }


            }






        }


        return super.use(level, player, interactionHand);



    }


}
