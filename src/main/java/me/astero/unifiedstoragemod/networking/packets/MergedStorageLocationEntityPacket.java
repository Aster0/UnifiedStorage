package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.networking.ClientPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.*;

public class MergedStorageLocationEntityPacket implements EntityPacket {

    public List<ItemIdentifier> data;
    public BlockPos blockPos;

    public boolean targetSelf = false, regenerateMenu;
    public UUID senderUUID;


    public MergedStorageLocationEntityPacket(List<ItemIdentifier> data, BlockPos blockPos, boolean targetSelf, UUID senderUUID, boolean regenerateMenu) {
        this.data = data;
        this.blockPos = blockPos;
        this.targetSelf = targetSelf;
        this.senderUUID = senderUUID;
        this.regenerateMenu = regenerateMenu;



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
        this.targetSelf = buffer.readBoolean();
        this.senderUUID = buffer.readUUID();
        this.regenerateMenu = buffer.readBoolean();





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
        buffer.writeBoolean(this.targetSelf);
        buffer.writeUUID(this.senderUUID);
        buffer.writeBoolean(this.regenerateMenu);

    }







    public boolean handle(CustomPayloadEvent.Context context) {


        return ClientPacketHandler.handleMergeStorageLocation(this);
    }
}
