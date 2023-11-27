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
    int amount, slotIndex;
    ItemStack itemStack;
    boolean quickMove, take;




    public UpdateStorageInventoryClientEntityPacket(BlockPos blockPos, int amount,
                                                    ItemStack itemStack, int slotIndex, boolean quickMove, boolean take) {

        this.blockPos = blockPos;
        this.amount = amount;
        this.itemStack = itemStack;
        this.slotIndex = slotIndex;
        this.quickMove = quickMove;
        this.take = take;

    }

    public UpdateStorageInventoryClientEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.blockPos = buffer.readBlockPos();
        this.amount = buffer.readInt();
        this.itemStack = buffer.readItem();
        this.slotIndex = buffer.readInt();
        this.quickMove = buffer.readBoolean();
        this.take = buffer.readBoolean();






    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeBlockPos(this.blockPos);
        buffer.writeInt(this.amount);
        buffer.writeItemStack(this.itemStack, false);
        buffer.writeInt(this.slotIndex);
        buffer.writeBoolean(this.quickMove);
        buffer.writeBoolean(this.take);

    }







    public static void handle(UpdateStorageInventoryClientEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (context.isClientSide()) {



                BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(packet.blockPos);

                if(blockEntity instanceof DrawerGridControllerEntity d) {

                    AbstractContainerMenu abstractMenu = Minecraft.getInstance().player.containerMenu;

                    if(abstractMenu instanceof GridControllerMenu menu) {


                        System.out.println(d.mergedStorageContents.size() + " INSIDE");

                        menu.updateInsertVisual(d, packet.itemStack,
                                packet.amount, packet.quickMove, packet.slotIndex, packet.take);

                    }

                }


            }

        });

        context.setPacketHandled(true);
    }
}
