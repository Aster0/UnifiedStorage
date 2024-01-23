package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UpdateQueueToRemoveItemEntityPacket implements EntityPacket {







    public UpdateQueueToRemoveItemEntityPacket() {



    }



    public UpdateQueueToRemoveItemEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here





    }

    @Override
    public void encode(FriendlyByteBuf buffer) {




    }







    public static void handle(UpdateQueueToRemoveItemEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();



                if(serverPlayer.containerMenu instanceof StorageControllerMenu menu) {



                    menu.getStorageControllerEntity().queueToRemoveItems.clear();




                }









            }

        });

        context.setPacketHandled(true);
    }
}
