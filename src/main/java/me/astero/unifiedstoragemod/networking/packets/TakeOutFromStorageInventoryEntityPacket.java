package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.menu.enums.InventoryAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class TakeOutFromStorageInventoryEntityPacket implements EntityPacket {


    ItemStack itemStack;





    public TakeOutFromStorageInventoryEntityPacket(ItemStack itemStack) {

        this.itemStack = itemStack;


    }

    public TakeOutFromStorageInventoryEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.itemStack = buffer.readItem();








    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeItemStack(this.itemStack, false);



    }







    public static void handle(TakeOutFromStorageInventoryEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();

                InventoryMenu playerInventory = serverPlayer.inventoryMenu;





                if(serverPlayer.containerMenu instanceof GridControllerMenu menu) {





                    menu.interactWithMenu(packet.itemStack);












                }









            }

        });

        context.setPacketHandled(true);
    }
}
