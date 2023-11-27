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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class NetworkCardItem extends BaseItem {


    public NetworkCardItem(Properties properties) {
        super(properties);


    }





    private Map<String, List<SavedStorageData>> storageLocations = new HashMap<>();

    private boolean nbtLoaded = false;

    public List<SavedStorageData> getStorageLocations(ItemStack itemStack) {

        return storageLocations.get(getKey(itemStack));
    }

    public String getKey(ItemStack itemStack) {
        return String.valueOf(itemStack.hashCode());
    }

    public void saveNbt(ItemStack itemStack) {


        CompoundTag nbt = itemStack.getTag();

        if(nbt == null)
            nbt = new CompoundTag();


        CompoundTag innerNbt = new CompoundTag();

        List<SavedStorageData> savedStorageData = storageLocations.get(getKey(itemStack));




        for(int i = 0; i < savedStorageData.size(); i++) {
            innerNbt.putString("storage" + i, savedStorageData.get(i)
                    .getCustomBlockPosData().toString());

            System.out.println("storage" + i + " "  + savedStorageData.get(i)
                    .getCustomBlockPosData().toString());
        }

        nbt.put(ModUtils.MODID, innerNbt);


        itemStack.setTag(nbt);

    }

    public void loadNbt(ItemStack itemStack) {


        CompoundTag nbt = itemStack.getTag();

        //storageLocations.clear();


        if(nbt == null) {
            return;
        }


        nbt = nbt.getCompound(ModUtils.MODID);



        for(int i = 0; i < UpgradeTier.MAX.getAmount(); i++) {



            String rawPos = nbt.getString("storage" + i);


            if(rawPos.length() > 0) {

                rawPos = ModUtils.serializeBlockPosNbt(rawPos);

                CustomBlockPosData customBlockPosData =
                        ModUtils.convertStringToBlockData(rawPos.split(", "));

                SavedStorageData savedStorageData = new SavedStorageData(customBlockPosData);


                saveToStorage(itemStack, savedStorageData);

                continue;
            }

            return;

        }



    }


    private boolean addStorageData(String blockCoordinate, ItemStack itemStack) {



        SavedStorageData savedStorageData = new SavedStorageData(
                ModUtils.convertStringToBlockData(blockCoordinate.split(", ")));


        boolean added = false;



        if(saveToStorage(itemStack, savedStorageData)) {



            saveNbt(itemStack);


            added = true;
        }
        else {


            System.out.println("removed");
            removeFromStorage(itemStack, savedStorageData);

        }

        return added;

    }


    private boolean saveToStorage(ItemStack itemStack, SavedStorageData savedStorageData) {

        List<SavedStorageData> savedStorageDataList = storageLocations.computeIfAbsent(getKey(itemStack),
                (s) -> new ArrayList<>());


        if(!savedStorageDataList.contains(savedStorageData)) {
            savedStorageDataList.add(savedStorageData);

            return true;
        }


        return false;

    }

    private void removeFromStorage(ItemStack itemStack, SavedStorageData savedStorageData) {


        List<SavedStorageData> savedStorageDataList = storageLocations.get(getKey(itemStack));

        System.out.println(savedStorageDataList);

        savedStorageDataList.removeIf((value) ->
                value.getCustomBlockPosData()
                        .equals(savedStorageData.getCustomBlockPosData()));
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




    public List<Component> addShiftText(ItemStack itemStack) {

        if(storageLocations.get(getKey(itemStack)) == null)
            loadNbt(itemStack);

        List<SavedStorageData> savedStorageData = storageLocations.get(getKey(itemStack));

        int amountOfStorages = 0;
        if(savedStorageData != null) {
            amountOfStorages = savedStorageData.size();
        }

        return ModUtils.
                breakComponentLine(Component.translatable("lore.unifiedstorage.network_card"),
                        amountOfStorages, 0);

    }
}
