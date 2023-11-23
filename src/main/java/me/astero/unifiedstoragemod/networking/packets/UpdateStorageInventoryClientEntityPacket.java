package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.menu.enums.MouseAction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UpdateStorageInventoryClientEntityPacket implements EntityPacket {

    BlockPos blockPos;
    int amount;
    ItemStack itemStack;




    public UpdateStorageInventoryClientEntityPacket(BlockPos blockPos, int amount, ItemStack itemStack) {

        this.blockPos = blockPos;
        this.amount = amount;
        this.itemStack = itemStack;

    }

    public UpdateStorageInventoryClientEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.blockPos = buffer.readBlockPos();
        this.amount = buffer.readInt();
        this.itemStack = buffer.readItem();






    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeBlockPos(this.blockPos);
        buffer.writeInt(this.amount);
        buffer.writeItemStack(this.itemStack, false);

    }







    public static void handle(UpdateStorageInventoryClientEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (context.isClientSide()) {



                BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(packet.blockPos);

                if(blockEntity instanceof DrawerGridControllerEntity d) {

                    AbstractContainerMenu abstractMenu = Minecraft.getInstance().player.containerMenu;

                    if(abstractMenu instanceof GridControllerMenu menu) {

                        menu.updateInsertVisual(d, packet.itemStack, packet.amount);
                    }

                }


            }

        });

        context.setPacketHandled(true);
    }
}
