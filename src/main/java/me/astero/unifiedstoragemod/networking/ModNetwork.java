package me.astero.unifiedstoragemod.networking;


import me.astero.unifiedstoragemod.networking.packets.MergedStorageLocationEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.TakeOutFromStorageInventoryEntityPacket;
import me.astero.unifiedstoragemod.networking.packets.UpdateStorageInventoryClientEntityPacket;
import me.astero.unifiedstoragemod.utils.ModUtils;
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


        INSTANCE.messageBuilder(TakeOutFromStorageInventoryEntityPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(TakeOutFromStorageInventoryEntityPacket::encode)
                .decoder(TakeOutFromStorageInventoryEntityPacket::new)
                .consumerMainThread(TakeOutFromStorageInventoryEntityPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateStorageInventoryClientEntityPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateStorageInventoryClientEntityPacket::encode)
                .decoder(UpdateStorageInventoryClientEntityPacket::new)
                .consumerMainThread(UpdateStorageInventoryClientEntityPacket::handle)
                .add();




    }

    public static void sendToClient(Object message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToServer(Object message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }


}
