package me.astero.unifiedstoragemod.client;

import me.astero.unifiedstoragemod.renderer.items.wings.StorageWingsModel;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CustomModelLayer {

    public static ModelLayerLocation WINGS
            = new ModelLayerLocation(new ResourceLocation(ModUtils.MODID,
            "wings"), "main");



    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(WINGS, StorageWingsModel::createLayer);
    }
}
