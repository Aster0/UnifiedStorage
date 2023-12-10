package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class TakeOutFromStorageInventoryEntityPacket implements EntityPacket {


    ItemStack itemStack;
    boolean take;
    int value, slotIndex;

    boolean quickMove;





    public TakeOutFromStorageInventoryEntityPacket(ItemStack itemStack, boolean take, int value, boolean quickMove, int slotIndex) {

        this.itemStack = itemStack;
        this.take = take;
        this.value = value;
        this.quickMove = quickMove;
        this.slotIndex = slotIndex;


    }

    public TakeOutFromStorageInventoryEntityPacket(ItemStack itemStack, boolean take, int value, boolean quickMove) {

        this.itemStack = itemStack;
        this.take = take;
        this.value = value;
        this.quickMove = quickMove;



    }

    public TakeOutFromStorageInventoryEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.itemStack = buffer.readItem();
        this.take = buffer.readBoolean();
        this.value = buffer.readInt();
        this.quickMove = buffer.readBoolean();
        this.slotIndex = buffer.readInt();








    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeItemStack(this.itemStack, false);
        buffer.writeBoolean(this.take);
        buffer.writeInt(this.value);
        buffer.writeBoolean(this.quickMove);
        buffer.writeInt(this.slotIndex);


    }







    public static void handle(TakeOutFromStorageInventoryEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();



                if(serverPlayer.containerMenu instanceof StorageControllerMenu menu) {


                    menu.interactWithMenu(packet.itemStack, packet.take,
                            packet.value, packet.quickMove, packet.slotIndex);



                }









            }

        });

        context.setPacketHandled(true);
    }
}
