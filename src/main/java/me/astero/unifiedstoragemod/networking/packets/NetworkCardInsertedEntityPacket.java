package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.items.generic.NetworkItem;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.Optional;

public class NetworkCardInsertedEntityPacket implements EntityPacket {





    private BlockPos blockPos;


    public NetworkCardInsertedEntityPacket(BlockPos blockPos) {



        this.blockPos = blockPos;


    }

    public NetworkCardInsertedEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here
        this.blockPos = buffer.readBlockPos();


    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeBlockPos(this.blockPos);


    }







    public static void handle(NetworkCardInsertedEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                ServerPlayer player = context.getSender();


                if(player.containerMenu instanceof StorageControllerMenu menu) {

                    ItemStack itemStack = menu.getCarried();
                    if(!(itemStack.getItem() instanceof NetworkItem)) {

                        itemStack = menu.getStorageControllerEntity().getNetworkInventory().getStackInSlot(0);
                    }

                    menu.getStorageControllerEntity().updateNetworkCardItems(itemStack,
                            player);


                }




            }

        });

        context.setPacketHandled(true);
    }

    private static void changeCraftingResult(ItemStack stack, StorageControllerMenu menu, ServerPlayer player) {
        ModNetwork.sendToClient(new SendCraftingResultEntityPacket(stack), player);

        menu.changeCraftingResultSlot(stack); // server side
    }
}
