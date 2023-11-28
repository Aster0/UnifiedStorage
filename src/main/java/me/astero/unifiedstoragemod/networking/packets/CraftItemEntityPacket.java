package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class CraftItemEntityPacket implements EntityPacket {




    private ItemStack itemStack;
    private boolean quickMove;



    public CraftItemEntityPacket(ItemStack itemStack, boolean quickMove) {


        this.itemStack = itemStack;
        this.quickMove = quickMove;

    }

    public CraftItemEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here
        this.itemStack = buffer.readItem();
        this.quickMove = buffer.readBoolean();


    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeItemStack(this.itemStack, false);
        buffer.writeBoolean(this.quickMove);

    }







    public static void handle(CraftItemEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                ServerPlayer player = context.getSender();

                if(player.containerMenu instanceof StorageControllerMenu menu) {



                    menu.onItemCrafted(packet.itemStack, packet.quickMove);



                }




            }

        });

        context.setPacketHandled(true);
    }
}
