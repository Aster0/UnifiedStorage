package me.astero.mechanicaldrawersmod.networking;

import me.astero.mechanicaldrawersmod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.mechanicaldrawersmod.utils.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;

public class ModNetwork {

    private static SimpleChannel INSTANCE;




    public static void register() {

        INSTANCE = ChannelBuilder.named(new ResourceLocation(ModUtils.MODID, "main"))
                .serverAcceptedVersions((status, version) -> true)
                .clientAcceptedVersions((status, version) -> true)
                .networkProtocolVersion(1)
                .optionalClient()
                .optionalServer().simpleChannel();


        INSTANCE.messageBuilder(MergedStorageLocationEntityPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MergedStorageLocationEntityPacket::encode)
                .decoder(MergedStorageLocationEntityPacket::new)
                .consumerMainThread(MergedStorageLocationEntityPacket::handle)
                .add();

    }

    public static void sendToClient(Object message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }


}
