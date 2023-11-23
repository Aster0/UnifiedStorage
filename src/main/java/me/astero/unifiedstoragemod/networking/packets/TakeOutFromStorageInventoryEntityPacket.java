package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class TakeOutFromStorageInventoryEntityPacket implements EntityPacket {


    ItemStack itemStack;
    boolean take;
    int value;

    boolean quickMove;





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








    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeItemStack(this.itemStack, false);
        buffer.writeBoolean(this.take);
        buffer.writeInt(this.value);
        buffer.writeBoolean(this.quickMove);



    }







    public static void handle(TakeOutFromStorageInventoryEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();

                InventoryMenu playerInventory = serverPlayer.inventoryMenu;





                if(serverPlayer.containerMenu instanceof GridControllerMenu menu) {


                    menu.interactWithMenu(packet.itemStack, packet.take, packet.value, packet.quickMove);



                }









            }

        });

        context.setPacketHandled(true);
    }
}
