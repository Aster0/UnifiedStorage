package me.astero.mechanicaldrawersmod.networking.packets;

import net.minecraft.network.FriendlyByteBuf;

public interface EntityPacket {


    void encode(FriendlyByteBuf buffer);

}
