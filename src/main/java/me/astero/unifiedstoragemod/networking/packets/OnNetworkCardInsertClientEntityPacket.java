package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class OnNetworkCardInsertClientEntityPacket implements EntityPacket {


    ItemStack itemStack;





    public OnNetworkCardInsertClientEntityPacket(ItemStack itemStack) {

        this.itemStack = itemStack;

    }

    public OnNetworkCardInsertClientEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.itemStack = buffer.readItem();


    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeItemStack(this.itemStack, false);

    }







    public static void handle(OnNetworkCardInsertClientEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                ServerPlayer serverPlayer = context.getSender();

                if(serverPlayer.containerMenu instanceof GridControllerMenu menu) {


                    menu.getDrawerGridControllerEntity().updateNetworkCardItems(packet.itemStack, serverPlayer);



                }




            }

        });

        context.setPacketHandled(true);
    }
}
