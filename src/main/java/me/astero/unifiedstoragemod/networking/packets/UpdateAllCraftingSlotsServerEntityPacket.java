package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.ArrayList;
import java.util.List;

public class UpdateAllCraftingSlotsServerEntityPacket implements EntityPacket {


    public BlockPos blockPos;
    public List<ItemStack> items;



    public UpdateAllCraftingSlotsServerEntityPacket(List<ItemStack> items, BlockPos blockPos) {



        this.items = items;
        this.blockPos = blockPos;


    }

    public UpdateAllCraftingSlotsServerEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        items = new ArrayList<>();


        for(int i = 0; i < 9; i++) {
            items.add(buffer.readItem());
        }

        this.blockPos = buffer.readBlockPos();



    }

    @Override
    public void encode(FriendlyByteBuf buffer) {



        for(int i = 0; i < 9; i++) {
            buffer.writeItemStack(this.items.get(i), false);
        }

        buffer.writeBlockPos(this.blockPos);



    }







    public static void handle(UpdateAllCraftingSlotsServerEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                Player player = context.getSender();
                Level level = player.level();
                BlockEntity blockEntity = level.getBlockEntity(packet.blockPos);

                if(blockEntity instanceof StorageControllerEntity d) {


                    AbstractContainerMenu abstractMenu = player.containerMenu;

                    if(abstractMenu instanceof StorageControllerMenu menu) {


                        if(!menu.getStorageControllerEntity().getBlockPos().equals(packet.blockPos)) { // different menu
                            return;
                        } // checking again on the server if somehow the client managed to override it on client-side and get to server-side.

                        for(int i = 0; i < packet.items.size(); i++) {
                            menu.craftSlots.setItem(i, packet.items.get(i));
                        }


                        System.out.println("chjanged");
                    }

                }


            }

        });

        context.setPacketHandled(true);
    }
}
