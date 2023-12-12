package me.astero.unifiedstoragemod.networking;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.SendCraftingResultEntityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
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
}
