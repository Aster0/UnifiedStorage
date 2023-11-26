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

    public void saveNbt(ItemStack itemStack, String key) {


        CompoundTag nbt = itemStack.getTag();

        if(nbt == null)
            nbt = new CompoundTag();


        CompoundTag innerNbt = new CompoundTag();


        for(int i = 0; i < savedStorageData.size(); i++) {
            innerNbt.putString("chest" + i, this.savedStorageData.get(i)
                    .getCustomBlockPosData().toString());

            System.out.println("chest" + i + " "  +this.savedStorageData.get(i)
                    .getCustomBlockPosData().toString());
        }

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


    private boolean addStorageData(String blockCoordinate, ItemStack itemStack) {



        SavedStorageData savedStorageData = new SavedStorageData(
                ModUtils.convertStringToBlockData(blockCoordinate.split(", ")));


        boolean added = false;

        if(!this.savedStorageData.contains(savedStorageData)) {
            this.savedStorageData.add(savedStorageData);




            saveNbt(itemStack, "storages");


            added = true;
        }
        else {
            System.out.println("removed");
            this.savedStorageData.removeIf((value) ->
                    value.getCustomBlockPosData()
                            .equals(savedStorageData.getCustomBlockPosData()));

        }

        return added;

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




    public List<Component> addShiftText() {

        return ModUtils.
                breakComponentLine(Component.translatable("lore.unifiedstorage.network_card"),
                        savedStorageData.size(), 0);

    }
}
