package me.astero.unifiedstoragemod.client;

import me.astero.unifiedstoragemod.registry.BlockEntityRegistry;
import me.astero.unifiedstoragemod.renderer.DrawerItemRenderer;
import me.astero.unifiedstoragemod.renderer.StorageControllerRenderer;
import me.astero.unifiedstoragemod.renderer.items.wings.StorageWingsLayer;
import me.astero.unifiedstoragemod.renderer.items.wings.StorageWingsModel;
import me.astero.unifiedstoragemod.utils.AsteroLogger;
import me.astero.unifiedstoragemod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ModUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterBlockEntities {



    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {


        event.registerBlockEntityRenderer(BlockEntityRegistry.DRAWER_BLOCK_ENTITY.get(),
                DrawerItemRenderer::new);


        event.registerBlockEntityRenderer(BlockEntityRegistry.STORAGE_CONTROLLER_BLOCK_ENTITY.get(),
                StorageControllerRenderer::new);

        AsteroLogger.info("RENDER LOADED");
    }
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers evt)
    {

        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        Map<PlayerSkin.Model, EntityRenderer<? extends Player>> skins = renderManager.getSkinMap();


        for (EntityRenderer<? extends Player> renderer : skins.values()) {
            if (renderer instanceof LivingEntityRenderer livingEntityRenderer) {
                // add render to the player's model
                livingEntityRenderer.addLayer(new StorageWingsLayer<>(livingEntityRenderer));
            }
        }


    }



}
