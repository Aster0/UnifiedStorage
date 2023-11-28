package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SendCraftingResultEntityPacket implements EntityPacket {




    private ItemStack itemStack;



    public SendCraftingResultEntityPacket(ItemStack itemStack) {

        this.itemStack = itemStack;

    }

    public SendCraftingResultEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.itemStack = buffer.readItem();

    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeItemStack(this.itemStack, false);

    }







    public static void handle(SendCraftingResultEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (context.isClientSide()) {


                Player player = Minecraft.getInstance().player;


                if(player.containerMenu instanceof StorageControllerMenu menu) {


                    menu.changeCraftingResultSlot(packet.itemStack);





                }




            }

        });

        context.setPacketHandled(true);
    }
}
