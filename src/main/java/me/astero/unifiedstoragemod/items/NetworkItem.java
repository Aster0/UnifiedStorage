package me.astero.unifiedstoragemod.items;


import me.astero.unifiedstoragemod.items.data.CustomBlockPosData;
import me.astero.unifiedstoragemod.items.data.SavedStorageData;
import me.astero.unifiedstoragemod.items.data.UpgradeTier;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// any items that can connect to a network should extend this.
public class NetworkItem extends BaseItem {


    private String linkedBlock;
    private MutableComponent shiftText;
    public NetworkItem(Properties properties, String linkedBlock, MutableComponent shiftText) {
        super(properties);


        this.linkedBlock = linkedBlock;
        this.shiftText = shiftText;
    }





    protected Map<String, List<SavedStorageData>> storageLocations = new HashMap<>();


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


    protected boolean addStorageData(String blockCoordinate, ItemStack itemStack) {



        SavedStorageData savedStorageData = new SavedStorageData(
                ModUtils.convertStringToBlockData(blockCoordinate.split(", ")));


        boolean added = false;



        if(saveToStorage(itemStack, savedStorageData)) {

            added = true;
        }
        else {



            removeFromStorage(itemStack, savedStorageData);

        }

        saveNbt(itemStack);

        return added;

    }


    protected boolean saveToStorage(ItemStack itemStack, SavedStorageData savedStorageData) {

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


        savedStorageDataList.removeIf((value) ->
                value.getCustomBlockPosData()
                        .equals(savedStorageData.getCustomBlockPosData()));



    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);

        if(storageLocations.get(getKey(stack)) == null)
            loadNbt(stack);

        String lore = Component.translatable("lore." + ModUtils.MODID + ".network_card_storages").getString();

        List<SavedStorageData> savedStorageData = storageLocations.get(getKey(stack));

        int amountOfStorages = 0;
        if(savedStorageData != null) {
            amountOfStorages = savedStorageData.size();
        }

        components.addAll(ModUtils.
                        breakComponentLine(
                                Component.literal(lore.replace("%amount%", String.valueOf(amountOfStorages))
                                        .replace("%block%", linkedBlock))));

    }

    public List<Component> addShiftText(ItemStack itemStack) {


        return ModUtils.
                breakComponentLine(shiftText);

    }
}
