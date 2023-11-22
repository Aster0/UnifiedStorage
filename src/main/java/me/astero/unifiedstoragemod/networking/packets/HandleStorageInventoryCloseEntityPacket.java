package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.menu.enums.InventoryAction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class HandleStorageInventoryCloseEntityPacket implements EntityPacket {


    BlockPos blockPos;




    public HandleStorageInventoryCloseEntityPacket(BlockPos blockPos) {

        this.blockPos = blockPos;


    }


    public HandleStorageInventoryCloseEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.blockPos = buffer.readBlockPos();








    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeBlockPos(this.blockPos);



    }







    public static void handle(HandleStorageInventoryCloseEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();

                InventoryMenu playerInventory = serverPlayer.inventoryMenu;


                BlockEntity blockEntity = serverPlayer.level().getBlockEntity(packet.blockPos);


                if(blockEntity instanceof DrawerGridControllerEntity drawerGridControllerEntity) {

                    System.out.println(drawerGridControllerEntity.menu.getCarried());
                    System.out.println(serverPlayer.inventoryMenu.getCarried());

                    System.out.println(drawerGridControllerEntity.menu.getCarried());

                }











            }

        });

        context.setPacketHandled(true);
    }
}
