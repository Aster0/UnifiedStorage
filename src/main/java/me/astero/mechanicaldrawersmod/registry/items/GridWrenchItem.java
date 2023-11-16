package me.astero.mechanicaldrawersmod.registry.items;

import me.astero.mechanicaldrawersmod.registry.blocks.entity.DrawerGridControllerEntity;
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


public class GridWrenchItem extends Item {

    public GridWrenchItem(Properties properties) {
        super(properties);
    }


    public void saveNbt(ItemStack itemStack, String key, String value) {

        CompoundTag nbt = itemStack.getTag();

        if(nbt == null)
            nbt = new CompoundTag();




        CompoundTag innerNbt = new CompoundTag();
        innerNbt.putString(key, value);




        nbt.put(ModUtils.MODID, innerNbt);

        itemStack.setTag(nbt);


    }

    public String loadNbt(ItemStack itemStack, String key) {

        CompoundTag nbt = itemStack.getTag();
        System.out.println("NBT " + nbt);

        if(nbt != null)
            nbt = nbt.getCompound(ModUtils.MODID);

        return nbt == null ? null : nbt.getString(key);




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

                System.out.println(hitBlockEntity);
                if(hitBlockEntity instanceof DrawerGridControllerEntity entity) {

                    System.out.println("Drawer");

                    saveNbt(itemStack, "grid_pos", entity.getBlockPos().toString());
                }
                else if(level.getBlockEntity(blockHitResult.getBlockPos()).getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {

                    System.out.println("A storage block");

                    String gridPos = loadNbt(itemStack, "grid_pos");

                    // TODO: Deserialize and serialize NBT data.


                    if(gridPos == null) {



                        // Send the action bar message to the player
                        player.displayClientMessage(Component.literal("Please select a [Grid Controller] before continuing.")
                                .withStyle(ChatFormatting.RED), true);

                        return super.use(level, player, interactionHand);

                    }
                }


            }






        }


        return super.use(level, player, interactionHand);



    }


}
