package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.UnifiedStorage;
import me.astero.unifiedstoragemod.blocks.entity.DrawerGridControllerEntity;
import me.astero.unifiedstoragemod.data.ItemIdentifier;
import me.astero.unifiedstoragemod.menu.GridControllerMenu;
import me.astero.unifiedstoragemod.menu.data.ViewOnlySlot;
import me.astero.unifiedstoragemod.menu.enums.InventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateStorageInventoryEntityPacket implements EntityPacket {

    InventoryAction action;
    ItemStack itemStack;
    ClickType clickType;
    int slotIndex;

    boolean cameFromStorage;




    public UpdateStorageInventoryEntityPacket(ItemStack itemStack, InventoryAction action, ClickType clickType,
                                             int slotIndex, boolean cameFromStorage) {

        this.itemStack = itemStack;
        this.action = action;
        this.clickType = clickType;
        this.slotIndex = slotIndex;
        this.cameFromStorage = cameFromStorage;

    }

    public UpdateStorageInventoryEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        this.itemStack = buffer.readItem();
        this.action = buffer.readEnum(InventoryAction.class);
        this.clickType = buffer.readEnum(ClickType.class);
        this.slotIndex = buffer.readInt();
        this.cameFromStorage = buffer.readBoolean();







    }

    @Override
    public void encode(FriendlyByteBuf buffer) {


        buffer.writeItemStack(this.itemStack, false);
        buffer.writeEnum(this.action);
        buffer.writeEnum(this.clickType);
        buffer.writeInt(this.slotIndex);
        buffer.writeBoolean(this.cameFromStorage);


    }







    public static void handle(UpdateStorageInventoryEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {



            if (!context.isClientSide()) {



                ServerPlayer serverPlayer = context.getSender();

                InventoryMenu playerInventory = serverPlayer.inventoryMenu;





                if(serverPlayer.containerMenu instanceof GridControllerMenu menu) {



                    if(packet.action == InventoryAction.DROP_ITEMS && packet.clickType != ClickType.QUICK_CRAFT) {

                        System.out.println("Yea");
                        serverPlayer.drop(menu.getCarried(), true);
                        menu.setCarried(ItemStack.EMPTY);




                        return;
                    }






                    menu.interactWithMenu(packet.clickType, packet.action,
                            packet.itemStack, menu.getSlot(packet.slotIndex),
                            packet.cameFromStorage);






                    menu.lastClickedSlot = packet.slotIndex;





                }









            }

        });

        context.setPacketHandled(true);
    }
}
