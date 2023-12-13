package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class UpdateCraftingSlotsEntityPacket implements EntityPacket {





    private ItemStack itemStack, itemStackToStore;
    private int slot;
    private boolean populateCraftingSlot, moveToPlayer;


    public UpdateCraftingSlotsEntityPacket(ItemStack itemStack, int slot, boolean populateCraftingSlot, ItemStack itemStackToStore, boolean moveToPlayer) {

        this.itemStack = itemStack;
        this.slot = slot;
        this.populateCraftingSlot = populateCraftingSlot;
        this.itemStackToStore = itemStackToStore;
        this.moveToPlayer = moveToPlayer;



    }

    public UpdateCraftingSlotsEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here
        this.itemStack = buffer.readItem();
        this.slot = buffer.readInt();
        this.populateCraftingSlot = buffer.readBoolean();
        this.itemStackToStore = buffer.readItem();
        this.moveToPlayer = buffer.readBoolean();



    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

        buffer.writeItemStack(this.itemStack, false);
        buffer.writeInt(this.slot);
        buffer.writeBoolean(this.populateCraftingSlot);
        buffer.writeItemStack(this.itemStackToStore, false);
        buffer.writeBoolean(this.moveToPlayer);


    }







    public static void handle(UpdateCraftingSlotsEntityPacket packet, CustomPayloadEvent.Context context) {


        context.enqueueWork(() -> {




            if (!context.isClientSide()) {


                ServerPlayer player = context.getSender();


                if(player.containerMenu instanceof StorageControllerMenu menu) {



                    if(packet.populateCraftingSlot) {

                        // try to store first whatever is in the crafting slot
                        if(!menu.canInsertItemIntoInventory(packet.itemStackToStore,
                                packet.itemStackToStore.getCount(), packet.slot, packet.moveToPlayer)) {

                            return; // if we cannot store, we shouldn't move forward to putting a new item on the grid.
                        }



                        if(packet.itemStack.equals(ItemStack.EMPTY, false)) {
                            return;
                        }


                        // check if enough
                        if(menu.canRemoveItemFromInventory(packet.itemStack, true, true, 1)) {
                            menu.populateCraftSlots(packet.itemStack, packet.slot);
                        }


                        // SlotsChanged will trigger the recipe change
                    }









                }




            }

        });

        context.setPacketHandled(true);
    }


}
