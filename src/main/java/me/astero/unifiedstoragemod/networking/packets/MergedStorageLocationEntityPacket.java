package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergedStorageLocationEntityPacket implements EntityPacket {

    List<ItemIdentifier> data;
    BlockPos blockPos;


    public MergedStorageLocationEntityPacket(List<ItemIdentifier> data, BlockPos blockPos) {
        this.data = data;
        this.blockPos = blockPos;



    }

    public MergedStorageLocationEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        List<ItemIdentifier> list = new ArrayList<>();
        int size = buffer.readInt();

        for (int i = 0; i < size; i++) {

            int size2 = buffer.readInt();
            Map<String, Integer> locations = new HashMap<>();

            for(int x = 0; x < size2; x++) {
                locations.put(buffer.readUtf(), buffer.readInt());
            }

            ItemIdentifier itemIdentifier = new ItemIdentifier(buffer.readItem(),
                    buffer.readInt(), locations);

            list.add(itemIdentifier);
        }


        this.data = list;
        this.blockPos = buffer.readBlockPos();





    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeInt(this.data.size());

        for (ItemIdentifier item : this.data) {

            buffer.writeInt(item.getLocations().size());
            for(String key : item.getLocations().keySet()) {
                buffer.writeUtf(key);
                buffer.writeInt(item.getLocations().get(key));
            }

            buffer.writeItemStack(item.getItemStack(), false);
            buffer.writeInt(item.getCount());

        }

        buffer.writeBlockPos(this.blockPos);

    }







    public static void handle(MergedStorageLocationEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (context.isClientSide()) {



                BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(packet.blockPos);

                if(blockEntity instanceof StorageControllerEntity storageControllerEntity) {
                    storageControllerEntity.setMergedStorageContents(packet.data);





                    Player player = Minecraft.getInstance().player;

                    if(player.containerMenu instanceof StorageControllerMenu menu) {


                        menu.createBlockEntityInventory();
                    }
                }



            }

        });

        context.setPacketHandled(true);
    }
}
