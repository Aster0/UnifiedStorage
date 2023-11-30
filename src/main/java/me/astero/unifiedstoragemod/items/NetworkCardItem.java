package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeTier;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class NetworkCardItem extends NetworkItem {


    public NetworkCardItem(Properties properties) {
        super(properties, "Storages", Component.translatable("lore.unifiedstorage.network_card"));


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



                    if(storageLocations.get(getKey(itemStack)) == null)
                        loadNbt(itemStack);




                    String language = "language."
                                    + ModUtils.MODID + ".unlinked_storage";


                    if(addStorageData(ModUtils.serializeBlockPosNbt(hitBlockEntity.getBlockPos().toString()),
                            itemStack)) {
                        language = "language."
                                + ModUtils.MODID + ".linked_storage";
                    }

                    player.displayClientMessage(Component.translatable(language), true);
                }

            }

        }


        return super.use(level, player, interactionHand);



    }


}
