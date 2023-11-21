package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.UnifiedStorage;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdatePlayerInventoryEntityPacket implements EntityPacket {

    ItemStack itemStack;
    int slotIndex;
    boolean cameFromCustomGUI;




    public UpdatePlayerInventoryEntityPacket(ItemStack itemStack, int slotIndex, boolean cameFromCustomGUI) {
        this.itemStack = itemStack;
        this.slotIndex = slotIndex;
        this.cameFromCustomGUI = cameFromCustomGUI;

    }

    public UpdatePlayerInventoryEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.itemStack = buffer.readItem();
        this.slotIndex = buffer.readInt();
        this.cameFromCustomGUI = buffer.readBoolean();







    }

    @Override
    public void encode(FriendlyByteBuf buffer) {



        buffer.writeItemStack(this.itemStack, false);
        buffer.writeInt(this.slotIndex);
        buffer.writeBoolean(this.cameFromCustomGUI);


    }







    public static void handle(UpdatePlayerInventoryEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();

                InventoryMenu playerInventory = serverPlayer.inventoryMenu;


                System.out.println("Ya " + packet.itemStack);

                if(!packet.cameFromCustomGUI) {
                    playerInventory.setCarried(packet.itemStack);
                }
                else {

                    playerInventory.getSlot(packet.slotIndex).setByPlayer(playerInventory.getCarried());
                    playerInventory.setCarried(ItemStack.EMPTY);
                }









            }

        });

        context.setPacketHandled(true);
    }
}
