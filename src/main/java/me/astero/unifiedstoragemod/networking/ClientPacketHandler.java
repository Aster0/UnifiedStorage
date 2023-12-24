package me.astero.unifiedstoragemod.networking;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.packets.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClientPacketHandler {

    public static boolean handleMergeStorageLocation(MergedStorageLocationEntityPacket packet) {

        Minecraft mc = Minecraft.getInstance();

        mc.execute(() -> {
            BlockEntity blockEntity = mc.level.getBlockEntity(packet.blockPos);

            if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {
                storageControllerEntity.setMergedStorageContents(packet.data);


                Player player = mc.player;

                if(player.containerMenu instanceof StorageControllerMenu menu) {


                    menu.createBlockEntityInventory();
                }
            }
        });

        return true;

    }


    public static boolean sendCraftingResult(SendCraftingResultEntityPacket packet) {

        Minecraft mc = Minecraft.getInstance();

        mc.execute(() -> {
            Player player = mc.player;


            if(player.containerMenu instanceof StorageControllerMenu menu) {


                menu.changeCraftingResultSlot(packet.itemStack);





            }
        });

        return true;
    }

    public static boolean updateAllCraftingSlotsClient(UpdateAllCraftingSlotsClientEntityPacket packet) {

        Minecraft mc = Minecraft.getInstance();

        mc.execute(() -> {
            Player player = mc.player;


            if(packet.playerUUID.equals(player.getUUID()))
                return;



            AbstractContainerMenu abstractMenu = player.containerMenu;



            if(abstractMenu instanceof StorageControllerMenu menu) {


                if(!menu.getStorageControllerEntity().getBlockPos().equals(packet.blockPos)) { // different menu
                    return;
                }

                boolean needsUpdate = false;

                for(int i = 0; i < 9; i++) {

                    ItemStack clientItem = menu.craftSlots.getItem(i);
                    ItemStack updatedItem = packet.items.get(i);
                    if(!clientItem.equals(updatedItem, false)) {
                        needsUpdate = true;
                    }
                }

                if(needsUpdate) {
                    ModNetwork.sendToServer(new UpdateAllCraftingSlotsServerEntityPacket(packet.items, packet.blockPos));
                }


            }


        });

        return true;
    }
}
