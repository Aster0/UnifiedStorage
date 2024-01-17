package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UpdateStorageDisabledEntityPacket implements EntityPacket {


    boolean disabled;
    BlockPos blockPos;





    public UpdateStorageDisabledEntityPacket(boolean disabled, BlockPos blockPos) {

        this.disabled = disabled;
        this.blockPos = blockPos;

    }

    public UpdateStorageDisabledEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.disabled = buffer.readBoolean();
        this.blockPos = buffer.readBlockPos();


    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeBoolean(this.disabled);
        buffer.writeBlockPos(this.blockPos);

    }







    public static void handle(UpdateStorageDisabledEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (context.isClientSide()) {


                BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(packet.blockPos);

                if(blockEntity instanceof StorageControllerEntity entity) {


                    entity.setDisabled(packet.disabled);




                }




            }

        });

        context.setPacketHandled(true);
    }
}
