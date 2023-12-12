package me.astero.unifiedstoragemod.networking;


import me.astero.unifiedstoragemod.networking.packets.*;
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
                .consumerNetworkThread(MergedStorageLocationEntityPacket::handle)
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

        INSTANCE.messageBuilder(UpdateStorageDisabledEntityPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateStorageDisabledEntityPacket::encode)
                .decoder(UpdateStorageDisabledEntityPacket::new)
                .consumerMainThread(UpdateStorageDisabledEntityPacket::handle)
                .add();

        INSTANCE.messageBuilder(GetCraftingRecipesEntityPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(GetCraftingRecipesEntityPacket::encode)
                .decoder(GetCraftingRecipesEntityPacket::new)
                .consumerMainThread(GetCraftingRecipesEntityPacket::handle)
                .add();


        INSTANCE.messageBuilder(SendCraftingResultEntityPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SendCraftingResultEntityPacket::encode)
                .decoder(SendCraftingResultEntityPacket::new)
                .consumerNetworkThread(SendCraftingResultEntityPacket::handle)
                .add();

        INSTANCE.messageBuilder(CraftItemEntityPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(CraftItemEntityPacket::encode)
                .decoder(CraftItemEntityPacket::new)
                .consumerMainThread(CraftItemEntityPacket::handle)
                .add();

        INSTANCE.messageBuilder(NetworkCardInsertedEntityPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(NetworkCardInsertedEntityPacket::encode)
                .decoder(NetworkCardInsertedEntityPacket::new)
                .consumerMainThread(NetworkCardInsertedEntityPacket::handle)
                .add();




    }

    public static void sendToClient(Object message, ServerPlayer player) {
        INSTANCE.send(message, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToServer(Object message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }


}
