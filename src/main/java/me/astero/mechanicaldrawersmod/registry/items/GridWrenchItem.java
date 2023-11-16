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

            try {
                int x = Integer.parseInt(eValue[0].substring(2));
                int y = Integer.parseInt(eValue[1].substring(2));
                int z = Integer.parseInt(eValue[2].substring(2));


                customBlockPosData = new CustomBlockPosData(x, y, z);
            }
            catch(NumberFormatException e) {
                throw new NumberFormatException("Check the inputted pos if it's numbers.");
            }


        }







        return customBlockPosData;




    }


    private String serializeBlockPosNbt(String value) {

        String eValue = value.substring(value.indexOf("x"), value.length() - 1);
        // Edited value to e.g., "x=2, y=-60, z=36"

        System.out.println(eValue);

        return eValue;

    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {


        if(!level.isClientSide) {

            ItemStack itemStack = player.getItemInHand(interactionHand);
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);


            AsteroLogger.info("BLOCK HIT!!");
            System.out.println(loadNbt(itemStack, "grid_pos"));





            BlockEntity hitBlockEntity = level.getBlockEntity(blockHitResult.getBlockPos());



            if(hitBlockEntity != null) {



                if(hitBlockEntity instanceof DrawerGridControllerEntity entity) {

                    System.out.println("Drawer");

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

                        System.out.println("Set " + drawerGridControllerEntity.getCapability(
                                ForgeCapabilities.ITEM_HANDLER) );
                        drawerGridControllerEntity.setChestInventory(drawerGridControllerEntity.getCapability(
                                ForgeCapabilities.ITEM_HANDLER));


                    }





                }


            }






        }


        return super.use(level, player, interactionHand);



    }


}
