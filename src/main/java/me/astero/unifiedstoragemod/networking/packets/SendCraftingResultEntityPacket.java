package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SendCraftingResultEntityPacket implements EntityPacket {




    public ItemStack itemStack;



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







    public boolean handle(CustomPayloadEvent.Context context) {


        return ClientPacketHandler.sendCraftingResult(this);
    }
}
