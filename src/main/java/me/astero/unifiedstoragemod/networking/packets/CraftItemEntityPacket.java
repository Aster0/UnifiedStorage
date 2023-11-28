package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Optional;

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

                System.out.println("SERVER!!!!");
                ServerPlayer player = context.getSender();

                if(player.containerMenu instanceof StorageControllerMenu menu) {


                    System.out.println("SERVER!!!");
                    menu.onItemCrafted(packet.itemStack, packet.quickMove);



                }




            }

        });

        context.setPacketHandled(true);
    }
}
