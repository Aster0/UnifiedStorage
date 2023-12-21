package me.astero.unifiedstoragemod.networking.packets;

import me.astero.unifiedstoragemod.blocks.entity.StorageControllerEntity;
import me.astero.unifiedstoragemod.menu.storage.StorageControllerMenu;
import me.astero.unifiedstoragemod.networking.ClientPacketHandler;
import me.astero.unifiedstoragemod.networking.ModNetwork;
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
import java.util.UUID;

public class UpdateAllCraftingSlotsClientEntityPacket implements EntityPacket {



    public BlockPos blockPos;
    public List<ItemStack> items;
    public UUID playerUUID;


    public UpdateAllCraftingSlotsClientEntityPacket(List<ItemStack> items, BlockPos blockPos, UUID playerUUID) {

        this.items = items;
        this.blockPos = blockPos;
        this.playerUUID = playerUUID;


    }

    public UpdateAllCraftingSlotsClientEntityPacket(FriendlyByteBuf buffer) {

        // Decode your packet data here

        items = new ArrayList<>();

        for(int i = 0; i < 9; i++) {
            items.add(buffer.readItem());
        }

        this.blockPos = buffer.readBlockPos();

        this.playerUUID = buffer.readUUID();

    }

    @Override
    public void encode(FriendlyByteBuf buffer) {




        for(int i = 0; i < 9; i++) {
            buffer.writeItemStack(this.items.get(i), false);
        }

        buffer.writeBlockPos(this.blockPos);
        buffer.writeUUID(this.playerUUID);


    }







    public boolean handle(CustomPayloadEvent.Context context) {


        return ClientPacketHandler.updateAllCraftingSlotsClient(this);

    }
}
